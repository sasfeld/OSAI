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
	public static String buildString( final String[] strArray ) {
		final StringBuilder builder = new StringBuilder();

		int lineNum = 1;
		for( final String line : strArray ) {
			if( null != line ) {
				builder.append( line
						+ ( ( strArray.length == lineNum ) ? "" : "\n" ) );

				lineNum++;
			}
		}

		return builder.toString();
	}
	
	/**
	 * Fill missing whitespaces after characters: ,;?!.:
	 * @param str
	 * @return
	 */
	public static String fillMissingWhitespaces( final String str ) {
	    String cleanedText = str;  

        // insert whitespaces after ".": negative lookahead regex: all "."
        // followed by no whitespace will be replaced by ".[whitespace]". to
        // avoid a whitespace at the end of the string, the whitespace must
        // be followed by alphanumeric characters
        cleanedText = cleanedText.replaceAll(
                "(?<!(http://)(www)?[a-zA-Z.]*)\\.(?!\\s)(?=[a-zA-Z0-9])",
                ". " );
        // insert whitespaces after ",": negative lookahead regex: all ","
        // followed by no whitespace will be replaced by ",[whitespace]"
        cleanedText = cleanedText.replaceAll( "\\,(?!\\s)(?=[a-zA-Z0-9])",
                ", " );
        // insert whitespaces after "!": negative lookahead regex: all ","
        // followed by no whitespace will be replaced by ",[whitespace]"
        cleanedText = cleanedText.replaceAll( "\\!(?!\\s)(?=[a-zA-Z0-9])",
                ", " );
        // insert whitespaces after "?": negative lookahead regex: all ","
        // followed by no whitespace will be replaced by ",[whitespace]"
        cleanedText = cleanedText.replaceAll( "\\?(?!\\s)(?=[a-zA-Z0-9])",
                ", " );
        // insert whitespaces after ";": negative lookahead regex: all ","
        // followed by no whitespace will be replaced by ",[whitespace]"
        cleanedText = cleanedText.replaceAll( "\\;(?!\\s)(?=[a-zA-Z0-9])",
                ", " );
        // insert whitespaces after ":": negative lookahead regex: all ","
        // followed by no whitespace will be replaced by ",[whitespace]"
        cleanedText = cleanedText.replaceAll( "\\:(?!\\s)(?=[a-zA-Z0-9])",
                ", " );
        
        return cleanedText;
	}

}
