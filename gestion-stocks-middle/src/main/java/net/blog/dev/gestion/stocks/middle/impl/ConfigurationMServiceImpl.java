/**
 *
 */
package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.back.KContext;
import net.blog.dev.gestion.stocks.back.dropbox.IDropboxService;
import net.blog.dev.gestion.stocks.dto.KConfiguration;
import net.blog.dev.gestion.stocks.middle.api.IConfigurationMService;
import net.blog.dev.gestion.stocks.middle.beans.ConfigurationBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Kiva
 */
@ApplicationScoped
public class ConfigurationMServiceImpl implements IConfigurationMService {

    static final private Logger logger = LoggerFactory.getLogger(ConfigurationMServiceImpl.class);

    @Inject
    private KContext context;

    @Inject
    private IDropboxService dropboxService;

    @Override
    public ConfigurationBean getConfiguration() {
        final KConfiguration configuration = context.getConfiguration();
        final ConfigurationBean configurationBean = new ConfigurationBean();
        configurationBean.setDirectory(configuration.getDirectory());
        configurationBean.setSrdLoan(configuration.getSrdLoan());
        configurationBean.setIdDropbox(configuration.getIdDropbox());
        return configurationBean;
    }

    @Override
    public void setConfiguration(ConfigurationBean configurationBean) {
        final KConfiguration configuration = context.getConfiguration();
        if (configurationBean.getDirectory() != null)
            configuration.setDirectory(configurationBean.getDirectory());
        if (configurationBean.getSrdLoan() != null)
            configuration.setSrdLoan(configurationBean.getSrdLoan());
        if (configurationBean.getIdDropbox() != null)
            configuration.setIdDropbox(configurationBean.getIdDropbox());
        context.saveConfiguration();
    }

    public String getDropboxUrl() {
        return dropboxService.getUrl();
    }

    @Override
    public void saveDropboxAccount(String code) {
        String authorized = dropboxService.authorized(code);
        if (authorized != null) {
            logger.debug("Dropbox returns a code : {}, saves it", authorized);
            ConfigurationBean configuration = getConfiguration();
            configuration.setIdDropbox(authorized);
            setConfiguration(configuration);
        }
    }

    @Override
    public void saveOnExit() {
        final KConfiguration configuration = context.getConfiguration();
        if (StringUtils.isNotBlank(configuration.getIdDropbox())) {
            dropboxService.saveFile(configuration.getIdDropbox());
        }
    }

    @Override
    public void getFromDropbox() {
        final KConfiguration configuration = context.getConfiguration();
        if (StringUtils.isNotBlank(configuration.getIdDropbox())) {
            if (dropboxService.getPortfolio(configuration.getIdDropbox())) {
                context.loadSaveFile();
            }
        }

    }
}
