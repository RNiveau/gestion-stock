/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.blog.dev.gestion.stocks.dto.AccountEnum;
import net.blog.dev.gestion.stocks.dto.DirectionEnum;
import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.dto.DtoOrder;
import net.blog.dev.gestion.stocks.dto.DtoStock;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
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
public class InitialisationMServiceImplTest extends AbstractMServiceTest {

	@Inject
	private InitialisationMServiceImpl initialisationMService;

	@Test
	public void automaticMovements() throws Exception {
		List<DtoStock> listStocks = new ArrayList<>();
		final List<DtoMovement> listMovements = new ArrayList<>();

		EasyMock.reset(getStockDao(), getMovementDao());

		EasyMock.expect(getStockDao().getListStocks()).andReturn(listStocks)
				.anyTimes();
		EasyMock.expect(getMovementDao().getListMovements())
				.andReturn(listMovements).anyTimes();

		getMovementDao().saveMovement(EasyMock.isA(DtoMovement.class));
		EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {

			@Override
			public Object answer() throws Throwable {
				listMovements.add((DtoMovement) EasyMock.getCurrentArguments()[0]);
				return null;
			}
		}).anyTimes();

		EasyMock.replay(getStockDao(), getMovementDao());

		// Test nullite
		initialisationMService.automaticMovements();

		// Une action non vendu, on prend la taxe
		DtoStock stock1 = new DtoStock();
		stock1.setBuyOrdre(new DtoOrder());
		stock1.getBuyOrdre().setDate(new DateTime(2012, 2, 1, 1, 1).toDate());
		stock1.getBuyOrdre().setTotalPrice(100f);
		stock1.getBuyOrdre().setTaxes(1f);
		stock1.setDirection(DirectionEnum.BUY);
		stock1.setAccount(AccountEnum.PEA);
		listStocks.add(stock1);

		initialisationMService.automaticMovements();
		Assert.assertEquals(1, listMovements.size());
		DtoMovement dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 1f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

		// Une action vendu pas cette annee
		stock1.setSellOrder(new DtoOrder());
		stock1.getSellOrder().setDate(new DateTime(2012, 2, 1, 1, 1).toDate());
		stock1.getSellOrder().setTotalPrice(110f);
		stock1.getSellOrder().setTaxes(2f);

		listMovements.clear();
		initialisationMService.automaticMovements();
		Assert.assertEquals(2, listMovements.size());
		dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 10f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(1);
		Assert.assertEquals((Float) 3f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

		// Pas de nouvelle action, pas de nouveau mouvement
		initialisationMService.automaticMovements();
		Assert.assertEquals(2, listMovements.size());
		dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 10f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(1);
		Assert.assertEquals((Float) 3f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

		// 2 actions compte courants vendu en 2011, plus une pea plus une pea
		// non vendu

		DtoStock stock2 = new DtoStock();
		stock2.setBuyOrdre(new DtoOrder());
		stock2.getBuyOrdre().setDate(new DateTime(2011, 2, 1, 1, 1).toDate());
		stock2.getBuyOrdre().setTotalPrice(100f);
		stock2.getBuyOrdre().setTaxes(1f);
		stock2.setDirection(DirectionEnum.SELL);
		stock2.setAccount(AccountEnum.SECURITIES);
		stock2.setSellOrder(new DtoOrder());
		stock2.getSellOrder().setDate(new DateTime(2011, 2, 1, 1, 1).toDate());
		stock2.getSellOrder().setTotalPrice(90f);
		stock2.getSellOrder().setTaxes(2f);
		listStocks.add(stock2);

		DtoStock stock3 = new DtoStock();
		stock3.setBuyOrdre(new DtoOrder());
		stock3.getBuyOrdre().setDate(new DateTime(2011, 2, 1, 1, 1).toDate());
		stock3.getBuyOrdre().setTotalPrice(100f);
		stock3.getBuyOrdre().setTaxes(1f);
		stock3.setDirection(DirectionEnum.BUY);
		stock3.setAccount(AccountEnum.SECURITIES);
		stock3.setSellOrder(new DtoOrder());
		stock3.getSellOrder().setDate(new DateTime(2011, 2, 1, 1, 1).toDate());
		stock3.getSellOrder().setTotalPrice(99f);
		stock3.getSellOrder().setTaxes(2f);
		listStocks.add(stock3);

		DtoStock stock4 = new DtoStock();
		stock4.setBuyOrdre(new DtoOrder());
		stock4.getBuyOrdre().setDate(new DateTime(2012, 2, 1, 1, 1).toDate());
		stock4.getBuyOrdre().setTotalPrice(100f);
		stock4.getBuyOrdre().setTaxes(1f);
		stock4.setDirection(DirectionEnum.BUY);
		stock4.setAccount(AccountEnum.PEA);
		listStocks.add(stock4);

		listMovements.clear();
		initialisationMService.automaticMovements();
		Assert.assertEquals(4, listMovements.size());
		dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 9f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(1);
		Assert.assertEquals((Float) 6f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(2);
		Assert.assertEquals((Float) 10f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(3);
		Assert.assertEquals((Float) 4f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

		// Action compte courant 2012 vendu + une vendu dans l'annee en cours
		DtoStock stock5 = new DtoStock();
		stock5.setBuyOrdre(new DtoOrder());
		stock5.getBuyOrdre().setDate(new DateTime(2012, 2, 1, 1, 1).toDate());
		stock5.getBuyOrdre().setTotalPrice(100f);
		stock5.getBuyOrdre().setTaxes(1f);
		stock5.setDirection(DirectionEnum.BUY);
		stock5.setAccount(AccountEnum.SECURITIES);
		stock5.setSellOrder(new DtoOrder());
		stock5.getSellOrder().setDate(new DateTime(2012, 6, 1, 1, 1).toDate());
		stock5.getSellOrder().setTotalPrice(200f);
		stock5.getSellOrder().setTaxes(2f);
		listStocks.add(stock5);

		DtoStock stock6 = new DtoStock();
		stock6.setBuyOrdre(new DtoOrder());
		stock6.getBuyOrdre().setDate(new Date());
		stock6.getBuyOrdre().setTotalPrice(100f);
		stock6.getBuyOrdre().setTaxes(1f);
		stock6.setDirection(DirectionEnum.BUY);
		stock6.setAccount(AccountEnum.SECURITIES);
		stock6.setSellOrder(new DtoOrder());
		stock6.getSellOrder().setDate(new Date());
		stock6.getSellOrder().setTotalPrice(200f);
		stock6.getSellOrder().setTaxes(2f);
		listStocks.add(stock6);

		initialisationMService.automaticMovements();
		Assert.assertEquals(6, listMovements.size());
		dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 9f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(1);
		Assert.assertEquals((Float) 6f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(2);
		Assert.assertEquals((Float) 10f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(3);
		Assert.assertEquals((Float) 4f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(4);
		Assert.assertEquals((Float) 100f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(5);
		Assert.assertEquals((Float) 3f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

		// Pas de nouvelle action, pas de nouveau mouvement
		initialisationMService.automaticMovements();
		Assert.assertEquals(6, listMovements.size());
		dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 9f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(1);
		Assert.assertEquals((Float) 6f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(2);
		Assert.assertEquals((Float) 10f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(3);
		Assert.assertEquals((Float) 4f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(4);
		Assert.assertEquals((Float) 100f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(5);
		Assert.assertEquals((Float) 3f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

		// Action ouverte en 2012 mais non fermer PEA, prise en compte de la
		// taxes en 2012 + une action ouverte en 2011 et ferme en 2012 pea
		DtoStock stock7 = new DtoStock();
		stock7.setBuyOrdre(new DtoOrder());
		stock7.getBuyOrdre().setDate(new DateTime(2012, 2, 1, 1, 1).toDate());
		stock7.getBuyOrdre().setTotalPrice(100f);
		stock7.getBuyOrdre().setTaxes(10f);
		stock7.setDirection(DirectionEnum.BUY);
		stock7.setAccount(AccountEnum.PEA);
		listStocks.add(stock7);

		DtoStock stock8 = new DtoStock();
		stock8.setBuyOrdre(new DtoOrder());
		stock8.getBuyOrdre().setDate(new DateTime(2011, 2, 1, 1, 1).toDate());
		stock8.getBuyOrdre().setTotalPrice(100f);
		stock8.getBuyOrdre().setTaxes(10f);
		stock8.setDirection(DirectionEnum.BUY);
		stock8.setAccount(AccountEnum.PEA);
		stock8.setSellOrder(new DtoOrder());
		stock8.getSellOrder().setDate(new DateTime(2012, 2, 1, 1, 1).toDate());
		stock8.getSellOrder().setTotalPrice(200f);
		stock8.getSellOrder().setTaxes(20f);
		listStocks.add(stock8);

		listMovements.clear();
		initialisationMService.automaticMovements();
		Assert.assertEquals(7, listMovements.size());
		dtoMovement = listMovements.get(0);
		Assert.assertEquals((Float) 10f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(1);
		Assert.assertEquals((Float) 9f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(2);
		Assert.assertEquals((Float) 6f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2012, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(3);
		Assert.assertEquals((Float) 110f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(4);
		Assert.assertEquals((Float) 34f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.PEA, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(5);
		Assert.assertEquals((Float) 100f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_SOLD,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());
		dtoMovement = listMovements.get(6);
		Assert.assertEquals((Float) 3f, (Float) dtoMovement.getTotal());
		Assert.assertEquals(AccountEnum.SECURITIES, dtoMovement.getAccount());
		Assert.assertEquals(InitialisationMServiceImpl.MOVEMENT_TAXES,
				dtoMovement.getComment());
		Assert.assertEquals(new DateTime(2013, 1, 1, 1, 1).toDate(),
				dtoMovement.getDate());

	}

	@Test
	public void soldMovementExist() throws Exception {
		final Method method = InitialisationMServiceImpl.class
				.getDeclaredMethod("soldMovementExist", int.class, List.class,
						AccountEnum.class);
		method.setAccessible(true);

		final ArrayList<DtoMovement> list = new ArrayList<>();
		Assert.assertFalse((boolean) method.invoke(initialisationMService, 0,
				list, AccountEnum.PEA));

		list.add(new DtoMovement());
		Assert.assertFalse((boolean) method.invoke(initialisationMService, 0,
				list, AccountEnum.PEA));

		list.get(0).setAccount(AccountEnum.PEA);
		list.get(0).setAutomatic(true);
		list.get(0).setComment(InitialisationMServiceImpl.MOVEMENT_SOLD);
		list.get(0).setProvision(true);
		list.get(0).setDate(new Date(1));

		// pas la bonne annee
		Assert.assertFalse((boolean) method.invoke(initialisationMService, 0,
				list, AccountEnum.PEA));

		// bonne annee
		Assert.assertTrue((boolean) method.invoke(initialisationMService, 1970,
				list, AccountEnum.PEA));

		// pas le bon compte
		Assert.assertFalse((boolean) method.invoke(initialisationMService,
				1970, list, AccountEnum.SECURITIES));

		// pas automatic
		list.get(0).setAutomatic(false);
		Assert.assertFalse((boolean) method.invoke(initialisationMService,
				1970, list, AccountEnum.PEA));

		// pas le bon commentaire
		list.get(0).setAutomatic(true);
		list.get(0).setComment("toto");
		Assert.assertFalse((boolean) method.invoke(initialisationMService,
				1970, list, AccountEnum.PEA));

	}
}
