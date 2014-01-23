/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>
 * This class contains functionality to read the configuration for the importer
 * module from a properties file.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class ConfigReader extends APropertiesConfigReader implements
		IConfigReader {
	/**
	 * Path to the anonymization.properties file.
	 */
	public static final String PATH_IMPORT_PROPERTIES = "./conf/importer.properties";
	/**
	 * Lazy value.
	 */
	protected AImportStrategy importStrategy;
	
	/**
	 * Create a new special config reader for the importer module.
	 * 
	 * @see de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader for
	 *      the functionality implementation
	 */
	public ConfigReader() {
		super( new File( PATH_IMPORT_PROPERTIES ) );
	}

	/**
	 * Get the configured import strategy.<br />
	 * The value must be a valid existing class which extends the
	 * {@link AImportStrategy} abstract.
	 * 
	 * @return
	 * @throws InvalidConfigException
	 */
	public AImportStrategy getConfiguredImportStrategy()
			throws InvalidConfigException {
		if (null == this.importStrategy) { // instanciate a new import strategy
			// read config value
			String importerStrategy = this
					.fetchValue( IConfigKeys.IMPORT_STRATEGY_CLASS );
			Class< ? > className;
			
			try { // try to instanciate an instance from the property value
				className = Class.forName( importerStrategy );
				Constructor< ? > constructor = className.getConstructor();
				Object strategyObject = constructor.newInstance();
				if ( ! (strategyObject instanceof AImportStrategy)) {
					throw new IllegalArgumentException( "The configured class doesn't extend AImportStrategy." );
				}
				this.importStrategy =  (AImportStrategy) strategyObject;
			} catch( ClassNotFoundException | NoSuchMethodException
					| SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e ) {
				// raise invalid config exception
				String errorMessage = "de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader.getImportStrategy(): the instanciation of the given configured class (given value: "
						+ importerStrategy
						+ ") was not successfull. Either it doesn't exist or doesn't extend AImportStrategy. Full exception: \n"
						+ e;
				String presentationMessage = "An internal error occured while trying to read the configuration in the importer module. Please read the error log.";
				throw new InvalidConfigException( errorMessage,
						presentationMessage, e );
			}
		} // be lazy if the import strategy was already instanciated
		
		return this.importStrategy;
	}

}
