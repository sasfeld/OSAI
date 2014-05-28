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
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodleXmlImporterStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.GeneralStatisticsBuilder;

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
	private final AExportStrategy exportStrategy;
	private final List< String > chainingKeys;
	private final IConfigReader anonymConfigReader;

	public AnonymizationController() throws InvalidConfigException {
		this.importStrategy = ( ( ConfigReader ) ServiceFactory
				.getConfigReader() ).getConfiguredImportStrategy();

		this.anonymConfigReader = de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory
				.getConfigReader();
		if( this.importStrategy instanceof DirectoryImportStrategy ) {
			this.configuredDataFile = new File(
					ServiceFactory
							.getConfigReader()
							.fetchValue(
									IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER ) );
		} else if( this.importStrategy instanceof MoodleXmlImporterStrategy ) {
			this.configuredDataFile = new File( ServiceFactory
					.getConfigReader().fetchValue(
							IConfigKeys.IMPORT_STRATEGY_LUEBECK_DATA ) );
		} else {
			this.configuredDataFile = new File( ServiceFactory
					.getConfigReader().fetchValue(
							IConfigKeys.IMPORT_STRATEGY_POSTGRESQL_DUMPFOLDER ) );
		}
		this.chainingKeys = this.anonymConfigReader
				.fetchMultipleValues( IConfigKeys.ANONYM_STRATEGY_CHAIN );
		this.exportStrategy = ( ( de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader ) de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory
				.getConfigReader() ).getConfiguredExportStrategy();

		if( this.exportStrategy instanceof DirectoryExportStrategy ) {
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

	public AnonymizationController( final AImportStrategy importStrategy,
			final AExportStrategy exportStrategy ) throws InvalidConfigException {
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
	public LmsCourseSet performAnonymization(
			final LmsCourseSet rawCourses ) throws GeneralLoggingException {
		// perform import first
		final LmsCourseSet courses = rawCourses;

		// chaining: create new anonymizer instance
		for( final String chainingKey : this.chainingKeys ) {
			// break the processing if the chaining key is empty
			if ( "".equals(chainingKey.trim()) ) {
				break;
			}
			
			final String chainingValue = this.anonymConfigReader
					.fetchValue( chainingKey );

			if( "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy"
					.equals( chainingValue )
					|| "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.EnglishNerAnonymizationStrategy"
							.equals( chainingValue ) ) {
				// cascades: create cascades of corpora
				final String cascadeKey = ( chainingValue
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
					for( final String corpusFile : corpora ) {
						final AAnomyzationStrategy strategy = this.anonymConfigReader
								.getConfiguredClass( chainingKey, new File(
										corpusFile ) );
						final Anonymizer anonymizer = new Anonymizer( strategy );

						// anonymize boards using the ANerAnonymizationStrategy
						this._anonymizeCourseBoards( anonymizer, courses );
					}
				} else { // cascades is a list of properties' keys
					for( final String corpusKey : corpora ) {
						final AAnomyzationStrategy strategy = this.anonymConfigReader
								.getConfiguredClass(
										chainingKey,
										new File( this.anonymConfigReader
												.fetchValue( corpusKey ) ) );
						final Anonymizer anonymizer = new Anonymizer( strategy );

						// anonymize boards using the ANerAnonymizationStrategy
						this._anonymizeCourseBoards( anonymizer, courses );
					}
				}

			} else {
				final AAnomyzationStrategy strategy = this.anonymConfigReader
						.getConfiguredClass( chainingKey );

				final Anonymizer anonymizer = new Anonymizer( strategy );

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
	protected void _anonymizeCourseBoards( final Anonymizer anonymizer,
			final Collection< Course > courses ) {
		for( final Course course : courses ) {
			for( final Board courseBoard : course.getBoards() ) {
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
		final long startTime = new Date().getTime();
		System.out.println( "starting import..." );
		final LmsCourseSet rawCourses = this.importStrategy
				.importBoardFromFile( this.configuredDataFile );
		System.out.println( "import successful" );
		System.out.println();
		System.out.println( "starting anonymization..." );
		final LmsCourseSet anonymizedCourses = this
				.performAnonymization( rawCourses );
		System.out.println();
		System.out.println( "anonymization successful" );
		System.out.println( "Starting export..." );
		final long elapsedTime = new Date().getTime() - startTime;
		this.exportStrategy.exportToFile( anonymizedCourses,
				this.configuredExportFile );
		System.out.println();
		System.out.println( "Export successfull" );
		System.out.println( this.getStatistics( anonymizedCourses, elapsedTime ) );
	}

	/**
	 * Get a statistics string.
	 * 
	 * @param anonymizedCourses
	 * @param elapsedTime
	 */
	public String getStatistics( final LmsCourseSet anonymizedCourses,
			final long elapsedTime ) {
		final StringBuilder statisticsBuilder = new StringBuilder();

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
		for( final Course course : anonymizedCourses ) {
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
				statisticsBuilder
						.append( "Course specific name corpus for course: "
								+ course.getTitle() + "\n" );
				statisticsBuilder.append( course.getPersonNameCorpus() + "\n" );
			}			
			
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
		
		final AStatisticsBuilder generalBuilder = new GeneralStatisticsBuilder();
		statisticsBuilder.append( generalBuilder.buildStatistics( anonymizedCourses ).toString() + "\n" );
		
		statisticsBuilder.append( "Elapsed time (s): " + elapsedTime / 1000
				+ "\n" );

		return statisticsBuilder.toString();
	}

	public static void main( final String[] args ) throws GeneralLoggingException {
		final AnonymizationController controller = new AnonymizationController();
		controller.performAnonymizationAnalysis();

		System.out.println( "analysis performed!" );
	}
}
