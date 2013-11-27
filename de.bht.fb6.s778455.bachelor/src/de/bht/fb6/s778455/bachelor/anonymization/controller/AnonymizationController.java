/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.anonymization.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
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
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
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
	private AExportStrategy exportStrategy;
	private List< String > chainingKeys;
	private IConfigReader anonymConfigReader;

	public AnonymizationController() throws InvalidConfigException {
		this.importStrategy = ( ( ConfigReader ) ServiceFactory
				.getConfigReader() ).getConfiguredImportStrategy();

		this.anonymConfigReader = de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory
				.getConfigReader();
		this.configuredDataFile = new File( ServiceFactory.getConfigReader()
				.fetchValue(
						IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER ) );
		this.chainingKeys = this.anonymConfigReader
				.fetchMultipleValues( IConfigKeys.ANONYM_STRATEGY_CHAIN );
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

		// chaining: create new anonymizer instance
		for( String chainingKey : this.chainingKeys ) {
			String chainingValue = this.anonymConfigReader
					.fetchValue( chainingKey );

			if( "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy"
					.equals( chainingValue )
					|| "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy"
							.equals( chainingValue ) ) {
				// cascades: create cascades of corpora
				String cascadeKey = ( chainingValue
						.equals( "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy" ) ) ? IConfigKeys.ANONYM_NER_GERMAN_CASCADE
						: IConfigKeys.ANONYM_NER_ENGLISH_CASCADE;
				List< String > corpora = this.anonymConfigReader
						.fetchMultipleValues( cascadeKey );

				// if cascades is configured to "all", fetch multiple corpus
				// properties
				if( 1 == corpora.size() && "all".equals( corpora.get( 0 ) ) ) {
					// corpora are files
					corpora = this.anonymConfigReader
							.fetchMultipleValues( ( chainingValue
									.equals( "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy" ) ) ? IConfigKeys.ANONYM_NER_GERMAN_CORPORA
									: IConfigKeys.ANONYM_NER_ENGLISH_CORPORA );
					for( String corpusFile : corpora ) {
						AAnomyzationStrategy strategy = this.anonymConfigReader
								.getConfiguredClass( chainingKey, new File(
										corpusFile ) );
						Anonymizer anonymizer = new Anonymizer( strategy );

						// anonymize boards using the ANerAnonymizationStrategy
						this._anonymizeBoards( anonymizer, courses );
					}
				} else { // cascades is a list of properties' keys
					for( String corpusKey : corpora ) {
						AAnomyzationStrategy strategy = this.anonymConfigReader
								.getConfiguredClass(
										chainingKey,
										new File( this.anonymConfigReader
												.fetchValue( corpusKey ) ) );
						Anonymizer anonymizer = new Anonymizer( strategy );

						// anonymize boards using the ANerAnonymizationStrategy
						this._anonymizeBoards( anonymizer, courses );
					}
				}

			} else {
				AAnomyzationStrategy strategy = this.anonymConfigReader.getConfiguredClass( chainingKey );
				
				Anonymizer anonymizer = new Anonymizer( strategy );

				// anonymize boards using the ANerAnonymizationStrategy
				this._anonymizeBoards( anonymizer, courses );
			}
		}

		return courses;
	}

	/**
	 * Anonymize boards using the given anonymizer.
	 * 
	 * @param courses
	 */
	protected void _anonymizeBoards( Anonymizer anonymizer,
			Map< String, Board > courses ) {
		for( String course : courses.keySet() ) {
			Board courseBoard = courses.get( course );
			anonymizer.anonymizeBoard( courseBoard );
		}
	}

	/**
	 * Perform an anonymization analysis using the export module to export the
	 * anonymized data.
	 * 
	 * @throws GeneralLoggingException
	 */
	public void performAnonymizationAnalysis() throws GeneralLoggingException {
		long startTime = new Date().getTime();
		Map< String, Board > anonymizedCourses = this.performAnonymization();
		long elapsedTime = new Date().getTime() - startTime;
		
		this.exportStrategy
				.exportToFile(
						anonymizedCourses,
						new File(
								de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
										.getConfigReader()
										.fetchValue(
												IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_DATAFOLDER ) ) );

		System.out.println( "Starting analysis...." );
		System.out
				.println( "Used chain of strategies (config keys): "
						+ de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory
								.getConfigReader().fetchValue(
										IConfigKeys.ANONYM_STRATEGY_CHAIN ) );
		System.out.println( "Number of courses: " + anonymizedCourses.size() );
		int numberThreads = 0;
		int numberPostings = 0;
		for( Board board : anonymizedCourses.values() ) {
			numberThreads += board.getBoardThreads().size();

			for( BoardThread boardThread : board.getBoardThreads() ) {
				numberPostings += boardThread.getPostings().size();
			}
		}
		System.out.println( "Number of threads: " + numberThreads );
		System.out.println( "Number of postings: " + numberPostings );
		System.out.println( "Elapsed time(s): " + elapsedTime / 1000);
	}

	public static void main( String[] args ) throws GeneralLoggingException {
		AnonymizationController controller = new AnonymizationController();
		controller.performAnonymizationAnalysis();

		System.out.println( "analysis performed!" );
	}
}
