/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.organization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class contains functionality to read the configuration for the anonymization module from a properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 *
 */
public class ConfigReader implements IConfigReader {
	/**
	 * Path to the anonymization.properties file.
	 */
	public static final String PATH_ANONYMIZATION_PROPERTIES = "./conf/anonymization.properties";
	protected Properties properties;
	protected Map<String, String> propertiesMap;
	
	public ConfigReader() {
		properties = new Properties();
		try {
			properties.load( new FileInputStream( new File( PATH_ANONYMIZATION_PROPERTIES ) ) );
		} catch( IOException e ) { // log critical message
			Application.log( e.getLocalizedMessage(), LogType.CRITICAL );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValue(java.lang.String)
	 */
	public String fetchValue( String property ) {
		String value = this.properties.getProperty( property );
		
		if (null == value) {		
			throw new IllegalArgumentException("ConfigReader::fetchValue(): No value found for the given property '"+property+"'.");
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValues()
	 */
	public Map< String, String > fetchValues() {
		if (null == this.propertiesMap) { // be lazy
			this.propertiesMap = new HashMap<String, String>();
			
			// fill properties map
			for( Object key : properties.keySet() ) {
				Object property = properties.get( key );
				if (key instanceof String && property instanceof String) {
					propertiesMap.put( (String) key, (String) property );
				}
				else {
					Application.log( "ConfigReader::fetchValues(): the properties keys or values are not strings.", LogType.ERROR );
				}
			}
		}
		
		return this.propertiesMap;
	}

}
