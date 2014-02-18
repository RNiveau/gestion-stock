/**
 * 
 */
package net.blog.dev.gestion.stocks.back.daos;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.manager.IConfigurationManager;
import net.blog.dev.gestion.stocks.dto.DtoMovement;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class MovementDaoImpl implements IMovementDao {

	@Inject
	private IConfigurationManager manager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.back.daos.IMovementDao#getListMovements()
	 */
	@Override
	public List<DtoMovement> getListMovements() {
		return manager.getPortfolio().getMovements();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.back.daos.IMovementDao#saveMovement(net.blog
	 * .dev.gestion.stocks.dto.DtoMovement)
	 */
	@Override
	public void saveMovement(DtoMovement movement) {
		manager.getPortfolio().getMovements().add(movement);
		manager.save();
	}
}
