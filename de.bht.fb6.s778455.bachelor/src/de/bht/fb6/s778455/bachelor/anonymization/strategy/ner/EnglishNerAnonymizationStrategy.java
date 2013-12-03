/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class is a concrete implementation of the Stanford NER for the English language.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public class EnglishNerAnonymizationStrategy extends ANerAnonymizationStrategy {

	public EnglishNerAnonymizationStrategy(
			AAnomyzationStrategy decoratedStrategy, File corpusFile ) throws GeneralLoggingException {
		super( decoratedStrategy, corpusFile );
	}
	
	public EnglishNerAnonymizationStrategy(File corpusFile) throws GeneralLoggingException {
		super( corpusFile );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String, de.bht.fb6.s778455.bachelor.model.Board)
	 */
	public String anonymizeText( String inputText, Board belongingBoard )
			throws GeneralLoggingException {
		// we don't need the board instance here
		return this.anonymizeText( inputText ); 
	}


}
