/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

/**
 * 
 * <p>This class describes the API for an anonymization strategy.</p>
 * <p>An anomization strategy offers an implementation to anonymize text.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public abstract class AAnomyzationStrategy {
	/**
	 * Replacement String for personal data (such as eMail-addresses and so on).
	 */
	public static final String PERSONAL_DATA_REPLACEMENT = "<REMOVED_PERSONAL_DATA>";
	
	/**
	 * Anonymize a given {@link String}.
	 * Anonymization means removing all named entities recording to a person.
	 * 
	 * @param inputText
	 * @return a new {@link String} with anonymized data
	 */
	public abstract String anonymizeText(String inputText);
	
	/**
	 * Prepare the given text for the anonymization.
	 * @param preparedText
	 * @return a new {@link String}
	 */
	protected String prepareText( String preparedText ) {
		String cleanedText = preparedText;
		
		// insert whitespaces after ".": negative lookahead regex: all "." followed by no whitespace will be replaced by ".[whitespace]"
		cleanedText = cleanedText.replaceAll( "\\.(?!\\s)", ". " );
		// insert whitespaces after ",": negative lookahead regex: all "," followed by no whitespace will be replaced by ",[whitespace]"
		cleanedText = cleanedText.replaceAll( "\\,(?!\\s)", ", " );
		
		return cleanedText;
	}

	/**
	 * Filter personal data, such as eMail adresses and so on.
	 * @param preparedText
	 * @return a new {@link String}
	 */
	protected String filterPersonalData( String preparedText ) {
		String cleanedText = preparedText;
		
		// replace eMail-addresses, follows example at http://www.brain4.de/programmierecke/js/tools/regex.php
		cleanedText = cleanedText.replaceAll( "[a-zA-Z0-9][\\w\\.-]*@(?:[a-zA-Z0-9][a-zA-Z0-9_-]+\\.)+[A-Z,a-z]{2,5}", PERSONAL_DATA_REPLACEMENT );		
		
		return cleanedText;
	}
}
