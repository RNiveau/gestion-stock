package net.blog.dev.gestion.stocks.middle.beans;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.junit.Assert;
import org.junit.Test;

public class ResultYearBeanTest {

	@Test
	public void test() {
		ResultYearBean resultYearBean = new ResultYearBean();
		resultYearBean.addOrder();
		resultYearBean.addOrder();
		resultYearBean.addOrder();
		resultYearBean.addOrderClosed();
		resultYearBean.addOrderPastYear();
		resultYearBean.addOrderPastYear();
		Assert.assertEquals((Integer) 3, resultYearBean.getNbrOrder());
		Assert.assertEquals((Integer) 1, resultYearBean.getNbrOrderClosed());
		Assert.assertEquals((Integer) 2, resultYearBean.getNbrOrderPastYear());

		resultYearBean.addBenefit(100f);
		resultYearBean.addBenefit(150f);
		Assert.assertEquals((Float) 250f, resultYearBean.getBenefit());

		resultYearBean.addDividens(100f);
		resultYearBean.addDividens(50f);
		Assert.assertEquals((Float) 150f, resultYearBean.getTotalDividends());

		resultYearBean.addInvested(110f);
		resultYearBean.addInvested(150f);
		Assert.assertEquals((Float) 260f, resultYearBean.getTotalInvested());

		resultYearBean.addRunning(120f);
		resultYearBean.addRunning(150f);
		Assert.assertEquals((Float) 270f, resultYearBean.getTotalRunning());

		resultYearBean.addRunningPastYear(12f);
		resultYearBean.addRunningPastYear(150f);
		Assert.assertEquals((Float) 162f,
				resultYearBean.getTotalRunningPastYear());

		resultYearBean.addSold(100f);
		resultYearBean.addSold(10f);
		Assert.assertEquals((Float) 110f, resultYearBean.getTotalSold());

		resultYearBean.addTaxes(100f);
		resultYearBean.addTaxes(1f);
		Assert.assertEquals((Float) 101f, resultYearBean.getTaxes());

		Assert.assertEquals((Float) 149f, resultYearBean.getBenefitWithTaxes());

		// 422 d'investi en tout
		DecimalFormat df = new DecimalFormat("#00.00");
		Assert.assertEquals("59"
				+ DecimalFormatSymbols.getInstance()
						.getMonetaryDecimalSeparator() + "24",
				df.format(resultYearBean.getBenefitPercentage()));
		Assert.assertEquals("35"
				+ DecimalFormatSymbols.getInstance()
						.getMonetaryDecimalSeparator() + "31",
				df.format(resultYearBean.getBenefitWithTaxesPercentage()));

	}
}
