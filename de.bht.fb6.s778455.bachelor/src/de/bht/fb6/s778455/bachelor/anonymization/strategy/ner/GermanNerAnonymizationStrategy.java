/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * <p>This class is a concrete implementation of the Stanford NER anonymization strategy for the German language.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public class GermanNerAnonymizationStrategy extends ANerAnonymizationStrategy {
	/**
	 * Define the replacement sequence for person entities.
	 */
	private static final String NE_PERSON_REPLACEMENT = "<REMOVED_PERSON_ENTITY>";
	protected AbstractSequenceClassifier< CoreLabel > classifier;
		
	public GermanNerAnonymizationStrategy(
			AAnomyzationStrategy decoratedStrategy, File corpusFile ) {
		super( decoratedStrategy, corpusFile );
		
		this.initializeClassifier();
	}
	
	public GermanNerAnonymizationStrategy(File corpusFile) {
		super( corpusFile );
		
		this.initializeClassifier();
	}
	
	private void initializeClassifier() {
		this.classifier = CRFClassifier.getClassifierNoExceptions( super.textCorpus.getAbsolutePath() );
		
	}	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) {
		String taggedText = this.classifier.classifyWithInlineXML( inputText );
		
		String anonymizedText = this.removeTaggedPersons(taggedText);
		
		return super.anonymizeText( anonymizedText );
	}

	private String removeTaggedPersons( String taggedText ) {
		String cleanedText = taggedText; 
		
		cleanedText = cleanedText.replaceAll( "<I-PER>.*?</I-PER>" , NE_PERSON_REPLACEMENT );
		
		
		return cleanedText;
	}
}
