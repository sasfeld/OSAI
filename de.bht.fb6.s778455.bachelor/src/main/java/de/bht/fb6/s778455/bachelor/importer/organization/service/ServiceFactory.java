/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization.service;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.OliverLuebeckStrategy;
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
    protected static ServiceFactory instance;
    
	protected static IConfigReader configReader;
	private static PersonNameCorpus personNameCorpus;
	protected static DirectoryImportStrategy dirImportStrat;
	
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
	
	/**
	 * Get the {@link DirectoryImportStrategy}.
	 * @return
	 */
	public static AImportStrategy getDirectoryImportStrategy() {
	    if(null == dirImportStrat) {
	        dirImportStrat = new DirectoryImportStrategy();
	    }
	    
	    return dirImportStrat;
	}

	/**
	 * Get the singleton ServiceFactory.
	 * @return
	 */
    public static ServiceFactory getInstance() {
       if ( null == instance ) {
           instance = new ServiceFactory();
       }
       
       return instance;
    }

    /**
     * This fabric method creates a new instance of {@link OliverLuebeckStrategy}.
     * Define additional creation logic here.
     * @return
     */
	public static AImportStrategy newOliverLuebeckStrategy() {
		OliverLuebeckStrategy strategy = new OliverLuebeckStrategy();
		return strategy;
	}
}
