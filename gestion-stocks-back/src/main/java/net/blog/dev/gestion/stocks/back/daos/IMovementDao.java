/**
 * 
 */
package net.blog.dev.gestion.stocks.back.daos;

import java.util.List;

import net.blog.dev.gestion.stocks.dto.DtoMovement;

/**
 * @author Kiva
 * 
 */
public interface IMovementDao {

	List<DtoMovement> getListMovements();

	void saveMovement(DtoMovement movement);
}
