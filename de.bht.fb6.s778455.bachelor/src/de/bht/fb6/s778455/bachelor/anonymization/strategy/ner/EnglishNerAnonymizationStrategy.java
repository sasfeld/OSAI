/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
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


}
