/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.blog.dev.gestion.stocks.back.daos.IMovementDao;
import net.blog.dev.gestion.stocks.dto.AccountEnum;
import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.middle.Utils;
import net.blog.dev.gestion.stocks.middle.api.IAddMovementMService;
import net.blog.dev.gestion.stocks.middle.beans.AddMovementBean;

import org.apache.commons.lang3.EnumUtils;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class AddMovementMServiceImpl extends AbstractMService implements
		IAddMovementMService {

	@Inject
	private IMovementDao movementDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.middle.api.IAddMovementMService#addMovement
	 * (net.blog.dev.gestion.stocks.middle.beans.AddMovementBean)
	 */
	@Override
	public void addMovement(AddMovementBean bean) {
		if (validate(bean).size() > 0) {
			throw new IllegalArgumentException("bean not valide");
		}
		DtoMovement movement = new DtoMovement();
		movement.setAccount(EnumUtils.getEnum(AccountEnum.class,
				bean.getAccount()));
		movement.setAutomatic(false);
		movement.setComment(bean.getComment());
		movement.setDate(Utils.getDate(bean.getDate(), "dd/MM/yyyy"));
		movement.setTotal(Utils.getFloat(bean.getTotal()));
		movement.setProvision(bean.isProvision());
		movementDao.saveMovement(movement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.middle.api.IAddMovementMService#validate(
	 * net.blog.dev.gestion.stocks.middle.beans.AddMovementBean)
	 */
	@Override
	public Map<String, String> validate(AddMovementBean bean) {
		final KValidator<AddMovementBean> kValidator = new KValidator<AddMovementBean>();
		return kValidator.validate(bean);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.blog.dev.gestion.stocks.middle.api.IAddMovementMService#getListAccount
	 * ()
	 */
	@Override
	public List<String> getListAccount() {
		return getListEnum(AccountEnum.values());
	}

}
