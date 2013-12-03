/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.List;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Board.LearnedWordTypes;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * The LearnedNamesStrategy finds well known words that represent Person
 * entities.
 * </p>
 * 
 * <p>
 * Those words are specific for a {@link Board}. Following the anonymization
 * strategy chain, this strategy expects the previous strategies to have added
 * "detected" person entities.
 * </p>
 * 
 * <p>
 * The strategy will simply replace those "learned words".
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.12.2013
 * 
 */
public class LearnedNamesStrategy extends AAnomyzationStrategy {


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #anonymizeText(java.lang.String, de.bht.fb6.s778455.bachelor.model.Board)
	 */
	@Override
	public String anonymizeText( String inputText, Board belongingBoard )
			throws GeneralLoggingException {
		String anonymizedText = inputText;
		
		Set< String > personNames = belongingBoard.getLearnedWords( LearnedWordTypes.PERSON_NAME );
		if ( null != personNames) {
			for( String personName : personNames ) {
				anonymizedText = anonymizedText.replaceAll( personName, LEARNED_PERSON_NAME_REPLACEMENT );
				anonymizedText = anonymizedText.replaceAll( personName.toLowerCase(), LEARNED_PERSON_NAME_REPLACEMENT );
				anonymizedText = anonymizedText.replaceAll( personName.toUpperCase(), LEARNED_PERSON_NAME_REPLACEMENT );
				
				// upper first character of personName
				String upperedPersonName = Character.toString( personName.charAt( 0 ) ).toUpperCase() + personName.substring( 1 ) ;
				anonymizedText = anonymizedText.replaceAll( upperedPersonName, LEARNED_PERSON_NAME_REPLACEMENT );
				
			}
		}
		
		return anonymizedText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #anonymizeText(java.lang.String)
	 */
	@Override
	public String anonymizeText( String inputText )
			throws GeneralLoggingException {
		throw new GeneralLoggingException(
				getClass()
						+ ":anonymizeText() doesn't support the anonymization without a given board.",
				"An internal error raised by the LearnedNamesStrategy occured. Please read the logs!" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #removeSpecialTags(java.lang.String)
	 */
	@Override
	public String removeSpecialTags( String inputText ) {
		return inputText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #getWrappedStrategies()
	 */
	@Override
	public List< AAnomyzationStrategy > getWrappedStrategies() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #getDetails()
	 */
	@Override
	public String getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
