/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.organization;

import java.io.File;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;

/**
 * <p>This class realizes the config reader of the exporter module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 *
 */
public class ConfigReader extends APropertiesConfigReader {

	private static final String PATH_EXPORT_PROPERTIES = "./conf/exporter.properties";

	/**
	 * Create a new special config reader.
	 * @param inputFile
	 */
	public ConfigReader(  ) {
		super( new File( PATH_EXPORT_PROPERTIES ) );
	}
	
}
