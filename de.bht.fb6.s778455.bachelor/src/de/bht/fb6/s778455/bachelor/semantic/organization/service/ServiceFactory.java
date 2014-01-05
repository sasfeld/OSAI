/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.organization.service;

import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.semantic.organization.ConfigReader;

/**
 * <p>This class is a static ServiceFactory for the semantics module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class ServiceFactory {	
	private static IConfigReader configReader;

	/**
	 * Return the config Reader for the semantics module.
	 * @return
	 */
	public static IConfigReader getConfigReader() {
		if ( null == configReader ) {
			configReader = new ConfigReader( );
		}
		
		return configReader;
	}
}
