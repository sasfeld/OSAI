/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.organization;

import java.io.File;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;

/**
 * <p>Configuration reader for the semantics.properties.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class ConfigReader extends APropertiesConfigReader {
	private static final File CONFIG_FILE = new File( "./conf/semantics.properties" );

	/**
	 * Create a new ConfigReader.
	 * @param inputFile
	 */
	public ConfigReader(  ) {
		super( CONFIG_FILE );	
	}

}
