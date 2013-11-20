/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.organization;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>This class contains functionality to read the configuration for the anonymization module from a properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 *
 */
public class ConfigReader extends APropertiesConfigReader implements IConfigReader {
	/**
	 * Path to the anonymization.properties file.
	 */
	public static final String PATH_ANONYMIZATION_PROPERTIES = "./conf/anonymization.properties";
	
	protected AAnomyzationStrategy anonymizationStrategy;
	
	/**
	 * Create a new special config reader for the anonymization module.
	 * @see de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader for the functionality implementation
	 */
	public ConfigReader() {
		super( new File ( PATH_ANONYMIZATION_PROPERTIES ) );
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
		if (null == this.anonymizationStrategy) { // instanciate a new import strategy
			// read config value
			String anonymizationStrategy = this
					.fetchValue( IConfigKeys.ANONY_STRATEGY_CLASS );
			Class< ? > className;
			
			try { // try to instanciate an instance from the property value
				className = Class.forName( anonymizationStrategy );
				Constructor< ? > constructor = className.getConstructor();
				Object strategyObject = constructor.newInstance();
				if ( ! (strategyObject instanceof AAnomyzationStrategy)) {
					throw new IllegalArgumentException( "The configured class doesn't extend AImportStrategy." );
				}
				this.anonymizationStrategy =  (AAnomyzationStrategy) strategyObject;
			} catch( ClassNotFoundException | NoSuchMethodException
					| SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e ) {
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

}
