/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.List;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course.LearnedWordTypes;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.organization.corpus.CommonNameExcluder;

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
	protected CommonNameExcluder commonNameExcluder;

	public LearnedNamesStrategy() throws InvalidConfigException {
		this.commonNameExcluder = CommonNameExcluder.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #anonymizeText(java.lang.String, de.bht.fb6.s778455.bachelor.model.Board)
	 */
	@Override
	public String anonymizeText( final String inputText, final Board belongingBoard )
			throws GeneralLoggingException {
		String anonymizedText = super.prepareText( inputText );

		final Set< String > personNames = belongingBoard.getBelongingCourse()
				.getLearnedWords( LearnedWordTypes.PERSON_NAME );
		if( null != personNames ) {
			for( final String personName : personNames ) {
				// not a common word
				if( !this.commonNameExcluder.isCommon( personName, false )) {
					final String prefixRegEx = "(?<=[\\s,.!?;]{1})";
					final String suffixRegEx = "(?=[\\s,.!?;]?)(?![a-zA-Z0-9]+)";

					anonymizedText = anonymizedText.replaceAll( prefixRegEx
							+ personName + suffixRegEx,
							LEARNED_PERSON_NAME_REPLACEMENT );
					anonymizedText = anonymizedText.replaceAll( prefixRegEx
							+ personName.toLowerCase() + suffixRegEx,
							LEARNED_PERSON_NAME_REPLACEMENT );
					anonymizedText = anonymizedText.replaceAll( prefixRegEx
							+ personName.toUpperCase() + suffixRegEx,
							LEARNED_PERSON_NAME_REPLACEMENT );

					// upper first character of personName
					final String upperedPersonName = Character.toString(
							personName.charAt( 0 ) ).toUpperCase()
							+ personName.substring( 1 );
					anonymizedText = anonymizedText.replaceAll( prefixRegEx
							+ upperedPersonName + suffixRegEx,
							LEARNED_PERSON_NAME_REPLACEMENT );
				}
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
	public String anonymizeText( final String inputText )
			throws GeneralLoggingException {
		throw new GeneralLoggingException(
				this.getClass()
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
	public String removeSpecialTags( final String inputText ) {
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
		return null;
	}

}
