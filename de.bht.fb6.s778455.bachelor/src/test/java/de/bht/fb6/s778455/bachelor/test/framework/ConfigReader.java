/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.test.framework;

import java.io.File;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IService;

/**
 * <p>
 * This class handles the reading of the unittest.properties configuration file.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.06.2014
 * 
 */
public class ConfigReader extends APropertiesConfigReader implements IService {
    public static final String SERVICE_NAME = "unittest_config_reader";
    
    protected static final File CONFIG_FILE = new File(PATH_CONFIG_UNITTEST + "unittest.properties");
    
    
    public ConfigReader() {
        super(CONFIG_FILE);
    }
    

    /**
     * Check if the AuditoriumImportStrategyTest shall be enabled.
     * @return
     */
    public boolean isAuditoriumImportTestEnabled()
    {
        return super.isValueEnabled(IConfigKeys.UNITTEST_IMPORT_STRATEGY_AUDITORIUM);
    }


    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }
}
