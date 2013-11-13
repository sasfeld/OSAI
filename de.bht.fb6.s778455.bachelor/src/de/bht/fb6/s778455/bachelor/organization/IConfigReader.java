/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

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
	 * Fetch the value for a given property.
	 * @param property
	 * @return the value as String.
	 * @throws IllegalArgumentException if the property doesn't exist in the configuration target.
	 */
	String fetchValue(String property);
	
	/**
	 * Fetch all config values from the target system.
	 * @return a map with key - value - pairs. The key is the original config value from the target system.
	 */
	Map<String, String> fetchValues();
}
