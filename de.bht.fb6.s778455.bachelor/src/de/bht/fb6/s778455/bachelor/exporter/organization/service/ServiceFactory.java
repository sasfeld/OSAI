/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.organization.service;

import de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class is the ServiceFactory for the exporter module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 *
 */
public class ServiceFactory {
	protected static IConfigReader configReader;
	
	/**
	 * Get the {@link IConfigReader} for the exporter module.
	 * @return
	 */
	public static IConfigReader getConfigReader() {
		if ( null == configReader ) {
			configReader = new ConfigReader();
		}
		return configReader;
	}

}
