package net.blog.dev.gestion.stocks.back.dropbox;

import java.util.Date;

/**
 * Created by Xebia on 29/07/2014.
 */
public interface IDropboxService {

    String getUrl();

    String authorized(String code);

    void saveFile(String idDropbox);

    boolean getPortfolio(String idDropbox);

    Date getModifiedDatePortfolio(String idDropbox);

}
