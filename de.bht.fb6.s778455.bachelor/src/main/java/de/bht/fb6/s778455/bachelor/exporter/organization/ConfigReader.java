/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.organization;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>This class realizes the config reader of the exporter module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 *
 */
public class ConfigReader extends APropertiesConfigReader {

	private static final String PATH_EXPORT_PROPERTIES = PATH_CONFIG + "exporter.properties";
	private AExportStrategy exportStrategy;

	/**
	 * Create a new special config reader.
	 * @param inputFile
	 */
	public ConfigReader(  ) {
		super( new File( PATH_EXPORT_PROPERTIES ) );
	}

	public AExportStrategy getConfiguredExportStrategy() throws InvalidConfigException {
		if (null == this.exportStrategy) { // instanciate a new import strategy
			// read config value
			String exporterStrategy = this
					.fetchValue( IConfigKeys.EXPORT_STRATEGY_CLASS );
			Class< ? > className;
			
			try { // try to instanciate an instance from the property value
				className = Class.forName( exporterStrategy );
				Constructor< ? > constructor = className.getConstructor();
				Object strategyObject = constructor.newInstance();
				if ( ! (strategyObject instanceof AExportStrategy)) {
					throw new IllegalArgumentException( "The configured class doesn't extend AExportStrategy." );
				}
				this.exportStrategy =  (AExportStrategy) strategyObject;
			} catch( ClassNotFoundException | NoSuchMethodException
					| SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e ) {
				// raise invalid config exception
				String errorMessage = "de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader.getExportStrategy(): the instanciation of the given configured class (given value: "
						+ exporterStrategy
						+ ") was not successfull. Either it doesn't exist or doesn't extend AExportStrategy. Full exception: \n"
						+ e;
				String presentationMessage = "An internal error occured while trying to read the configuration in the exporter module. Please read the error log.";
				throw new InvalidConfigException( errorMessage,
						presentationMessage, e );
			}
		} // be lazy if the import strategy was already instanciated
		
		return this.exportStrategy;
	}
	
}
