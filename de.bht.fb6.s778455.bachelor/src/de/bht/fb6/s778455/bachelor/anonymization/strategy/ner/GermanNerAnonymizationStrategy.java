/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;
import java.util.List;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
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
	 * Create a new decorated GermanNerAnonymizationStrategy which is reponsible for anonymizing texts containing the German language.
	 * @param decoratingStrategy another {@link AAnomyzationStrategy} which will be called by this strategy instance for further tasks
	 * @param corpusFile
	 * @throws GeneralLoggingException 
	 */
	public GermanNerAnonymizationStrategy(
			AAnomyzationStrategy decoratingStrategy, File corpusFile ) throws GeneralLoggingException {
		super( decoratingStrategy, corpusFile );
	}
	
	/**
	 * Create a new undecorated GermanNerAnonymizationStrategy which is responsible for the anonymization of German texts.
	 * @param corpusFile
	 * @throws GeneralLoggingException 
	 */
	public GermanNerAnonymizationStrategy(File corpusFile) throws GeneralLoggingException {
		super( corpusFile );
	}	
	
	/**
	 * Create a new GermanNerAnonymizationStrategy with a cascade of text corpora.
	 * @param corpusFiles
	 * @throws GeneralLoggingException
	 */
	public GermanNerAnonymizationStrategy(List< File > corpusFiles ) throws GeneralLoggingException {
		super( corpusFiles );
	}
	
	/**
	 * Create a new decorated GermanNerAnonymizationStrategy with a cascade of text corpora.
	 * @param decoratingStrategy
	 * @param corpusFiles
	 * @throws GeneralLoggingException
	 */
	public GermanNerAnonymizationStrategy(AAnomyzationStrategy decoratingStrategy, List< File > corpusFiles ) throws GeneralLoggingException {
		super( decoratingStrategy, corpusFiles );
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
		
		cleanedText = cleanedText.replaceAll( "<I-PER>.*?</I-PER>" , NE_PERSON_REPLACEMENT );
		
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
