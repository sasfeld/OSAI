/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

/**
 * <p>
 * This class offers static methods to work with Strings.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.12.2013
 * 
 */
public class StringUtil {

	/**
	 * Build a string by an array of {@link String}.<br />
	 * The resulting string will contain new line characters (\n).
	 * 
	 * @param strArray
	 * @return the builded string
	 */
	public static String buildString( String[] strArray ) {
		StringBuilder builder = new StringBuilder();

		int lineNum = 1;
		for( String line : strArray ) {
			if( null != line ) {
				builder.append( line
						+ ( ( strArray.length == lineNum ) ? "" : "\n" ) );

				lineNum++;
			}
		}

		return builder.toString();
	}

}
