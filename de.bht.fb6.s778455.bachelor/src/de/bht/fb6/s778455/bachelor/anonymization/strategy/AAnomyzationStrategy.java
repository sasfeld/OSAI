/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>
 * This class describes the API for an anonymization strategy.
 * </p>
 * <p>
 * An anomization strategy offers an implementation to anonymize text.
 * </p>
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
	 * Replacement String for learned names (such names that previous strategies
	 * detected to be names).
	 */
	public static final String LEARNED_PERSON_NAME_REPLACEMENT = "<LEARNED_PERSON_NAME_REPLACEMENT>";
	/**
	 * Specify the replacement tag for replaced entities here.
	 */
	public static final String PERSONAL_GREETING_REPLACEMENT = "<PERSONAL_GREETING_REMOVEMENT>";
	/**
	 * Specify the replacement tag for replaced entries from a name corpus.
	 */
	public static final String NAME_CORPUS_REPLACEMENT = "<NAME_CORPUS_REPLACEMENT>";

	/**
	 * Anonymize a given {@link String}. Anonymization means removing all named
	 * entities recording to a person.
	 * 
	 * @param inputText
	 * @param belongingBoard
	 *            the {@link Board} on which this inputText is based
	 * @return a new {@link String} with anonymized data
	 * @throws GeneralLoggingException
	 */
	public abstract String anonymizeText( String inputText, Board belongingBoard )
			throws GeneralLoggingException;

	/**
	 * Anonymize a given {@link String}. Anonymization means removing all named
	 * entities recording to a person.
	 * 
	 * @param inputText
	 * @return a new {@link String} with anonymized data
	 * @throws GeneralLoggingException
	 */
	public abstract String anonymizeText( String inputText )
			throws GeneralLoggingException;

	/**
	 * Remove special tags that the implementation added to the given inputText.
	 * For example, natural language processing strategies use NER tags. Use
	 * this method to get an untagged text.
	 * 
	 * @param inputText
	 * @return a new {@link String} with untagged data
	 */
	public abstract String removeSpecialTags( String inputText );

	/**
	 * Prepare the given text for the anonymization.
	 * 
	 * @param preparedText
	 * @return a new {@link String}
	 * @throws GeneralLoggingException
	 */
	protected String prepareText( String preparedText )
			throws GeneralLoggingException {
		if( null != preparedText ) {
			String cleanedText = preparedText;

			// remove moodle tags
			cleanedText = this.removeMoodleChars( cleanedText );

			// remove empty lines
			cleanedText = cleanedText.replaceAll( "(?m)^[ \t]*\r?\n", "" );

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

			return cleanedText;
		}
		throw new GeneralLoggingException( getClass()
				+ "prepareText: null pointer",
				"Internal error in the anonymization system!" );
	}

	private String removeMoodleChars( String cleanedText ) {
		String newCleanedText = cleanedText;
		
//		newCleanedText = newCleanedText.replaceAll( "</?[a-z]+(\\s=\".*?\")*[\\s]*/?>", " " );
		Pattern pTag = Pattern.compile( "</?.*?/?>" );
		Matcher mTag = pTag.matcher( newCleanedText );
		List< String > matchedSequences = new ArrayList<String>();
		
		while ( mTag.find() ) {
			String matchedSequence = mTag.group();
			if ( !matchedSequence.contains( LEARNED_PERSON_NAME_REPLACEMENT )
				&& !matchedSequence.contains( NAME_CORPUS_REPLACEMENT )
				&& !matchedSequence.contains( PERSONAL_DATA_REPLACEMENT )
				&& !matchedSequence.contains( PERSONAL_GREETING_REPLACEMENT )) {
				matchedSequences.add( matchedSequence );
			}
		}
		for( String matchedSequence : matchedSequences ) {
			try {
			newCleanedText = newCleanedText.replaceAll( matchedSequence, "" );
			} catch ( Exception e) { // TODO be less defensive
				// TODO handle exception
			}
		}
// 		newCleanedText = newCleanedText.replaceAll( "</?.*?/?>", " " );
		newCleanedText = newCleanedText.replaceAll( "(\\\\r\\\\n|\\\\n|\\\\t)", " " );
//		newCleanedText = newCleanedText.replaceAll( "" + '\n', " " );
//		newCleanedText = newCleanedText.replaceAll( "" + '\r', " " );
//		newCleanedText = newCleanedText.replaceAll( "" + '\t', " " );
		
		return newCleanedText;
	}

	/**
	 * Filter personal data, such as eMail adresses and so on.
	 * 
	 * @param preparedText
	 * @return a new {@link String}
	 */
	protected String filterPersonalData( String preparedText ) {
		String cleanedText = preparedText;

		// replace eMail-addresses, follows example at
		// http://www.brain4.de/programmierecke/js/tools/regex.php
		cleanedText = cleanedText
				.replaceAll(
						"[a-zA-Z0-9][\\w\\.-]*@(?:[a-zA-Z0-9][a-zA-Z0-9_-]+\\.)+[A-Z,a-z]{2,5}",
						PERSONAL_DATA_REPLACEMENT );

		return cleanedText;
	}

	/**
	 * Get a list of {@link Class} concerning the wrapped (means including
	 * decorators) strategies and the strategy itself.
	 * 
	 * @return
	 */
	public abstract List< AAnomyzationStrategy > getWrappedStrategies();

	/**
	 * Get a (informal) String containing details. This is meant to offer
	 * information for analysis and logging.
	 * 
	 * @return
	 */
	public abstract String getDetails();
}
