/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization.anonymization;

import java.util.Map;

import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class ....</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 *
 */
public class ConfigReader implements IConfigReader {

	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValue(java.lang.String)
	 */
	public String fetchValue( String property ) {
		// TODO implement 'property' functionality.
		if(property.equals( IConfigKeys.ANONYM_NER_GERMAN_DEWAC_FILE )) {
			return "./conf/ner/stanford-ner-2012-05-22-german/dewac_175m_600.crf.ser.gz";
		}
		throw new IllegalArgumentException("ConfigReader::fetchValue(): No value found for the given property '"+property+"'.");
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValues()
	 */
	public Map< String, String > fetchValues() {
		// TODO Auto-generated method stub
		return null;
	}

}
