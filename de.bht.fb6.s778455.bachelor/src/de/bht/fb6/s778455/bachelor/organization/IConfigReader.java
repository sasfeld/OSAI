/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import java.util.List;
import java.util.Map;

/**
 * <p>An IConfigReader offers functionalities to access values given in some kind
 * of configuration target (for example: a file).
 * .</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 *
 */
public interface IConfigReader {

	/**
	 * Fetch the single value for a given property key.
	 * @param propertyKey
	 * @return the value as String.
	 * @throws IllegalArgumentException if the property doesn't exist in the configuration target.
	 */
	String fetchValue(String propertyKey);
	
	/**
	 * Fetch all config values from the target system.
	 * @return a map with key - value - pairs. The key is the original config value from the target system.
	 */
	Map<String, String> fetchValues();
	
	/**
	 * <p>Fetch multiple values for a given property key.<br />
	 * Multiple values are given when the config value points to a list of values.</p>
	 * @param propertyKey
	 * @return a list of values. The value list will contain at least 1 element.
	 * @throws InvalidConfigException 
	 */
	List< String > fetchMultipleValues(String propertyKey) throws InvalidConfigException;
}
