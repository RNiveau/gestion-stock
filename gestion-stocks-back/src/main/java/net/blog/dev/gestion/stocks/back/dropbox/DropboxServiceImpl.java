package net.blog.dev.gestion.stocks.back.dropbox;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;

/**
 * Created by Xebia on 29/07/2014.
 */
@ApplicationScoped
public class DropboxServiceImpl implements IDropboxService {

    private final static Logger logger = LoggerFactory.getLogger(DropboxServiceImpl.class);

    private final static String APP_KEY = "05vv5vid51xffen";

    private final static String APP_SECRET = "61ft6z4a3cnx4dh";

    private DbxWebAuthNoRedirect webAuth;

    @PostConstruct
    private void init() {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
                "KStocks", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        logger.debug("Dropbox service initalized");
    }

    @Override
    public String getUrl() {
        return webAuth.start();
    }

    @Override
    public String authorized(String code) {
        try {
            logger.debug("Try to authorized dropbox: {}", code);
            String accessToken = webAuth.finish(code).accessToken;
            logger.debug("Get token: {}", accessToken);
            return accessToken;
        } catch (DbxException e) {
            logger.warn("Error when authorized account in dropbox {}", e.getMessage());
        }
        return null;
    }
}
