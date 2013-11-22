/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;


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
	protected AbstractSequenceClassifier< CoreLabel >  classifier;

	/**
	 * Create a new {@link AAnomyzationStrategy} which uses the Stanford NER library.
	 * @param decoratedStrategy the anonymization strategy which decorates this strategy.
	 */
	public ANerAnonymizationStrategy(AAnomyzationStrategy decoratedStrategy, File corpusFile) {
		this.decoratingStrategy = decoratedStrategy;
		this.textCorpus = corpusFile;
		
		this.initializeClassifier();
	}
	
	/**
	 * Create a new {@link AAnomyzationStrategy} which uses the Stanford NER library.
	 */
	public ANerAnonymizationStrategy(File corpusFile) {
		this.decoratingStrategy = null;
		this.textCorpus = corpusFile;
		
		this.initializeClassifier();
	}
	
	/**
	 * Initialize the Stanford CRF classifier.
	 */
	protected void initializeClassifier() {
		this.classifier = CRFClassifier.getClassifierNoExceptions( this.textCorpus.getAbsolutePath() );
		
	}	

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) {
		String preparedText = inputText;
		if ( null != this.decoratingStrategy) {
			preparedText = this.decoratingStrategy.anonymizeText( inputText );
		}
		else { // last strategy in chain
			preparedText = super.prepareText(preparedText);
			preparedText = super.filterPersonalData(preparedText);
		}
		
		return preparedText;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#removeSpecialTags(java.lang.String)
	 */
	public String removeSpecialTags( String inputText ) {
		String cleanedText = inputText;
		
		Set< String > tags = this.classifier.labels();
		
		for( String tag : tags ) {
			// replace all tag appearances
			cleanedText = cleanedText.replaceAll( "</?"+tag+">", "" );
		}
		
		return cleanedText;
	}
	
}
