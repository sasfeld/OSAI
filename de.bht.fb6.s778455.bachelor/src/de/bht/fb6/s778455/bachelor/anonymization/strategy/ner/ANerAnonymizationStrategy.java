/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;

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
	/**
	 * Replacement String for personal data (such as eMail-addresses and so on).
	 */
	public static final String PERSONAL_DATA_REPLACEMENT = "<REMOVED_PERSONAL_DATA>";
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
			preparedText = this.prepareText(preparedText);
			preparedText = this.filterPersonalData(preparedText);
		}
		
		return preparedText;
	}

	/**
	 * Prepare the given text for the anonymization.
	 * @param preparedText
	 * @return a new {@link String}
	 */
	private String prepareText( String preparedText ) {
		String cleanedText = preparedText;
		
		// insert whitespaces after ".": negative lookahead regex: all "." followed by no whitespace will be replaced by ".[whitespace]"
		cleanedText = cleanedText.replaceAll( "\\.(?!\\s)", ". " );
		// insert whitespaces after ",": negative lookahead regex: all "," followed by no whitespace will be replaced by ",[whitespace]"
		cleanedText = cleanedText.replaceAll( "\\,(?!\\s)", ", " );
		
		return cleanedText;
	}

	/**
	 * Filter personal data, such as eMail adresses and so on.
	 * @param preparedText
	 * @return a new {@link String}
	 */
	private String filterPersonalData( String preparedText ) {
		String cleanedText = preparedText;
		
		// replace eMail-addresses, follows example at http://www.brain4.de/programmierecke/js/tools/regex.php
		cleanedText = cleanedText.replaceAll( "[a-zA-Z0-9][\\w\\.-]*@(?:[a-zA-Z0-9][a-zA-Z0-9_-]+\\.)+[A-Z,a-z]{2,5}", PERSONAL_DATA_REPLACEMENT );		
		
		return cleanedText;
	}
}
