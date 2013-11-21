/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;


/**
 * 
 * <p>This class is an implementation of an {@link AAnomyzationStrategy} using the Stanford NER library.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public abstract class ANerAnonymizationStrategy extends AAnomyzationStrategy {
	
	private AAnomyzationStrategy decoratingStrategy;
	protected File textCorpus;

	/**
	 * Create a new {@link AAnomyzationStrategy} which uses the Stanford NER library.
	 * @param decoratedStrategy the anonymization strategy which decorates this strategy.
	 */
	public ANerAnonymizationStrategy(AAnomyzationStrategy decoratedStrategy, File corpusFile) {
		this.decoratingStrategy = decoratedStrategy;
		this.textCorpus = corpusFile;
	}
	
	/**
	 * Create a new {@link AAnomyzationStrategy} which uses the Stanford NER library.
	 */
	public ANerAnonymizationStrategy(File corpusFile) {
		this.decoratingStrategy = null;
		this.textCorpus = corpusFile;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) {
		String text = inputText;
		if ( null != this.decoratingStrategy) {
			text = this.decoratingStrategy.anonymizeText( inputText );
		}
		
		return text;
	}
}
