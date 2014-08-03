package net.blog.dev.gestion.stocks.back.dropbox;

import com.dropbox.core.*;
import net.blog.dev.gestion.stocks.back.KContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Xebia on 29/07/2014.
 */
@ApplicationScoped
public class DropboxServiceImpl implements IDropboxService {

    private final static Logger logger = LoggerFactory.getLogger(DropboxServiceImpl.class);

    private final static String APP_KEY = "05vv5vid51xffen";

    private final static String APP_SECRET = "61ft6z4a3cnx4dh";

    private DbxWebAuthNoRedirect webAuth;

    @Inject
    private KContext context;

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

    @Override
    public void saveFile(String idDropbox) {
        logger.debug("Save portfolio in dropbox");
        final File inputFile = context.getSaveFile();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFile);
            final DbxClient client = new DbxClient(new DbxRequestConfig("KStocks/1.0", Locale.getDefault().toString()), idDropbox);
            final String fileName = "portfolio.k";
            final InputStream dropboxStream = inputStream;
            List<DbxEntry> dbxEntries = client.searchFileAndFolderNames("/", fileName);
            Optional<DbxEntry> dropboxFile = dbxEntries.stream().findFirst();
            dropboxFile.ifPresent(file -> {
                try {
                    client.uploadFile(file.path,
                            DbxWriteMode.update(file.asFile().rev), inputFile.length(), dropboxStream);
                } catch (DbxException | IOException e) {
                    logger.warn("Error on upload file : {}", e.getMessage());
                }
            });
            if (!dropboxFile.isPresent()) {
                client.uploadFile("/" + fileName,
                        DbxWriteMode.add(), inputFile.length(), inputStream);
            }
//            File target = new File("Copy of House.jpeg");
//            OutputStream out = new FileOutputStream(target);
//            client.getFile(fileName, null, new BufferedOutputStream());
        } catch (IOException | DbxException e) {
            logger.warn("Error on upload file : {}", e.getMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                logger.warn("Error on upload file : {}", e.getMessage());
            }
        }
    }
}
