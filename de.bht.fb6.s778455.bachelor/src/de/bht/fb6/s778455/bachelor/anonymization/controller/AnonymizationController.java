/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.anonymization.controller;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.bht.fb6.s778455.bachelor.anonymization.Anonymizer;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.experimental.DirectoryExportStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
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
	protected File configuredExportFile;
	private AExportStrategy exportStrategy;
	private List< String > chainingKeys;
	private IConfigReader anonymConfigReader;

	public AnonymizationController() throws InvalidConfigException {
		this.importStrategy = ( ( ConfigReader ) ServiceFactory
				.getConfigReader() ).getConfiguredImportStrategy();

		this.anonymConfigReader = de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory
				.getConfigReader();
		if( importStrategy instanceof DirectoryImportStrategy ) {
			this.configuredDataFile = new File(
					ServiceFactory
							.getConfigReader()
							.fetchValue(
									IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER ) );
		} else {
			this.configuredDataFile = new File( ServiceFactory
					.getConfigReader().fetchValue(
							IConfigKeys.IMPORT_STRATEGY_POSTGRESQL_DUMPFOLDER ) );
		}
		this.chainingKeys = this.anonymConfigReader
				.fetchMultipleValues( IConfigKeys.ANONYM_STRATEGY_CHAIN );
		this.exportStrategy = ( ( de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader ) de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
				.getConfigReader() ).getConfiguredExportStrategy();

		if( exportStrategy instanceof DirectoryExportStrategy ) {
			this.configuredExportFile = new File(
					de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
							.getConfigReader()
							.fetchValue(
									IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_DATAFOLDER ) );
		} else {
			this.configuredExportFile = new File(
					new File(
							de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
									.getConfigReader()
									.fetchValue(
											IConfigKeys.EXPORT_STRATEGY_SERIALIZED_DATAFOLDER ) ),
					"serialized.ser" );
		}

	}

	public AnonymizationController( AImportStrategy importStrategy,
			AExportStrategy exportStrategy ) throws InvalidConfigException {
		this.importStrategy = importStrategy;
		this.exportStrategy = exportStrategy;

		this.anonymConfigReader = de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory
				.getConfigReader();
		this.chainingKeys = this.anonymConfigReader
				.fetchMultipleValues( IConfigKeys.ANONYM_STRATEGY_CHAIN );
	}

	/**
	 * Perform the anonymization pipeline. The job will be started here.
	 * 
	 * @return a Map in which a course's name can be used as key to get the
	 *         related {@link Board} instance.
	 * @throws GeneralLoggingException
	 *             if the import strategy raised any error
	 */
	public Collection< Course > performAnonymization(
			Collection< Course > rawCourses ) throws GeneralLoggingException {
		// perform import first
		Collection< Course > courses = rawCourses;

		// chaining: create new anonymizer instance
		for( String chainingKey : this.chainingKeys ) {
			String chainingValue = this.anonymConfigReader
					.fetchValue( chainingKey );

			if( "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy"
					.equals( chainingValue )
					|| "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.EnglishNerAnonymizationStrategy"
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
						this._anonymizeCourseBoards( anonymizer, courses );
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
						this._anonymizeCourseBoards( anonymizer, courses );
					}
				}

			} else {
				AAnomyzationStrategy strategy = this.anonymConfigReader
						.getConfiguredClass( chainingKey );

				Anonymizer anonymizer = new Anonymizer( strategy );

				// anonymize boards using the ANerAnonymizationStrategy
				this._anonymizeCourseBoards( anonymizer, courses );
			}
		}

		return courses;
	}

	/**
	 * Anonymize boards using the given anonymizer.
	 * 
	 * @param courses
	 */
	protected void _anonymizeCourseBoards( Anonymizer anonymizer,
			Collection< Course > courses ) {
		for( Course course : courses ) {
			for( Board courseBoard : course.getBoards() ) {
				anonymizer.anonymizeBoard( courseBoard );
			}
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
		System.out.println("starting import...");
		Collection< Course > rawCourses = this.importStrategy
				.importBoardFromFile( this.configuredDataFile );
		System.out.println("import successful");
		System.out.println();
		System.out.println("starting anonymization...");
		Collection< Course > anonymizedCourses = this
				.performAnonymization( rawCourses );
		System.out.println();
		System.out.println("anonymization successful");
		System.out.println("Starting export...");
		long elapsedTime = new Date().getTime() - startTime;		
		this.exportStrategy.exportToFile( anonymizedCourses,
				this.configuredExportFile );
		System.out.println();
		System.out.println("Export successfull");
		System.out.println( getStatistics( anonymizedCourses, elapsedTime ) );
	}

	/**
	 * Get a statistics string.
	 * 
	 * @param anonymizedCourses
	 * @param elapsedTime
	 */
	public String getStatistics( Collection< Course > anonymizedCourses,
			long elapsedTime ) {
		StringBuilder statisticsBuilder = new StringBuilder();

		statisticsBuilder.append( "Import strategy: "
				+ this.importStrategy.getClass() + "\n" );
		if( this.importStrategy instanceof DirectoryImportStrategy ) {
			statisticsBuilder
					.append( "Used encoding of input files: "
							+ ServiceFactory
									.getConfigReader()
									.fetchValue(
											IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_ENCODING )
							+ "\n" );
		}
		statisticsBuilder
				.append( "Used chain of strategies (config keys): "
						+ de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory
								.getConfigReader().fetchValue(
										IConfigKeys.ANONYM_STRATEGY_CHAIN )
						+ "\n" );
		statisticsBuilder.append( "Number of courses: "
				+ anonymizedCourses.size() + "\n" );
		int numberThreads = 0;
		int numberPostings = 0;

		if( ServiceFactory
				.getConfigReader()
				.fetchValue(
						IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC )
				.equals( "false" ) ) {
			statisticsBuilder
					.append( "NameCorpusStrategy: using global corpus. Number of prenames: "
							+ ServiceFactory.getPersonNameCorpusSingleton()
									.getPrenames().size()
							+ "; number of lastnames: "
							+ ServiceFactory.getPersonNameCorpusSingleton()
									.getLastnames().size() + "\n" );
		}
		for( Course course : anonymizedCourses ) {
			if( ServiceFactory
					.getConfigReader()
					.fetchValue(
							IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC )
					.equals( "true" )
					|| ServiceFactory
							.getConfigReader()
							.fetchValue(
									IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC )
							.equals( "fallback" ) ) {
				statisticsBuilder.append( "Course specific name corpus for course: " + course.getTitle() + "\n");
				statisticsBuilder.append( course.getPersonNameCorpus() + "\n");
			}
			// System.out.println("Course: " + course);
			// System.out.println("");
			// System.out.println(".............................");
			for( Board board : course.getBoards() ) {
				// System.out.println("Board: " + board);
				// System.out.println();
				// System.out.println("++++++++++++++++++++++++++++++");
				numberThreads += board.getBoardThreads().size();

				for( BoardThread boardThread : board.getBoardThreads() ) {
					// System.out.println("Thread: " + boardThread);
					// System.out.println();
					// System.out.println("--------------------------------");
					numberPostings += boardThread.getPostings().size();

					// for( Posting p : boardThread.getPostings() ) {
					// // System.out.println("Posting: " + p);
					// }
					// System.out.println("--------------------------------");
				}
				// System.out.println("++++++++++++++++++++++++++++++");
			}
			// System.out.println(".............................");
		}

		statisticsBuilder.append( "Export strategy: "
				+ this.exportStrategy.getClass() + "\n" );
		if( this.exportStrategy instanceof DirectoryExportStrategy ) {
			statisticsBuilder
					.append( "Used encoding of export files: "
							+ de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
									.getConfigReader()
									.fetchValue(
											IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_ENCODING )
							+ "\n" );
		}
		statisticsBuilder.append( "Number of threads: " + numberThreads + "\n" );
		statisticsBuilder.append( "Number of postings: " + numberPostings
				+ "\n" );
		statisticsBuilder.append( "Elapsed time (s): " + elapsedTime / 1000
				+ "\n" );

		return statisticsBuilder.toString();
	}

	public static void main( String[] args ) throws GeneralLoggingException {
		AnonymizationController controller = new AnonymizationController();
		controller.performAnonymizationAnalysis();

		System.out.println( "analysis performed!" );
	}
}
