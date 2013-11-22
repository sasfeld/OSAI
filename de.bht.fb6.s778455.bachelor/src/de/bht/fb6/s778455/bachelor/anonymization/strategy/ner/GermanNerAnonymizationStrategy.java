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
	/**
	 * Define the replacement sequence for person entities.
	 */
	private static final String NE_PERSON_REPLACEMENT = "<REMOVED_PERSON_ENTITY>";
		
	/**
	 * Create a new decorated GermanNerAnonymizationStrategy which is reponsible for anonymizing texts containing the German language.
	 * @param decoratedStrategy another {@link AAnomyzationStrategy} which will be called by this strategy instance for further tasks
	 * @param corpusFile
	 */
	public GermanNerAnonymizationStrategy(
			AAnomyzationStrategy decoratedStrategy, File corpusFile ) {
		super( decoratedStrategy, corpusFile );
	}
	
	/**
	 * Create a new undecorated GermanNerAnonymizationStrategy which is responsible for the anonymization of German texts.
	 * @param corpusFile
	 */
	public GermanNerAnonymizationStrategy(File corpusFile) {
		super( corpusFile );
	}	
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) {
		String preAnonymizedText = super.anonymizeText( inputText );
		
		String taggedText = this.classifier.classifyWithInlineXML( preAnonymizedText );
		
		String anonymizedText = this.removeTaggedPersons(taggedText);
		
		return anonymizedText;
	}

	/**
	 * Remove (CRF)-tagged persons.
	 * @param taggedText
	 * @return a new {@link String}
	 */
	private String removeTaggedPersons( String taggedText ) {
		String cleanedText = taggedText; 
		
		cleanedText = cleanedText.replaceAll( "<I-PER>.*?</I-PER>" , NE_PERSON_REPLACEMENT );
		
		
		return cleanedText;
	}
}
