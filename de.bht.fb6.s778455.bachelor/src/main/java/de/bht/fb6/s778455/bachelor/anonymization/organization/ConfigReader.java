/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.organization;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>
 * This class contains functionality to read the configuration for the
 * anonymization module from a properties file.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 * 
 */
public class ConfigReader extends APropertiesConfigReader implements
		IConfigReader {
	/**
	 * Path to the anonymization.properties file.
	 */
	public static final String PATH_ANONYMIZATION_PROPERTIES = "./conf/anonymization.properties";

	protected AAnomyzationStrategy anonymizationStrategy;

	/**
	 * Create a new special config reader for the anonymization module.
	 * 
	 * @see de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader for
	 *      the functionality implementation
	 */
	public ConfigReader() {
		super( new File( PATH_ANONYMIZATION_PROPERTIES ) );
	}

	/**
	 * Get the configured anonymization strategy.<br />
	 * The value must be a valid existing class which extends the
	 * {@link AImportStrategy} abstract.
	 * 
	 * @return
	 * @throws InvalidConfigException
	 */
	public AAnomyzationStrategy getConfiguredAnonymizationStrategy()
			throws InvalidConfigException {
		if( null == this.anonymizationStrategy ) { // instanciate a new import
													// strategy
			// read config value
			String anonymizationStrategy = this
					.fetchValue( IConfigKeys.ANONY_STRATEGY_CLASS );
			Class< ? > className;

			try { // try to instanciate an instance from the property value
				className = Class.forName( anonymizationStrategy );

				Constructor< ? > constructor = null;
				Object strategyObject = null;
				// special behaviour for NerAnonymizationStrategy
				if( anonymizationStrategy
						.equals( "de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.ANerAnonymizationStrategy" ) ) {

					// read english corpus file as configured
					File englishCorpusFile = new File(
							super.fetchValue( super
									.fetchValue( IConfigKeys.ANONYM_NER_ENGLISH_PRIMARY ) ) );
					if( !englishCorpusFile.exists() ) {
						String errorMessage = "de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader.getConfiguredAnonymizationStrategy(): the configured corpus file for the English language doesn't exist. Please make sure, that the config key refers to the correct config key. \n";
						String presentationMessage = "An internal error occured while trying to read the configuration in the anonymization module. Please read the error log.";
						throw new InvalidConfigException( errorMessage,
								presentationMessage, null );
					}

					// read German corpora files
					List< String > germanCorpora = super
							.fetchMultipleValues( IConfigKeys.ANONYM_NER_GERMAN_CASCADE );
					List< File > germanCorporaFiles;
					if( 1 == germanCorpora.size()
							&& "all" == germanCorpora.get( 0 ).toLowerCase()
									.trim() ) {
						germanCorporaFiles = this.buildFileList();
					}
					else {
						germanCorporaFiles = this.buildFileList(germanCorpora);
					}


					// instanciate the NER constructor using reflection.
					// decorate the anonymization strategy by language specific
					// strategies.
					strategyObject = new GermanNerAnonymizationStrategy(
					// new EnglishNerAnonymizationStrategy(
					// englishCorpusFile ),
							germanCorporaFiles );
				} else {
					constructor = className.getConstructor();
					strategyObject = constructor.newInstance();
				}

				if( !( strategyObject instanceof AAnomyzationStrategy ) ) {
					throw new IllegalArgumentException(
							"The configured class doesn't extend AImportStrategy." );
				}
				this.anonymizationStrategy = ( AAnomyzationStrategy ) strategyObject;
			} catch( ClassNotFoundException | NoSuchMethodException
					| SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | GeneralLoggingException e ) {
				// raise invalid config exception
				String errorMessage = "de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader.getConfiguredAnonymizationStrategy(): the instanciation of the given configured class (given value: "
						+ anonymizationStrategy
						+ ") was not successfull. Either it doesn't exist or doesn't extend AAnonymizationStrategy. Full exception: \n"
						+ e;
				String presentationMessage = "An internal error occured while trying to read the configuration in the anonymization module. Please read the error log.";
				throw new InvalidConfigException( errorMessage,
						presentationMessage, e );
			}
		} // be lazy if the import strategy was already instanciated

		return this.anonymizationStrategy;
	}
	
	/**
	 * Get a new ner anonymization strategy instance.
	 * @param languageKey the config key of the concrete class (e.g. GermanNerAnonymizationStrategy) to be used.
	 * @param corpusKey the config key to the corpus used in the new strategy.
	 * @return the instanciated anonymization strategy.
	 * @throws InvalidConfigException 
	 */
	public AAnomyzationStrategy getNerAnonymizationStrategy(String languageKey, String corpusKey) throws InvalidConfigException {
		String corpusProperty = super.fetchValue( corpusKey );
		AAnomyzationStrategy strategy = super.getConfiguredClass( corpusProperty );
		
		return strategy;
	}

	/**
	 * Build a list of corpora Files by getting all properties with a configuration key '.corpus.'. 
	 * @return
	 * @throws InvalidConfigException
	 */
	private List< File > buildFileList() throws InvalidConfigException {
		List< File > corporaFiles = new ArrayList< File >();
		
		for( String propertyKey : super
				.fetchMultipleValues( IConfigKeys.ANONYM_NER_GERMAN_CORPORA ) ) {
			File corpusFile = new File( super.fetchValue( propertyKey ) );
			if( !corpusFile.exists() ) {
				String errorMessage = "de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader.buildFileList(): the configured corpus file (value: "
						+ corpusFile.getAbsolutePath()
						+ "  doesn't exist. Please make sure, that the config key refers to the correct config key. \n";
				String presentationMessage = "An internal error occured while trying to read the configuration in the anonymization module. Please read the error log.";
				throw new InvalidConfigException( errorMessage,
						presentationMessage, null );
			}
			corporaFiles.add( corpusFile );
		}
		
		return corporaFiles;
	}
	
	/**
	 * Build a list of corpora Files by getting all properties with a configuration key given in the list.
	 * @param germanCorpora the list of config keys pointing to corpus files.
	 * @return
	 * @throws InvalidConfigException
	 */
	private List< File > buildFileList( List< String > germanCorpora ) throws InvalidConfigException {
		List< File > corporaFiles = new ArrayList< File >();
		
		for( String propertyKey : germanCorpora ) {
			File corpusFile = new File( super.fetchValue( propertyKey ) );
			if( !corpusFile.exists() ) {
				String errorMessage = "de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader.buildFileList(): the configured corpus file (value: "
						+ corpusFile.getAbsolutePath()
						+ "  doesn't exist. Please make sure, that the config key refers to the correct config key. \n";
				String presentationMessage = "An internal error occured while trying to read the configuration in the anonymization module. Please read the error log.";
				throw new InvalidConfigException( errorMessage,
						presentationMessage, null );
			}
			corporaFiles.add( corpusFile );
		}
		
		return corporaFiles;
	}

}
