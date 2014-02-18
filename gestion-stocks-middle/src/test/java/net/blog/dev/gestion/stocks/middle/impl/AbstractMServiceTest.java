/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import javax.enterprise.inject.Produces;

import net.blog.dev.gestion.stocks.back.daos.IMovementDao;
import net.blog.dev.gestion.stocks.back.daos.IStockDao;

import org.easymock.EasyMock;

/**
 * @author Kiva
 * 
 */
public abstract class AbstractMServiceTest {
	@Produces
	private IMovementDao movementDao = EasyMock.createMock(IMovementDao.class);

	@Produces
	private IStockDao stockDao = EasyMock.createMock(IStockDao.class);

	/**
	 * @return the movementDao
	 */
	protected IMovementDao getMovementDao() {
		return movementDao;
	}

	/**
	 * @return the stockDao
	 */
	protected IStockDao getStockDao() {
		return stockDao;
	}

}
