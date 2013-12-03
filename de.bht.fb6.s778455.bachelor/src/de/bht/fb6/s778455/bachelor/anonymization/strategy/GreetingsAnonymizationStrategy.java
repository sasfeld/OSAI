/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.StringUtil;

/**
 * <p>This class realizes a strategy to remove greeting sequences in sentences, mostly by using regular expressions.</p>
 * 
 * <p>An example in German would be: "Gru�, Max Mustermann" or "Gru� MM"</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.11.2013
 *
 */
public class GreetingsAnonymizationStrategy extends AAnomyzationStrategy {
	
	public static final String PERSONAL_GREETING_REPLACEMENT = "<PERSONAL_GREETING_REMOVEMENT>";

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) throws GeneralLoggingException {
		String removedGreetings = super.prepareText( inputText );
		
		// replace acronyms which are following "Gru�"
		removedGreetings = this.removeGreetingFormula( removedGreetings, "Gru�");
		removedGreetings = this.removeGreetingFormula( removedGreetings, "Gr��le");
		removedGreetings = this.removeGreetingFormula( removedGreetings, "Viel Erfolg" );	
		
		// replace 2-digit acronyms in a single line (e.g. "XY")
		Pattern pSingleAcronym = Pattern.compile( "^[A-Za-z]{2}$" , Pattern.MULTILINE);
		removedGreetings = pSingleAcronym.matcher( removedGreetings ).replaceAll( PERSONAL_GREETING_REPLACEMENT );	
		
		Pattern pAcronymEndOfLine = Pattern.compile( "(?<=[!\\.\\?]+ )[A-Za-z]{2}$", Pattern.MULTILINE );
		removedGreetings = pAcronymEndOfLine.matcher( removedGreetings ).replaceAll( PERSONAL_GREETING_REPLACEMENT);		
		
		return removedGreetings;
	}

	/**
	 * Remove a special greeting formula, such as: "Gru�[,] XY", "Gr��le[,] XY", "Viel Erfolg[,] XY"
	 * @param textString the whole text {@link String} in which the greeting shall be removed
	 * @param greetingWord the greeting word which shall be looked up (e.g: "Gru�")
	 * @return
	 * @throws GeneralLoggingException 
	 */
	private String removeGreetingFormula( String textString, String greetingWord ) throws GeneralLoggingException {
		if (null == textString || null == greetingWord) {
			throw new IllegalArgumentException( getClass() + ":removeGreetingFormula() - null is not allowed as argument value" );
		}
		
		String removedGreetings = textString;	
		
		// read line-based to ensure that the greeting formula is only removed at the end of a posting
		String[] lines = removedGreetings.split( "\n" );
		String[] newLines = lines;
		for( int lineNumber = 0; lineNumber < lines.length; lineNumber++ ) {
			if ( lines.length - 1 == lineNumber ) { // greeting in the last line
				// replace acronyms which are following "[greetingWord] XY"
				Pattern pGreetingAcronym = Pattern.compile( "(?<="+greetingWord+"[,!\\.]?[\\s\\n]{1,5})[A-Za-z\\s-]+(?=[\\s\\n]*)", Pattern.MULTILINE );
				Matcher matcher = pGreetingAcronym.matcher( lines[lineNumber] );
				newLines[ lineNumber ] = matcher.replaceAll( PERSONAL_GREETING_REPLACEMENT )	;	
				
				// remove lower-cased
				pGreetingAcronym = Pattern.compile( "(?<="+greetingWord.trim().toLowerCase()+"[,!\\.]? )[A-Za-z]{2}(?![A-Za-z0-9])", Pattern.MULTILINE );
				matcher = pGreetingAcronym.matcher( lines[lineNumber] );
				newLines[ lineNumber ] = matcher.replaceAll( PERSONAL_GREETING_REPLACEMENT )	;	
			}
		}		
		
		
		return StringUtil.buildString( newLines );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#removeSpecialTags(java.lang.String)
	 */
	public String removeSpecialTags( String inputText ) {
		return inputText; // no special tags to remove here
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#getWrappedStrategies()
	 */
	public List< AAnomyzationStrategy > getWrappedStrategies() {
		return null;
	}

	@Override
	public String getDetails() {
		return this.toString();
	}

}
