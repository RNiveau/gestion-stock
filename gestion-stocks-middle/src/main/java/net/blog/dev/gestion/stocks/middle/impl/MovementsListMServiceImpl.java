/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.daos.IMovementDao;
import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IMovementsListMService;
import net.blog.dev.gestion.stocks.middle.beans.MovementListBean;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class MovementsListMServiceImpl extends AbstractMService implements
		IMovementsListMService {

	@Inject
	private IMovementDao movementDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.blog.dev.gestion.stocks.middle.api.IMovementsListMService#
	 * getMovementsList()
	 */
	@Override
	public List<MovementListBean> getMovementsList() {
		final ArrayList<MovementListBean> listMovements = new ArrayList<>();

		final List<DtoMovement> listDto = movementDao.getListMovements();
		for (DtoMovement movement : listDto) {
			MovementListBean movementListBean = new MovementListBean();
			movementListBean.setAutomatic(movement.isAutomatic());
			movementListBean.setComment(movement.getComment());
			movementListBean.setDate(Utils.formatDate(movement.getDate(),
					"dd/MM/yyyy"));
			movementListBean.setProvision(movement.isProvision() ? "Credit"
					: "Retrait");
			movementListBean.setTotal(((Float) movement.getTotal()).toString());
			movementListBean.setAccount(movement.getAccount().toString());
			listMovements.add(movementListBean);
		}

		return listMovements;
	}

}
