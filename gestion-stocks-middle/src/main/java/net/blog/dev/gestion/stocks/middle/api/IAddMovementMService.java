/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.api;

import java.util.List;
import java.util.Map;

import net.blog.dev.gestion.stocks.middle.beans.AddMovementBean;

/**
 * @author Kiva
 * 
 */
public interface IAddMovementMService {

	public void addMovement(AddMovementBean bean);

	public Map<String, String> validate(AddMovementBean bean);

	public List<String> getListAccount();
}
