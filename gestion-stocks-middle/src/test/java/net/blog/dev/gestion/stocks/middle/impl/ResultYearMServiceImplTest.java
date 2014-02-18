/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoDividend;
import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.dto.DtoOrder;
import net.blog.dev.gestion.stocks.dto.DtoPortfolio;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.beans.ResultYearBean;

import org.easymock.EasyMock;
import org.jglue.cdiunit.CdiRunner;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Kiva
 * 
 */
@RunWith(CdiRunner.class)
public class ResultYearMServiceImplTest extends AbstractMServiceTest {

	@Inject
	private ResultYearMServiceImpl resultYearMService;

	@Test
	public void bug1() {
		// Bug du cumul des past years
		final ArrayList<DtoStock> listStock = new ArrayList<DtoStock>();
		EasyMock.reset(getStockDao(), getMovementDao());
		EasyMock.expect(getStockDao().getListStocks()).andReturn(listStock)
				.anyTimes();
		EasyMock.expect(getMovementDao().getListMovements()).andReturn(null)
				.anyTimes();
		EasyMock.replay(getStockDao(), getMovementDao());

		// 2 positions ouvertes en 2005, une en 2006, cumul faux en 2006
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2005);
		DtoStock dtoStock = new DtoStock();
		dtoStock.setDirection(DirectionEnum.BUY);
		dtoStock.setBuyOrdre(new DtoOrder());
		dtoStock.getBuyOrdre().setDate(calendar.getTime());
		dtoStock.getBuyOrdre().setTaxes(7.5f);
		dtoStock.getBuyOrdre().setTotalPrice(224.49f);
		dtoStock.setSellOrder(new DtoOrder());
		calendar.set(Calendar.YEAR, 2010);
		dtoStock.getSellOrder().setDate(calendar.getTime());
		dtoStock.getSellOrder().setTotalPrice(306.24f);
		calendar.set(Calendar.YEAR, 2005);
		listStock.add(dtoStock);

		DtoStock dtoStock2 = new DtoStock();
		dtoStock2.setDirection(DirectionEnum.BUY);
		dtoStock2.setBuyOrdre(new DtoOrder());
		dtoStock2.getBuyOrdre().setDate(calendar.getTime());
		dtoStock2.getBuyOrdre().setTaxes(0f);
		dtoStock2.getBuyOrdre().setTotalPrice(480f);
		listStock.add(dtoStock2);

		calendar.set(Calendar.YEAR, 2006);
		DtoStock dtoStock3 = new DtoStock();
		dtoStock3.setDirection(DirectionEnum.BUY);
		dtoStock3.setBuyOrdre(new DtoOrder());
		dtoStock3.getBuyOrdre().setDate(calendar.getTime());
		dtoStock3.getBuyOrdre().setTaxes(7.5f);
		dtoStock3.getBuyOrdre().setTotalPrice(542.23f);
		listStock.add(dtoStock3);

		calendar.set(Calendar.YEAR, 2008);
		DtoStock dtoStock4 = new DtoStock();
		dtoStock4.setDirection(DirectionEnum.BUY);
		dtoStock4.setBuyOrdre(new DtoOrder());
		dtoStock4.getBuyOrdre().setDate(calendar.getTime());
		dtoStock4.getBuyOrdre().setTaxes(7.5f);
		dtoStock4.getBuyOrdre().setTotalPrice(260.99f);
		calendar.set(Calendar.YEAR, 2010);
		dtoStock.getSellOrder().setDate(calendar.getTime());
		dtoStock.getSellOrder().setTotalPrice(314f);
		calendar.set(Calendar.YEAR, 2008);
		listStock.add(dtoStock4);

		final List<ResultYearBean> resultList = resultYearMService
				.getResultList();
		Assert.assertEquals((Integer) 2005, resultList.get(0).getYear());
		Assert.assertEquals((Integer) 2, resultList.get(0).getNbrOrder());
		Assert.assertEquals((Float) 7.5f, resultList.get(0).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(0).getBenefit());
		Assert.assertEquals((Float) 704.49f, resultList.get(0)
				.getTotalInvested());
		Assert.assertEquals((Float) 704.49f, resultList.get(0)
				.getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(0)
				.getTotalRunningPastYear());

		Assert.assertEquals((Integer) 2006, resultList.get(1).getYear());
		Assert.assertEquals((Integer) 1, resultList.get(1).getNbrOrder());
		Assert.assertEquals((Float) 7.5f, resultList.get(1).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(1).getBenefit());
		Assert.assertEquals((Float) 542.23f, resultList.get(1)
				.getTotalInvested());
		Assert.assertEquals((Float) 542.23f, resultList.get(1)
				.getTotalRunning());
		Assert.assertEquals((Float) 704.49f, resultList.get(1)
				.getTotalRunningPastYear());

		Assert.assertEquals((Integer) 2007, resultList.get(2).getYear());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalRunning());
		Assert.assertEquals((Float) 1246.72f, resultList.get(2)
				.getTotalRunningPastYear());

		Assert.assertEquals((Integer) 2008, resultList.get(3).getYear());
		Assert.assertEquals((Float) 260.99f, resultList.get(3)
				.getTotalRunning());
		Assert.assertEquals((Float) 1246.72f, resultList.get(3)
				.getTotalRunningPastYear());
	}

	@Test
	public void bug2() throws FileNotFoundException, IOException, JAXBException {
		// Bug sur cas concret, le cumul des runnings est faux
		final URL url = ClassLoader.getSystemResource("testResultYear.k");
		JAXBContext jaxbContext;
		jaxbContext = JAXBContext.newInstance(DtoPortfolio.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		DtoPortfolio portfolio = (DtoPortfolio) unmarshaller
				.unmarshal(new File(url.getFile()));
		EasyMock.reset(getStockDao(), getMovementDao());
		EasyMock.expect(getStockDao().getListStocks())
				.andReturn(portfolio.getStocks()).once();
		EasyMock.expect(getMovementDao().getListMovements()).andReturn(null)
				.anyTimes();
		EasyMock.replay(getStockDao(), getMovementDao());

		final List<ResultYearBean> resultList = resultYearMService
				.getResultList();

		// 2005
		int index = 0;
		Assert.assertEquals(new Integer(2005), resultList.get(index).getYear());
		Assert.assertEquals(new Float(0.0f), resultList.get(index).getBenefit());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getBenefitPercentage());
		Assert.assertEquals(new Float(-7.5f), resultList.get(index)
				.getBenefitWithTaxes());
		Assert.assertEquals(new Float(-1.0646f), resultList.get(index)
				.getBenefitWithTaxesPercentage());
		Assert.assertEquals(new Integer(2), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(7.5f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getTotalDividends());
		Assert.assertEquals(new Float(704.49f), resultList.get(index)
				.getTotalInvested());
		Assert.assertEquals(new Float(704.49f), resultList.get(index)
				.getTotalRunning());
		Assert.assertEquals(new Float(0f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(0f), resultList.get(index).getTotalSold());

		// 2006
		index = 1;
		Assert.assertEquals(new Integer(2006), resultList.get(index).getYear());
		Assert.assertEquals(new Float(0.0f), resultList.get(index).getBenefit());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getBenefitPercentage());
		Assert.assertEquals(new Float(-7.5f), resultList.get(index)
				.getBenefitWithTaxes());
		Assert.assertEquals(
				new Float(-0.602),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(1), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(7.5f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(19.33f), resultList.get(index)
				.getTotalDividends());
		Assert.assertEquals(new Float(542.23f), resultList.get(index)
				.getTotalInvested());
		Assert.assertEquals(new Float(542.23f), resultList.get(index)
				.getTotalRunning());
		Assert.assertEquals(new Float(704.49f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(0f), resultList.get(index).getTotalSold());

		// 2007
		index = 2;
		Assert.assertEquals(new Integer(2007), resultList.get(index).getYear());
		Assert.assertEquals(new Float(0.0f), resultList.get(index).getBenefit());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getBenefitPercentage());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getBenefitWithTaxes());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getBenefitWithTaxesPercentage());
		Assert.assertEquals(new Integer(0), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(0f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(38.2f), resultList.get(index)
				.getTotalDividends());
		Assert.assertEquals(new Float(0f), resultList.get(index)
				.getTotalInvested());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getTotalRunning());
		Assert.assertEquals(new Float(1246.72f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(0f), resultList.get(index).getTotalSold());

		// 2008
		index = 3;
		Assert.assertEquals(new Integer(2008), resultList.get(index).getYear());
		Assert.assertEquals(new Float(0.0f), resultList.get(index).getBenefit());
		Assert.assertEquals(new Float(0.0f), resultList.get(index)
				.getBenefitPercentage());
		Assert.assertEquals(new Float(-7.5f), resultList.get(index)
				.getBenefitWithTaxes());
		Assert.assertEquals(
				new Float(-0.497f),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(1), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(7.5f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(46.92f),
				roundFloat(resultList.get(index).getTotalDividends(), 2));
		Assert.assertEquals(new Float(260.99f), resultList.get(index)
				.getTotalInvested());
		Assert.assertEquals(new Float(260.99f), resultList.get(index)
				.getTotalRunning());
		Assert.assertEquals(new Float(1246.72f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(0f), resultList.get(index).getTotalSold());

		// 2009
		index = 4;
		Assert.assertEquals(new Integer(2009), resultList.get(index).getYear());
		Assert.assertEquals(new Float(177.35), resultList.get(index)
				.getBenefit());
		Assert.assertEquals(new Float(7.819f),
				roundFloat(resultList.get(index).getBenefitPercentage(), 3));
		Assert.assertEquals(new Float(154.06f), resultList.get(index)
				.getBenefitWithTaxes());
		Assert.assertEquals(
				new Float(6.792f),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(3), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(1), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(23.29f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(55.01f),
				roundFloat(resultList.get(index).getTotalDividends(), 2));
		Assert.assertEquals(new Float(760.39f), resultList.get(index)
				.getTotalInvested());
		Assert.assertEquals(new Float(487.59f),
				roundFloat(resultList.get(index).getTotalRunning(), 2));
		Assert.assertEquals(new Float(1507.71f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(450.15f), resultList.get(index)
				.getTotalSold());

		// 2010
		index = 5;
		Assert.assertEquals(new Integer(2010), resultList.get(index).getYear());
		Assert.assertEquals(new Float(376.15),
				roundFloat(resultList.get(index).getBenefit(), 2));
		Assert.assertEquals(new Float(6.734f),
				roundFloat(resultList.get(index).getBenefitPercentage(), 3));
		Assert.assertEquals(new Float(316.24f),
				roundFloat(resultList.get(index).getBenefitWithTaxes(), 2));
		Assert.assertEquals(
				new Float(5.661f),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(4), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(1), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(3), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(59.91f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(50.22f),
				roundFloat(resultList.get(index).getTotalDividends(), 2));
		Assert.assertEquals(new Float(3590.57f),
				roundFloat(resultList.get(index).getTotalInvested(), 2));
		Assert.assertEquals(new Float(2674.36f),
				roundFloat(resultList.get(index).getTotalRunning(), 2));
		Assert.assertEquals(new Float(1995.3f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(1964.64f), resultList.get(index)
				.getTotalSold());

		// 2011
		index = 6;
		Assert.assertEquals(new Integer(2011), resultList.get(index).getYear());
		Assert.assertEquals(new Float(122.71),
				roundFloat(resultList.get(index).getBenefit(), 2));
		Assert.assertEquals(new Float(2.023f),
				roundFloat(resultList.get(index).getBenefitPercentage(), 3));
		Assert.assertEquals(new Float(100.6f),
				roundFloat(resultList.get(index).getBenefitWithTaxes(), 2));
		Assert.assertEquals(
				new Float(1.658f),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(2), resultList.get(index).getNbrOrder());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(1), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(22.11f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(111.95f),
				roundFloat(resultList.get(index).getTotalDividends(), 2));
		Assert.assertEquals(new Float(2068.72f),
				roundFloat(resultList.get(index).getTotalInvested(), 2));
		Assert.assertEquals(new Float(2068.72f),
				roundFloat(resultList.get(index).getTotalRunning(), 2));
		Assert.assertEquals(new Float(3997.38f), resultList.get(index)
				.getTotalRunningPastYear());
		Assert.assertEquals(new Float(423.5f), resultList.get(index)
				.getTotalSold());

		// 2012
		index = 7;
		Assert.assertEquals(new Integer(2012), resultList.get(index).getYear());
		Assert.assertEquals(new Float(484.61),
				roundFloat(resultList.get(index).getBenefit(), 2));
		Assert.assertEquals(new Float(3.818f),
				roundFloat(resultList.get(index).getBenefitPercentage(), 3));
		Assert.assertEquals(new Float(434.01f),
				roundFloat(resultList.get(index).getBenefitWithTaxes(), 2));
		Assert.assertEquals(
				new Float(3.419f),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(13), resultList.get(index)
				.getNbrOrder());
		Assert.assertEquals(new Integer(8), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(0), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(50.6f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(124.12f),
				roundFloat(resultList.get(index).getTotalDividends(), 2));
		Assert.assertEquals(new Float(6927.4f),
				roundFloat(resultList.get(index).getTotalInvested(), 2));
		Assert.assertEquals(new Float(2117.86f),
				roundFloat(resultList.get(index).getTotalRunning(), 2));
		Assert.assertEquals(new Float(5765.31f),
				roundFloat(resultList.get(index).getTotalRunningPastYear(), 2));
		Assert.assertEquals(new Float(5277.35f), resultList.get(index)
				.getTotalSold());

		// 2013
		index = 8;
		Assert.assertEquals(new Integer(2013), resultList.get(index).getYear());
		Assert.assertEquals(new Float(599.67),
				roundFloat(resultList.get(index).getBenefit(), 2));
		Assert.assertEquals(new Float(3.703f),
				roundFloat(resultList.get(index).getBenefitPercentage(), 3));
		Assert.assertEquals(new Float(505.12f),
				roundFloat(resultList.get(index).getBenefitWithTaxes(), 2));
		Assert.assertEquals(
				new Float(3.119f),
				roundFloat(resultList.get(index)
						.getBenefitWithTaxesPercentage(), 3));
		Assert.assertEquals(new Integer(11), resultList.get(index)
				.getNbrOrder());
		Assert.assertEquals(new Integer(5), resultList.get(index)
				.getNbrOrderClosed());
		Assert.assertEquals(new Integer(5), resultList.get(index)
				.getNbrOrderPastYear());
		Assert.assertEquals(new Float(94.55f), resultList.get(index).getTaxes());
		Assert.assertEquals(new Float(111.20f),
				roundFloat(resultList.get(index).getTotalDividends(), 2));
		Assert.assertEquals(new Float(8309.38f),
				roundFloat(resultList.get(index).getTotalInvested(), 2));
		Assert.assertEquals(new Float(3479.50f),
				roundFloat(resultList.get(index).getTotalRunning(), 2));
		Assert.assertEquals(new Float(7883.17f),
				roundFloat(resultList.get(index).getTotalRunningPastYear(), 2));
		Assert.assertEquals(new Float(8961.7f), resultList.get(index)
				.getTotalSold());

	}

	private Float roundFloat(Float nbr, int scale) {
		float round = 1.0f;
		for (int i = 0; i < scale; i++)
			round *= 10.0f;
		return new Float(Math.round((nbr * round)) / round);
	}

	@Test
	public void getResultList() {
		final ArrayList<DtoStock> listStock = new ArrayList<DtoStock>();
		EasyMock.reset(getStockDao(), getMovementDao());
		EasyMock.expect(getStockDao().getListStocks()).andReturn(null).once()
				.andReturn(listStock).anyTimes();
		EasyMock.expect(getMovementDao().getListMovements()).andReturn(null)
				.anyTimes();
		EasyMock.replay(getStockDao(), getMovementDao());

		// Test nullite
		Assert.assertEquals(0, resultYearMService.getResultList().size());
		Assert.assertEquals(0, resultYearMService.getResultList().size());

		// Test nullite tjs
		listStock.add(null);
		listStock.add(new DtoStock());
		Assert.assertEquals(0, resultYearMService.getResultList().size());

		// Test une seule position fermee la meme annee
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2000);
		DtoStock dtoStock = new DtoStock();
		dtoStock.setDirection(DirectionEnum.BUY);
		dtoStock.setBuyOrdre(new DtoOrder());
		dtoStock.getBuyOrdre().setDate(calendar.getTime());
		dtoStock.getBuyOrdre().setTaxes(10f);
		dtoStock.getBuyOrdre().setTotalPrice(100f);
		dtoStock.setSellOrder(new DtoOrder());
		dtoStock.getSellOrder().setDate(calendar.getTime());
		dtoStock.getSellOrder().setTaxes(10f);
		dtoStock.getSellOrder().setTotalPrice(110f);
		listStock.add(dtoStock);
		List<ResultYearBean> resultList = resultYearMService.getResultList();
		Assert.assertEquals(1, resultList.size());
		Assert.assertEquals((Float) 10f, resultList.get(0).getBenefit());
		Assert.assertEquals((Integer) 1, resultList.get(0).getNbrOrder());
		Assert.assertEquals((Integer) 1, resultList.get(0).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(0)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 20f, resultList.get(0).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(0).getTotalDividends());
		Assert.assertEquals((Float) 100f, resultList.get(0).getTotalInvested());
		Assert.assertEquals((Float) 0f, resultList.get(0).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(0)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultList.get(0).getTotalSold());
		Assert.assertEquals((Integer) 2000, resultList.get(0).getYear());

		// Test 1 position fermee 2000 et une ouverte, Pas de nouvelle en 2001
		// mais dividende et fermeture de l'ancienne, Rien 2002, 1 position 2003
		// avec dividende
		DtoStock dtoStock2 = new DtoStock();
		dtoStock2.setDirection(DirectionEnum.BUY);
		dtoStock2.setBuyOrdre(new DtoOrder());
		dtoStock2.getBuyOrdre().setDate(calendar.getTime());
		dtoStock2.getBuyOrdre().setTaxes(10f);
		dtoStock2.getBuyOrdre().setTotalPrice(100f);
		dtoStock2.setSellOrder(new DtoOrder());
		calendar.set(Calendar.YEAR, 2001);
		dtoStock2.getSellOrder().setDate(calendar.getTime());
		dtoStock2.getSellOrder().setTaxes(10f);
		dtoStock2.getSellOrder().setTotalPrice(110f);
		DtoDividend dtoDividend = new DtoDividend();
		dtoDividend.setDate(calendar.getTime());
		dtoDividend.setTotalPrice(10f);
		dtoStock2.getDividends().add(dtoDividend);
		listStock.add(dtoStock2);

		calendar.set(Calendar.YEAR, 2003);
		DtoStock dtoStock3 = new DtoStock();
		dtoStock3.setDirection(DirectionEnum.BUY);
		dtoStock3.setBuyOrdre(new DtoOrder());
		dtoStock3.getBuyOrdre().setDate(calendar.getTime());
		dtoStock3.getBuyOrdre().setTaxes(10f);
		dtoStock3.getBuyOrdre().setTotalPrice(100f);
		dtoStock3.setSellOrder(new DtoOrder());
		dtoStock3.getSellOrder().setDate(calendar.getTime());
		dtoStock3.getSellOrder().setTaxes(10f);
		dtoStock3.getSellOrder().setTotalPrice(110f);
		DtoDividend dtoDividend2 = new DtoDividend();
		dtoDividend2.setDate(calendar.getTime());
		dtoDividend2.setTotalPrice(10f);
		dtoStock3.getDividends().add(dtoDividend2);
		listStock.add(dtoStock3);

		resultList = resultYearMService.getResultList();
		Assert.assertEquals(4, resultList.size());
		Assert.assertEquals((Float) 10f, resultList.get(0).getBenefit());
		Assert.assertEquals((Integer) 2, resultList.get(0).getNbrOrder());
		Assert.assertEquals((Integer) 1, resultList.get(0).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(0)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 30f, resultList.get(0).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(0).getTotalDividends());
		Assert.assertEquals((Float) 200f, resultList.get(0).getTotalInvested());
		Assert.assertEquals((Float) 100f, resultList.get(0).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(0)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultList.get(0).getTotalSold());
		Assert.assertEquals((Integer) 2000, resultList.get(0).getYear());

		Assert.assertEquals((Float) 10f, resultList.get(1).getBenefit());
		Assert.assertEquals((Integer) 0, resultList.get(1).getNbrOrder());
		Assert.assertEquals((Integer) 0, resultList.get(1).getNbrOrderClosed());
		Assert.assertEquals((Integer) 1, resultList.get(1)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 10f, resultList.get(1).getTaxes());
		Assert.assertEquals((Float) 10f, resultList.get(1).getTotalDividends());
		Assert.assertEquals((Float) 0f, resultList.get(1).getTotalInvested());
		Assert.assertEquals((Float) 0f, resultList.get(1).getTotalRunning());
		Assert.assertEquals((Float) 100f, resultList.get(1)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultList.get(1).getTotalSold());
		Assert.assertEquals((Integer) 2001, resultList.get(1).getYear());

		Assert.assertEquals((Float) 0f, resultList.get(2).getBenefit());
		Assert.assertEquals((Integer) 0, resultList.get(2).getNbrOrder());
		Assert.assertEquals((Integer) 0, resultList.get(2).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(2)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalDividends());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalInvested());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(2)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalSold());
		Assert.assertEquals((Integer) 2002, resultList.get(2).getYear());

		Assert.assertEquals((Float) 10f, resultList.get(3).getBenefit());
		Assert.assertEquals((Integer) 1, resultList.get(3).getNbrOrder());
		Assert.assertEquals((Integer) 1, resultList.get(3).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(3)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 20f, resultList.get(3).getTaxes());
		Assert.assertEquals((Float) 10f, resultList.get(3).getTotalDividends());
		Assert.assertEquals((Float) 100f, resultList.get(3).getTotalInvested());
		Assert.assertEquals((Float) 0f, resultList.get(3).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(3)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultList.get(3).getTotalSold());
		Assert.assertEquals((Integer) 2003, resultList.get(3).getYear());

		// 1 position de plus en 2003 fermee et 1 ouverte
		DtoStock dtoStock4 = new DtoStock();
		dtoStock4.setDirection(DirectionEnum.BUY);
		dtoStock4.setBuyOrdre(new DtoOrder());
		dtoStock4.getBuyOrdre().setDate(calendar.getTime());
		dtoStock4.getBuyOrdre().setTaxes(10f);
		dtoStock4.getBuyOrdre().setTotalPrice(100f);
		dtoStock4.setSellOrder(new DtoOrder());
		dtoStock4.getSellOrder().setDate(calendar.getTime());
		dtoStock4.getSellOrder().setTaxes(10f);
		dtoStock4.getSellOrder().setTotalPrice(110f);
		listStock.add(dtoStock4);

		DtoStock dtoStock5 = new DtoStock();
		dtoStock5.setDirection(DirectionEnum.BUY);
		dtoStock5.setBuyOrdre(new DtoOrder());
		dtoStock5.getBuyOrdre().setDate(calendar.getTime());
		dtoStock5.getBuyOrdre().setTaxes(10f);
		dtoStock5.getBuyOrdre().setTotalPrice(100f);
		listStock.add(dtoStock5);

		resultList = resultYearMService.getResultList();
		Assert.assertEquals(4, resultList.size());
		Assert.assertEquals((Float) 10f, resultList.get(0).getBenefit());
		Assert.assertEquals((Integer) 2, resultList.get(0).getNbrOrder());
		Assert.assertEquals((Integer) 1, resultList.get(0).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(0)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 30f, resultList.get(0).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(0).getTotalDividends());
		Assert.assertEquals((Float) 200f, resultList.get(0).getTotalInvested());
		Assert.assertEquals((Float) 100f, resultList.get(0).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(0)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultList.get(0).getTotalSold());
		Assert.assertEquals((Integer) 2000, resultList.get(0).getYear());

		Assert.assertEquals((Float) 10f, resultList.get(1).getBenefit());
		Assert.assertEquals((Integer) 0, resultList.get(1).getNbrOrder());
		Assert.assertEquals((Integer) 0, resultList.get(1).getNbrOrderClosed());
		Assert.assertEquals((Integer) 1, resultList.get(1)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 10f, resultList.get(1).getTaxes());
		Assert.assertEquals((Float) 10f, resultList.get(1).getTotalDividends());
		Assert.assertEquals((Float) 0f, resultList.get(1).getTotalInvested());
		Assert.assertEquals((Float) 0f, resultList.get(1).getTotalRunning());
		Assert.assertEquals((Float) 100f, resultList.get(1)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultList.get(1).getTotalSold());
		Assert.assertEquals((Integer) 2001, resultList.get(1).getYear());

		Assert.assertEquals((Float) 0f, resultList.get(2).getBenefit());
		Assert.assertEquals((Integer) 0, resultList.get(2).getNbrOrder());
		Assert.assertEquals((Integer) 0, resultList.get(2).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(2)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTaxes());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalDividends());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalInvested());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(2)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 0f, resultList.get(2).getTotalSold());
		Assert.assertEquals((Integer) 2002, resultList.get(2).getYear());

		Assert.assertEquals((Float) 20f, resultList.get(3).getBenefit());
		Assert.assertEquals((Integer) 3, resultList.get(3).getNbrOrder());
		Assert.assertEquals((Integer) 2, resultList.get(3).getNbrOrderClosed());
		Assert.assertEquals((Integer) 0, resultList.get(3)
				.getNbrOrderPastYear());
		Assert.assertEquals((Float) 50f, resultList.get(3).getTaxes());
		Assert.assertEquals((Float) 10f, resultList.get(3).getTotalDividends());
		Assert.assertEquals((Float) 300f, resultList.get(3).getTotalInvested());
		Assert.assertEquals((Float) 100f, resultList.get(3).getTotalRunning());
		Assert.assertEquals((Float) 0f, resultList.get(3)
				.getTotalRunningPastYear());
		Assert.assertEquals((Float) 220f, resultList.get(3).getTotalSold());
		Assert.assertEquals((Integer) 2003, resultList.get(3).getYear());

	}

	@Test
	public void sortStockList() throws Exception {
		final Method method = AbstractMService.class.getDeclaredMethod(
				"sortStockList", List.class);
		method.setAccessible(true);

		// Test nulllite
		method.invoke(resultYearMService, (Object) null);

		List<DtoStock> list = new ArrayList<>();
		method.invoke(resultYearMService, list);

		final DtoStock stock = new DtoStock();
		final DtoOrder order = new DtoOrder();
		order.setDate(new Date(1));
		stock.setBuyOrdre(order);
		list.add(stock);

		final DtoStock stock2 = new DtoStock();
		final DtoOrder order2 = new DtoOrder();
		order2.setDate(new Date(2));
		stock2.setBuyOrdre(order2);
		list.add(stock2);

		final DtoStock stock3 = new DtoStock();
		final DtoOrder order3 = new DtoOrder();
		order3.setDate(new Date(3));
		stock3.setBuyOrdre(order3);
		list.add(stock3);

		method.invoke(resultYearMService, list);
		Assert.assertEquals(new Date(1), list.get(0).getBuyOrdre().getDate());
		Assert.assertEquals(new Date(2), list.get(1).getBuyOrdre().getDate());
		Assert.assertEquals(new Date(3), list.get(2).getBuyOrdre().getDate());

		list.clear();
		list.add(stock3);
		list.add(stock);
		list.add(stock2);
		method.invoke(resultYearMService, list);
		Assert.assertEquals(new Date(1), list.get(0).getBuyOrdre().getDate());
		Assert.assertEquals(new Date(2), list.get(1).getBuyOrdre().getDate());
		Assert.assertEquals(new Date(3), list.get(2).getBuyOrdre().getDate());
	}

	@Test
	public void getResultYearBean() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"getResultYearBean", List.class, Integer.class);
		method.setAccessible(true);

		final Field fieldDao = ResultYearMServiceImpl.class
				.getDeclaredField("movementDao");
		fieldDao.setAccessible(true);

		fieldDao.set(resultYearMService, getMovementDao());

		EasyMock.reset(getMovementDao());
		EasyMock.expect(getMovementDao().getListMovements()).andReturn(null)
				.anyTimes();
		EasyMock.replay(getMovementDao());

		// Test nulllite
		Assert.assertNotNull(method.invoke(resultYearMService, (Object) null,
				(Object) null));
		Assert.assertNotNull(method
				.invoke(resultYearMService, (Object) null, 1));
		Assert.assertNotNull(method.invoke(resultYearMService,
				new ArrayList<>(), (Object) null));

		List<ResultYearBean> list = new ArrayList<>();

		// Sans annee a l'envoi
		ResultYearBean resultYear = (ResultYearBean) method.invoke(
				resultYearMService, list, 1);
		Assert.assertEquals((Integer) 1, resultYear.getYear());
		Assert.assertEquals(1, list.size());

		// Annee n'existe pas
		resultYear = (ResultYearBean) method
				.invoke(resultYearMService, list, 2);
		Assert.assertEquals((Integer) 2, resultYear.getYear());
		Assert.assertEquals(2, list.size());

		// Annee existe
		ResultYearBean resultYear2 = (ResultYearBean) method.invoke(
				resultYearMService, list, 2);
		Assert.assertSame(resultYear, resultYear2);
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void getResultYearBeanDate() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"getResultYearBean", List.class, Date.class);
		method.setAccessible(true);

		final Field fieldDao = ResultYearMServiceImpl.class
				.getDeclaredField("movementDao");
		fieldDao.setAccessible(true);

		fieldDao.set(resultYearMService, getMovementDao());

		EasyMock.reset(getMovementDao());
		EasyMock.expect(getMovementDao().getListMovements()).andReturn(null)
				.anyTimes();
		EasyMock.replay(getMovementDao());

		// Test nulllite
		Assert.assertNotNull(method.invoke(resultYearMService, (Object) null,
				(Object) null));
		Assert.assertNotNull(method.invoke(resultYearMService, (Object) null,
				new Date(1)));
		Assert.assertNotNull(method.invoke(resultYearMService,
				new ArrayList<>(), (Object) null));

		List<ResultYearBean> list = new ArrayList<>();

		// Sans annee a l'envoi
		ResultYearBean resultYear = (ResultYearBean) method.invoke(
				resultYearMService, list, new Date(1));
		Assert.assertEquals((Integer) 1970, resultYear.getYear());
		Assert.assertEquals(1, list.size());

		// Annee n'existe pas
		resultYear = (ResultYearBean) method.invoke(resultYearMService, list,
				new Date(32540000000l));
		Assert.assertEquals((Integer) 1971, resultYear.getYear());
		Assert.assertEquals(2, list.size());

		// Annee existe
		ResultYearBean resultYear2 = (ResultYearBean) method.invoke(
				resultYearMService, list, new Date(31549000000l));
		Assert.assertSame(resultYear, resultYear2);
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void prepareBudget() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"prepareBudget", ResultYearBean.class);
		method.setAccessible(true);

		final Field fieldDao = ResultYearMServiceImpl.class
				.getDeclaredField("movementDao");
		fieldDao.setAccessible(true);

		fieldDao.set(resultYearMService, getMovementDao());

		List<DtoMovement> movements = new ArrayList<>();
		ResultYearBean resultYearBean = new ResultYearBean();

		EasyMock.reset(getMovementDao());
		EasyMock.expect(getMovementDao().getListMovements()).andReturn(null)
				.once().andReturn(movements).anyTimes();
		EasyMock.replay(getMovementDao());

		// Test nulllite
		method.invoke(resultYearMService, resultYearBean);
		Assert.assertEquals((Float) 0f, resultYearBean.getBudget());

		final DtoMovement movement1 = new DtoMovement();
		movement1.setProvision(true);
		movement1.setDate(new DateTime(2012, 1, 1, 1, 1).toDate());
		movement1.setTotal(100f);
		movements.add(movement1);

		final DtoMovement movement2 = new DtoMovement();
		movement2.setProvision(false);
		movement2.setDate(new DateTime(2012, 1, 1, 1, 1).toDate());
		movement2.setTotal(10f);
		movements.add(movement2);

		final DtoMovement movement3 = new DtoMovement();
		movement3.setProvision(true);
		movement3.setDate(new DateTime(2013, 1, 1, 1, 1).toDate());
		movement3.setTotal(100f);
		movements.add(movement3);

		resultYearBean.setYear(2012);
		method.invoke(resultYearMService, resultYearBean);
		Assert.assertEquals((Float) 90f, resultYearBean.getBudget());

		resultYearBean.setYear(2013);
		method.invoke(resultYearMService, resultYearBean);
		Assert.assertEquals((Float) 190f, resultYearBean.getBudget());
	}

	@Test
	public void sortResultList() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"sortResultList", List.class);
		method.setAccessible(true);

		List<ResultYearBean> list = new ArrayList<>();
		// liste vide
		method.invoke(resultYearMService, list);

		ResultYearBean bean1 = new ResultYearBean();
		bean1.setYear(1900);
		ResultYearBean bean2 = new ResultYearBean();
		bean2.setYear(1901);
		ResultYearBean bean3 = new ResultYearBean();
		bean3.setYear(1902);
		list.add(bean1);
		list.add(bean2);
		list.add(bean3);

		method.invoke(resultYearMService, list);
		Assert.assertEquals((Integer) 1900, list.get(0).getYear());
		Assert.assertEquals((Integer) 1901, list.get(1).getYear());
		Assert.assertEquals((Integer) 1902, list.get(2).getYear());

		list.clear();
		list.add(bean3);
		list.add(bean1);
		list.add(bean2);
		list.add(bean3);
		list.add(bean1);
		method.invoke(resultYearMService, list);
		Assert.assertEquals((Integer) 1900, list.get(0).getYear());
		Assert.assertEquals((Integer) 1900, list.get(1).getYear());
		Assert.assertEquals((Integer) 1901, list.get(2).getYear());
		Assert.assertEquals((Integer) 1902, list.get(3).getYear());
		Assert.assertEquals((Integer) 1902, list.get(4).getYear());
	}

	@Test
	public void isSameYearDate() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"isSameYear", Date.class, Date.class);
		method.setAccessible(true);

		// Test nullite
		Assert.assertFalse((boolean) method.invoke(resultYearMService,
				(Object) null, (Object) null));
		Assert.assertFalse((boolean) method.invoke(resultYearMService,
				new Date(1), (Object) null));
		Assert.assertFalse((boolean) method.invoke(resultYearMService,
				(Object) null, new Date(1)));

		Assert.assertTrue((boolean) method.invoke(resultYearMService, new Date(
				1), new Date(1)));
		Assert.assertTrue((boolean) method.invoke(resultYearMService, new Date(
				1), new Date(2)));

		Assert.assertFalse((boolean) method.invoke(resultYearMService,
				new Date(1), new Date(32540000000l)));

	}

	@Test
	public void isSameYear() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"isSameYear", Integer.class, Date.class);
		method.setAccessible(true);

		// Test nullite
		Assert.assertFalse((boolean) method.invoke(resultYearMService,
				(Object) null, (Object) null));
		Assert.assertFalse((boolean) method.invoke(resultYearMService, 1,
				(Object) null));
		Assert.assertFalse((boolean) method.invoke(resultYearMService,
				(Object) null, new Date(1)));

		Assert.assertTrue((boolean) method.invoke(resultYearMService, 1970,
				new Date(1)));
		Assert.assertTrue((boolean) method.invoke(resultYearMService, 1970,
				new Date(2)));
		Assert.assertTrue((boolean) method.invoke(resultYearMService, 1971,
				new Date(32540000000l)));

		Assert.assertFalse((boolean) method.invoke(resultYearMService, 1970,
				new Date(32540000000l)));

	}

	@Test
	public void manageDividends() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"manageDividends", DtoStock.class, ResultYearBean.class);
		method.setAccessible(true);

		DtoStock stock = new DtoStock();
		ResultYearBean result = new ResultYearBean();

		// Test appel sans dividendes, test que ca plante pas
		method.invoke(resultYearMService, stock, result);

		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2000);
		final DtoDividend dtoDividend = new DtoDividend();
		dtoDividend.setDate(calendar.getTime());
		dtoDividend.setTotalPrice(100f);
		stock.getDividends().add(dtoDividend);

		// 1 dividende de la bonne annee
		result.setYear(2000);
		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals((Float) 100f, result.getTotalDividends());

		// 1 dividende de la mauvaise annee
		dtoDividend.setDate(new Date(1));
		result = new ResultYearBean();
		result.setYear(2000);
		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals((Float) 0f, result.getTotalDividends());

		// 3 dividendes de la bonne annee, 2 de la mauvaise et 2 pourries
		dtoDividend.setDate(calendar.getTime());
		DtoDividend dtoDividend2 = new DtoDividend();
		dtoDividend2.setDate(calendar.getTime());
		dtoDividend2.setTotalPrice(201f);
		stock.getDividends().add(dtoDividend2);

		calendar.set(Calendar.YEAR, 2001);
		DtoDividend dtoDividend3 = new DtoDividend();
		dtoDividend3.setDate(calendar.getTime());
		dtoDividend3.setTotalPrice(201f);
		stock.getDividends().add(dtoDividend3);

		calendar.set(Calendar.YEAR, 1999);
		DtoDividend dtoDividend4 = new DtoDividend();
		dtoDividend4.setDate(calendar.getTime());
		dtoDividend4.setTotalPrice(201f);
		stock.getDividends().add(dtoDividend4);

		calendar.set(Calendar.YEAR, 2000);
		DtoDividend dtoDividend5 = new DtoDividend();
		dtoDividend5.setDate(calendar.getTime());
		dtoDividend5.setTotalPrice(10f);
		stock.getDividends().add(dtoDividend5);
		// 2 pourries
		stock.getDividends().add(new DtoDividend());
		stock.getDividends().add(null);
		result = new ResultYearBean();
		result.setYear(2000);
		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals((Float) 311f, result.getTotalDividends());

	}

	@Test
	public void manageSoldStock() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"manageSoldStock", DtoStock.class, ResultYearBean.class);
		method.setAccessible(true);

		DtoStock stock = new DtoStock();
		ResultYearBean result = new ResultYearBean();

		// Test achat positif
		stock.setDirection(DirectionEnum.BUY);
		stock.setBuyOrdre(new DtoOrder());
		stock.getBuyOrdre().setTotalPrice(100f);
		stock.setSellOrder(new DtoOrder());
		stock.getSellOrder().setTaxes(10f);
		stock.getSellOrder().setTotalPrice(150f);

		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals((Float) 50f, result.getBenefit());
		Assert.assertEquals((Float) 10f, result.getTaxes());
		Assert.assertEquals((Float) 150f, result.getTotalSold());

		// Test achat negatif
		stock.getSellOrder().setTotalPrice(50f);

		result = new ResultYearBean();
		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals(new Float(-50f), result.getBenefit());
		Assert.assertEquals((Float) 10f, result.getTaxes());
		Assert.assertEquals((Float) 50f, result.getTotalSold());

		// Test vad positive
		stock.setDirection(DirectionEnum.SELL);
		stock.setBuyOrdre(new DtoOrder());
		stock.getBuyOrdre().setTotalPrice(100f);
		stock.setSellOrder(new DtoOrder());
		stock.getSellOrder().setTaxes(10f);
		stock.getSellOrder().setTotalPrice(50f);

		result = new ResultYearBean();
		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals((Float) 50f, result.getBenefit());
		Assert.assertEquals((Float) 10f, result.getTaxes());
		Assert.assertEquals((Float) 50f, result.getTotalSold());

		// Test vad negative
		stock.getSellOrder().setTotalPrice(150);

		result = new ResultYearBean();
		method.invoke(resultYearMService, stock, result);
		Assert.assertEquals(new Float(-50f), result.getBenefit());
		Assert.assertEquals((Float) 10f, result.getTaxes());
		Assert.assertEquals((Float) 150f, result.getTotalSold());

	}

	@Test
	public void manageRunningPosition() throws Exception {
		final Method method = ResultYearMServiceImpl.class.getDeclaredMethod(
				"manageRunningPosition", List.class, ResultYearBean.class);
		method.setAccessible(true);

		List<DtoStock> listRunning = new ArrayList<>();
		ResultYearBean resultYearBean = new ResultYearBean();
		DtoStock dtoStock = new DtoStock();
		dtoStock.setBuyOrdre(new DtoOrder());
		dtoStock.setDirection(DirectionEnum.BUY);
		dtoStock.getBuyOrdre().setTotalPrice(100f);
		listRunning.add(dtoStock);
		resultYearBean.setYear(2000);

		// Test 0 position sur l'annee de fermer, 0 dividende
		Assert.assertEquals(1, ((ArrayList<?>) method.invoke(
				resultYearMService, listRunning, resultYearBean)).size());
		Assert.assertEquals((Float) 0f, resultYearBean.getBenefit());
		Assert.assertEquals((Integer) 0, resultYearBean.getNbrOrderPastYear());
		Assert.assertEquals((Float) 0f, resultYearBean.getTotalRunning());
		Assert.assertEquals((Float) 100f,
				resultYearBean.getTotalRunningPastYear());

		// Test 1 position sur l'annee de fermer, 1 dividende
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2000);
		dtoStock.setSellOrder(new DtoOrder());
		dtoStock.getSellOrder().setDate(calendar.getTime());
		dtoStock.getSellOrder().setTotalPrice(110f);
		dtoStock.getSellOrder().setTaxes(1f);
		DtoDividend dividend = new DtoDividend();
		dividend.setDate(calendar.getTime());
		dividend.setTotalPrice(10f);
		dtoStock.getDividends().add(dividend);

		resultYearBean = new ResultYearBean();
		resultYearBean.setYear(2000);
		Assert.assertEquals(0, ((ArrayList<?>) method.invoke(
				resultYearMService, listRunning, resultYearBean)).size());
		Assert.assertEquals((Float) 10f, resultYearBean.getBenefit());
		Assert.assertEquals((Integer) 1, resultYearBean.getNbrOrderPastYear());
		Assert.assertEquals((Float) 0f, resultYearBean.getTotalRunning());
		Assert.assertEquals((Float) 100f,
				resultYearBean.getTotalRunningPastYear());
		Assert.assertEquals((Float) 10f, resultYearBean.getTotalDividends());

		// Test 3 position sur l'annee de fermer, 1 non, 1 pas fermee du tout, 2
		// dividendes sur les
		// fermees, 1 sur la non fermee et 1 dividende pas de l'annee
		DtoStock dtoStock2 = new DtoStock();
		dtoStock2.setBuyOrdre(new DtoOrder());
		dtoStock2.setDirection(DirectionEnum.BUY);
		dtoStock2.getBuyOrdre().setTotalPrice(200f);
		dtoStock2.setSellOrder(new DtoOrder());
		dtoStock2.getSellOrder().setDate(calendar.getTime());
		dtoStock2.getSellOrder().setTotalPrice(250f);
		dtoStock2.getSellOrder().setTaxes(1f);
		DtoDividend dividend2 = new DtoDividend();
		dividend2.setDate(calendar.getTime());
		dividend2.setTotalPrice(50f);
		dtoStock2.getDividends().add(dividend2);

		DtoStock dtoStock3 = new DtoStock();
		dtoStock3.setBuyOrdre(new DtoOrder());
		dtoStock3.setDirection(DirectionEnum.SELL);
		dtoStock3.getBuyOrdre().setTotalPrice(100f);
		dtoStock3.setSellOrder(new DtoOrder());
		dtoStock3.getSellOrder().setDate(calendar.getTime());
		dtoStock3.getSellOrder().setTotalPrice(90f);
		dtoStock3.getSellOrder().setTaxes(10f);

		DtoStock dtoStock4 = new DtoStock();
		dtoStock4.setBuyOrdre(new DtoOrder());
		dtoStock4.setDirection(DirectionEnum.SELL);
		dtoStock4.getBuyOrdre().setTotalPrice(100f);
		DtoDividend dividend3 = new DtoDividend();
		dividend3.setDate(calendar.getTime());
		dividend3.setTotalPrice(50f);
		dtoStock4.getDividends().add(dividend3);
		DtoDividend dividend4 = new DtoDividend();
		calendar.set(Calendar.YEAR, 1999);
		dividend4.setDate(calendar.getTime());
		dividend4.setTotalPrice(50f);
		dtoStock4.getDividends().add(dividend4);

		DtoStock dtoStock5 = new DtoStock();
		dtoStock5.setBuyOrdre(new DtoOrder());
		dtoStock5.setDirection(DirectionEnum.SELL);
		dtoStock5.getBuyOrdre().setTotalPrice(100f);
		dtoStock5.setSellOrder(new DtoOrder());
		dtoStock5.getSellOrder().setDate(calendar.getTime());
		dtoStock5.getSellOrder().setTotalPrice(90f);
		dtoStock5.getSellOrder().setTaxes(10f);

		listRunning.add(dtoStock2);
		listRunning.add(dtoStock3);
		listRunning.add(dtoStock4);
		listRunning.add(dtoStock5);

		resultYearBean = new ResultYearBean();
		resultYearBean.setYear(2000);
		Assert.assertEquals(2, ((ArrayList<?>) method.invoke(
				resultYearMService, listRunning, resultYearBean)).size());
		Assert.assertEquals((Float) 70f, resultYearBean.getBenefit());
		Assert.assertEquals((Integer) 3, resultYearBean.getNbrOrderPastYear());
		Assert.assertEquals((Float) 0f, resultYearBean.getTotalRunning());
		Assert.assertEquals((Float) 600f,
				resultYearBean.getTotalRunningPastYear());
		Assert.assertEquals((Float) 110f, resultYearBean.getTotalDividends());

	}
}
