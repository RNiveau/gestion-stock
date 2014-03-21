package net.blog.dev.gestion.stocks.core;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Created by romainn on 19/03/2014.
 */
public class KWeldContainer {

    static private WeldContainer weldContainer;

    synchronized static public org.jboss.weld.environment.se.WeldContainer getInstance() {
        if (weldContainer == null) {
            weldContainer = new Weld().initialize();
        }
        return weldContainer;
    }
}
