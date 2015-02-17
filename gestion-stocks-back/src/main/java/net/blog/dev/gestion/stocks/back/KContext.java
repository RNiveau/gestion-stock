/**
 *
 */
package net.blog.dev.gestion.stocks.back;

import net.blog.dev.gestion.stocks.back.migration.Migration;
import net.blog.dev.gestion.stocks.dto.DtoPortfolio;
import net.blog.dev.gestion.stocks.dto.KConfiguration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Kiva
 */
@ApplicationScoped
public class KContext {

    static public String SAVE_FILENAME = "portfolio.k";

    static private Logger logger = LoggerFactory.getLogger(KContext.class);

    private File saveFile;

    /**
     * Save the portfolio state on starup
     */
    private String lastXml;

    private File configurationFile;

    private KConfiguration kConfiguration;

    private DtoPortfolio portfolio;

    @Inject
    @Migration
    private Event<KContext> migration;

    @Inject
    @InitApp
    private Event<String> initialisation;

    public void init(@Observes @Initialize String event) {
        // Chargement de la conf
        String userDir = System.getProperty("user.dir");
        File directory = new File(userDir);
        if (!directory.canRead() || !directory.canWrite())
            throw new BackException("Can't initialize application");
        configurationFile = new File(userDir + "/configuration.k");
        if (!configurationFile.exists()) {
            initConfigurationFile(configurationFile, userDir);
        }
        loadKConfiguration(configurationFile);

        // Chargement du fichier de sauvegarde
        String directionSaveFile = getDirectorySave();
        File tmpSaveFile = new File(directionSaveFile);
        if (!tmpSaveFile.exists())
            throw new BackException("Can't initialize application");
        tmpSaveFile = new File(directionSaveFile + "/" + SAVE_FILENAME);
        if (!tmpSaveFile.exists()) {
            try {
                tmpSaveFile.createNewFile();
                JAXBContext jaxbContext = JAXBContext
                        .newInstance(DtoPortfolio.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.marshal(new DtoPortfolio(), tmpSaveFile);
            } catch (IOException | JAXBException e) {
                throw new BackException("Can't initialize application");
            }
        }
        saveFile = tmpSaveFile;
        loadSaveFile();
        if (migration != null)
            migration.fire(this);
        if (initialisation != null)
            initialisation.fire("");
    }

    private String getDirectorySave() {
        String userDir = System.getProperty("user.dir");
        String directionSaveFile = kConfiguration.getDirectory();
        if (directionSaveFile == null)
            directionSaveFile = userDir;
        return directionSaveFile;
    }

    /**
     *
     */
    public void loadSaveFile() {
        JAXBContext jaxbContext;
        logger.debug("Load save file");
        try {
            jaxbContext = JAXBContext.newInstance(DtoPortfolio.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            portfolio = (DtoPortfolio) unmarshaller.unmarshal(saveFile);
            byte[] content = IOUtils.toByteArray(saveFile.toURI());
            lastXml = new String(content);
        } catch (JAXBException | IOException e) {
            logger.error("Can't read save file");
            throw new BackException("Can't initialize application");
        }
    }

    private void loadKConfiguration(File configuration) {
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(KConfiguration.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            kConfiguration = (KConfiguration) unmarshaller
                    .unmarshal(configuration);
        } catch (JAXBException e) {
            throw new BackException("Can't initialize application");
        }

    }

    private void initConfigurationFile(File configurationFile, String defaultDir) {
        try {
            configurationFile.createNewFile();
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(KConfiguration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            KConfiguration tmpConfiguration = new KConfiguration();
            tmpConfiguration.setDirectory(defaultDir);
            marshaller.marshal(tmpConfiguration, configurationFile);
        } catch (IOException | JAXBException e) {
            throw new BackException("Can't initialize application");
        }
    }

    public void writeFile() {
        try {
            JAXBContext jaxbContext;
            jaxbContext = JAXBContext.newInstance(DtoPortfolio.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(portfolio, saveFile);
        } catch (JAXBException e) {
            throw new BackException("Can't save file: reason is:"
                    + e.getMessage());
        }
    }

    public void saveConfiguration() {
        try {
            JAXBContext jaxbContext;
            jaxbContext = JAXBContext.newInstance(KConfiguration.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(kConfiguration, configurationFile);
        } catch (JAXBException e) {
            throw new BackException("Can't save file: reason is:"
                    + e.getMessage());
        }
    }

    /**
     * @return the saveFile
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * @return the configuration
     */
    public KConfiguration getConfiguration() {
        return kConfiguration;
    }

    /**
     * @return the portfolio
     */
    public DtoPortfolio getPortfolio() {
        return portfolio;
    }

    public boolean needToSave() {
        try {
            JAXBContext jaxbContext;
            StringWriter stringWriter = new StringWriter();
            jaxbContext = JAXBContext.newInstance(DtoPortfolio.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(portfolio, stringWriter);
            return !stringWriter.toString().equals(lastXml);
        } catch (JAXBException e) {
            throw new BackException("Can't save file: reason is:"
                    + e.getMessage());
        }
    }
}
