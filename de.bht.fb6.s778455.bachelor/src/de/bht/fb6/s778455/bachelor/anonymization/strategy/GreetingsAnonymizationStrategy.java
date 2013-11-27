/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>This class realizes a strategy to remove greeting sequences in sentences, mostly by using regular expressions.</p>
 * 
 * <p>An example in German would be: "Gruﬂ, Max Mustermann" or "Gruﬂ MM"</p>
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
	public String anonymizeText( String inputText ) {
		String removedGreetings = super.prepareText( inputText );
		
		// replace acronyms which are following "Gruﬂ"
		Pattern pGreetingAcronym = Pattern.compile( "(?<=Gruﬂ[,!\\.]? )[A-Za-z]{2}(?![A-Za-z0-9])", Pattern.MULTILINE );
		removedGreetings = pGreetingAcronym.matcher( removedGreetings ).replaceAll( PERSONAL_GREETING_REPLACEMENT )	;	

		// replace acronyms which are following "Viel Erfolg"
		Pattern pGreetingSuccessAcronym = Pattern.compile( "(?<=Viel Erfolg[,!\\.]? )[A-Za-z]{2}(?![A-Za-z0-9])", Pattern.MULTILINE );
		removedGreetings = pGreetingSuccessAcronym.matcher( removedGreetings ).replaceAll( PERSONAL_GREETING_REPLACEMENT );		
		
		// replace 2-digit acronyms in a single line (e.g. "XY")
		Pattern pSingleAcronym = Pattern.compile( "^[A-Za-z]{2}$" , Pattern.MULTILINE);
		removedGreetings = pSingleAcronym.matcher( removedGreetings ).replaceAll( PERSONAL_GREETING_REPLACEMENT );	
		
		Pattern pAcronymEndOfLine = Pattern.compile( "(?<=[!\\.\\?]+ )[A-Za-z]{2}$", Pattern.MULTILINE );
		removedGreetings = pAcronymEndOfLine.matcher( removedGreetings ).replaceAll( PERSONAL_GREETING_REPLACEMENT);		
		
		return removedGreetings;
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
