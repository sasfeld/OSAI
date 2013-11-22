/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization.service;

import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class realizes a ServiceFactory for the importer module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class ServiceFactory {
	protected static IConfigReader configReader;
	
	public static IConfigReader getConfigReader() {
		if(null == configReader) {
			configReader = new ConfigReader();
		}
		return configReader;
	}
}
