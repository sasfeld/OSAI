/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;

/**
 * <p>This class is a concrete implementation of the Stanford NER anonymization strategy for the German language.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public class GermanNerAnonymizationStrategy extends ANerAnonymizationStrategy {

		
	public GermanNerAnonymizationStrategy(
			AAnomyzationStrategy decoratedStrategy, File corpusFile ) {
		super( decoratedStrategy, corpusFile );
	}
	
	public GermanNerAnonymizationStrategy(File corpusFile) {
		super( corpusFile );
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) {
		return super.anonymizeText( inputText );
	}
}
