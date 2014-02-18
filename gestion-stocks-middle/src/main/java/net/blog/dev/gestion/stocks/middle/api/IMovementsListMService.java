/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.List;

import net.blog.dev.gestion.stocks.middle.beans.MovementListBean;

/**
 * @author Kiva
 * 
 */
public interface IMovementsListMService {

	List<MovementListBean> getMovementsList();

}
