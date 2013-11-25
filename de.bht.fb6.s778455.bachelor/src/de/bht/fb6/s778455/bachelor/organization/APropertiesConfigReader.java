/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class is a generalization for reading from a properties file.
 * </p>
 * <p>
 * ConfigReader classes in each module can extend it to avoid code duplication.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public abstract class APropertiesConfigReader implements IConfigReader {
	protected Properties properties;
	protected Map< String, String > propertiesMap;

	/**
	 * Create a new config reader which reads from a '.property' - file. <br />
	 * A critical log entry will be executed if the given file doesn't exist or
	 * isn't accessible.
	 * 
	 * @param inputFile
	 */
	public APropertiesConfigReader( File inputFile ) {
		super();

		properties = new Properties();
		try {
			properties.load( new FileInputStream( inputFile ) );
		} catch( IOException e ) { // log critical message
			Application.log( e.getLocalizedMessage(), LogType.CRITICAL );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValue(java
	 * .lang.String)
	 */
	public String fetchValue( String property ) {
		String value = this.properties.getProperty( property.trim()
				.toLowerCase() );

		if( null == value ) {
			throw new IllegalArgumentException(
					"ConfigReader::fetchValue(): No value found for the given property '"
							+ property + "'." );
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValues()
	 */
	public Map< String, String > fetchValues() {
		if( null == this.propertiesMap ) { // be lazy
			this.propertiesMap = new HashMap< String, String >();

			// fill properties map
			for( Object key : properties.keySet() ) {
				Object property = properties.get( key );
				if( key instanceof String && property instanceof String ) {
					propertiesMap.put( ( String ) key, ( String ) property );
				} else {
					Application
							.log( "ConfigReader::fetchValues(): the properties keys or values are not strings.",
									LogType.ERROR );
				}
			}
		}

		return this.propertiesMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchMultipleValues
	 * (java.lang.String)
	 */
	public List< String > fetchMultipleValues( String propertyKey ) throws InvalidConfigException {
		List< String > returnValues = new ArrayList< String >();
		Map< String, String > propertiesMap = this.fetchValues();

		for( String key : propertiesMap.keySet() ) {
			// check if the propertyKey is the beginning of the one in the map and the one in the map is followed by other characters (such as ".anotherconfigkey")
			if( key.startsWith( propertyKey )
					&& key.substring( propertyKey.length(), key.length() )
							.length() > 0 ) {
				returnValues.add( propertiesMap.get( key ) );
			}
		}

		// if the list is empty the config key doesn't point to a list of values
		if( 0 == returnValues.size() ) {
			throw new InvalidConfigException(
					getClass() + ":fetchMultipleValues: the given config key ("
							+ propertyKey
							+ ") doesn't point to a list of values.",
					"Internal error: the configuration is damaged. Please see the logs.",
					null );
		}

		return returnValues;
	}

}