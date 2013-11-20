/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.anonymization.controller;

import java.io.File;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.anonymization.Anonymizer;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.importer.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * 
 * <p>The anonymization controller is responsible for for controlling the anonymization module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class AnonymizationController {
	/**
	 * Refers to the import module.
	 */
	protected AImportStrategy importStrategy;
	protected File configuredDataFile;
	protected Anonymizer anonymizer;
	
	public AnonymizationController() throws InvalidConfigException {
		this.importStrategy = ((ConfigReader) ServiceFactory.getConfigReader()).getConfiguredImportStrategy();
		this.configuredDataFile = new File( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER ));
		this.anonymizer = new Anonymizer();
	}
	
	/**
	 * Perform the anonymization pipeline. 
	 * The job will be started here.
	 * @return a Map in which a course's name can be used as key to get the related {@link Board} instance.
	 * @throws GeneralLoggingException if the import strategy raised any error
	 */
	public Map<String, Board> performAnonymization() throws GeneralLoggingException {
		// perform import first
		Map<String, Board> unanonymizedCourses = this.importStrategy.importFromFile( this.configuredDataFile );
		
		// iterate through courses and anonymize each board
		for( String course : unanonymizedCourses.keySet() ) {
			Board courseBoard = unanonymizedCourses.get( course );
			this.anonymizer.anonymizeBoard( courseBoard );
		}
		
		return null;	
	}
}
