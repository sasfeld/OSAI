/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization.service;

import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
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
	private static PersonNameCorpus personNameCorpus;
	
	/**
	 * Get the singleton config reader.
	 * @return
	 */
	public static IConfigReader getConfigReader() {
		if(null == configReader) {
			configReader = new ConfigReader();
		}
		return configReader;
	}

	/**
	 * Get the {@link PersonNameCorpus} singleton instance.
	 * @return
	 */
	public static PersonNameCorpus getPersonNameCorpusSingleton() {
		if(null == personNameCorpus) {
			personNameCorpus = new PersonNameCorpus();
		}
		return personNameCorpus;
	}
}
