/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.organization;

import java.io.File;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class contains functionality to read the configuration for the anonymization module from a properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 *
 */
public class ConfigReader extends APropertiesConfigReader implements IConfigReader {
	/**
	 * Path to the anonymization.properties file.
	 */
	public static final String PATH_ANONYMIZATION_PROPERTIES = "./conf/anonymization.properties";
	
	/**
	 * Create a new special config reader for the anonymization module.
	 * @see de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader for the functionality implementation
	 */
	public ConfigReader() {
		super( new File ( PATH_ANONYMIZATION_PROPERTIES ) );
	}

}
