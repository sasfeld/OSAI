/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.organization.service;

import de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class ....</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
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
