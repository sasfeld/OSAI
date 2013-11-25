/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.anonymization.controller;

import java.io.File;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.anonymization.Anonymizer;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * 
 * <p>
 * The anonymization controller is responsible for for controlling the
 * anonymization module.
 * </p>
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
	private AExportStrategy exportStrategy;

	public AnonymizationController() throws InvalidConfigException {
		this.importStrategy = ( ( ConfigReader ) ServiceFactory
				.getConfigReader() ).getConfiguredImportStrategy();
		this.configuredDataFile = new File( ServiceFactory.getConfigReader()
				.fetchValue(
						IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER ) );
		this.anonymizer = new Anonymizer();
		this.exportStrategy = ( ( de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader ) de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
				.getConfigReader() ).getConfiguredExportStrategy();
	}

	/**
	 * Perform the anonymization pipeline. The job will be started here.
	 * 
	 * @return a Map in which a course's name can be used as key to get the
	 *         related {@link Board} instance.
	 * @throws GeneralLoggingException
	 *             if the import strategy raised any error
	 */
	public Map< String, Board > performAnonymization()
			throws GeneralLoggingException {
		// perform import first
		Map< String, Board > courses = this.importStrategy
				.importFromFile( this.configuredDataFile );

		// iterate through courses and anonymize each board
		for( String course : courses.keySet() ) {
			Board courseBoard = courses.get( course );
			this.anonymizer.anonymizeBoard( courseBoard );
		}

		return courses;
	}

	/**
	 * Perform an anonymization analysis using the export module to export the anonymized data.
	 * @throws GeneralLoggingException
	 */
	public void performAnonymizationAnalysis() throws GeneralLoggingException {
		Map< String, Board > anonymizedCourses = this.performAnonymization();
		this.exportStrategy
				.exportToFile(
						anonymizedCourses,
						new File(
								de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
										.getConfigReader()
										.fetchValue(
												IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_DATAFOLDER ) ) );
		
		System.out.println("Starting analysis....");
		for( AAnomyzationStrategy strategy : this.anonymizer.getAnonymizationStrategy().getWrappedStrategies() ) {
			System.out.println(strategy.getDetails());
		}
		System.out.println("Number of courses: " + anonymizedCourses.size());
		int numberThreads = 0;
		int numberPostings = 0;
		for( Board board : anonymizedCourses.values() ) {
			numberThreads += board.getBoardThreads().size();
			
			for( BoardThread boardThread : board.getBoardThreads() ) {
				numberPostings += boardThread.getPostings().size();
			}
		}
		System.out.println("Number of threads: " + numberThreads);
		System.out.println("Number of postings: " + numberPostings);
	}
	
	public static void main( String[] args ) throws GeneralLoggingException {
		AnonymizationController controller = new AnonymizationController();
		controller.performAnonymizationAnalysis();
		
		System.out.println("analysis performed!");
	}
}
