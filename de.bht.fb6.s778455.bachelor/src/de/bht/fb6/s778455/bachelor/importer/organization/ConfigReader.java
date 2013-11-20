/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization;

import java.io.File;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class contains functionality to read the configuration for the importer module from a properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class ConfigReader extends APropertiesConfigReader implements IConfigReader {
	/**
	 * Path to the anonymization.properties file.
	 */
	public static final String PATH_IMPORT_PROPERTIES = "./conf/importer.properties";
	
	/**
	 * Create a new special config reader for the importer module.
	 * @see de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader for the functionality implementation
	 */
	public ConfigReader() {
		super( new File ( PATH_IMPORT_PROPERTIES ) );
	}

}
