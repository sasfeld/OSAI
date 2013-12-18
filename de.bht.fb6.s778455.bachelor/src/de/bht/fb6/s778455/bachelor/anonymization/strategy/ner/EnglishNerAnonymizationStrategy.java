/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * <p>This class is a concrete implementation of the Stanford NER for the English language.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public class EnglishNerAnonymizationStrategy extends ANerAnonymizationStrategy {

	/**
	 * Create a new decorating strategy.
	 * @param decoratedStrategy
	 * @param corpusFile
	 * @throws GeneralLoggingException
	 */
	public EnglishNerAnonymizationStrategy(
			AAnomyzationStrategy decoratedStrategy, File corpusFile ) throws GeneralLoggingException {
		super( decoratedStrategy, corpusFile );
	}
	
	/**
	 * Create a simple - undecorated - strategy. 
	 * @param corpusFile
	 * @throws GeneralLoggingException
	 */
	public EnglishNerAnonymizationStrategy(File corpusFile) throws GeneralLoggingException {
		super( corpusFile );
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) throws GeneralLoggingException {
		String preAnonymizedText = super.anonymizeText( inputText );
		
		String anonymizedText = preAnonymizedText;
		for( AbstractSequenceClassifier< CoreLabel > classifier : this.classifierList ) {
			anonymizedText = classifier.classifyWithInlineXML( anonymizedText );
			anonymizedText = this.removeTaggedPersons(anonymizedText, classifier);
		}						
		
		return anonymizedText;
	}

	/**
	 * Remove (CRF)-tagged persons.
	 * @param taggedText
	 * @param classifier 
	 * @return a new {@link String}
	 */
	private String removeTaggedPersons( String taggedText, AbstractSequenceClassifier< CoreLabel > classifier ) {
		String cleanedText = taggedText; 
		
		cleanedText = cleanedText.replaceAll( "<PERSON>.*?</PERSON>" , NE_PERSON_REPLACEMENT );
		
		return cleanedText;
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
