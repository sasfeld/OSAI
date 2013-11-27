/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.List;

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
	public String anonymizeText( String inputText ) {
		String removedGreetings = inputText;
		removedGreetings = removedGreetings.replaceAll( "(?<=Gru� )[A-Za-z]{2}", PERSONAL_GREETING_REPLACEMENT );		
		removedGreetings = removedGreetings.replaceAll( "(?<=Viel Erfolg )[A-Za-z]{2}", PERSONAL_GREETING_REPLACEMENT );		
		removedGreetings = removedGreetings.replaceAll( "^[A-Za-z]{2}$", PERSONAL_GREETING_REPLACEMENT );		
		removedGreetings = removedGreetings.replaceAll( "(?<=[!\\.\\?]{1} )[A-Za-z]{2}$", PERSONAL_GREETING_REPLACEMENT );		
		
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
