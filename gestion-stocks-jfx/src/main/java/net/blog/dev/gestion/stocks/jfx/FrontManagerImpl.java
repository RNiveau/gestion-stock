/**
 * 
 */
package net.blog.dev.gestion.stocks.jfx;

import javafx.stage.Window;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Kiva
 * 
 */
@ApplicationScoped
public class FrontManagerImpl implements IFrontManager {

	private Window windowParent;

	@Override
	public Window getWindowParent() {
		return windowParent;
	}

	@Override
	public void setWindowParent(Window parent) {
		windowParent = parent;
	}

}
