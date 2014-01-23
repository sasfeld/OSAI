/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			final AAnomyzationStrategy decoratingStrategy, final File corpusFile ) throws GeneralLoggingException {
		super( decoratingStrategy, corpusFile );
	}
	
	/**
	 * Create a new undecorated GermanNerAnonymizationStrategy which is responsible for the anonymization of German texts.
	 * @param corpusFile
	 * @throws GeneralLoggingException 
	 */
	public GermanNerAnonymizationStrategy(final File corpusFile) throws GeneralLoggingException {
		super( corpusFile );
	}	
	
	/**
	 * Create a new GermanNerAnonymizationStrategy with a cascade of text corpora.
	 * @param corpusFiles
	 * @throws GeneralLoggingException
	 */
	public GermanNerAnonymizationStrategy(final List< File > corpusFiles ) throws GeneralLoggingException {
		super( corpusFiles );
	}
	
	/**
	 * Create a new decorated GermanNerAnonymizationStrategy with a cascade of text corpora.
	 * @param decoratingStrategy
	 * @param corpusFiles
	 * @throws GeneralLoggingException
	 */
	public GermanNerAnonymizationStrategy(final AAnomyzationStrategy decoratingStrategy, final List< File > corpusFiles ) throws GeneralLoggingException {
		super( decoratingStrategy, corpusFiles );
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	@Override
    public String anonymizeText( final String inputText ) throws GeneralLoggingException {
		final String preAnonymizedText = super.anonymizeText( inputText );
		
		String anonymizedText = preAnonymizedText;
		for( final AbstractSequenceClassifier< CoreLabel > classifier : this.classifierList ) {
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
	private String removeTaggedPersons( final String taggedText, final AbstractSequenceClassifier< CoreLabel > classifier ) {
		String cleanedText = taggedText; 
		
		// check common names before replacing
		final Pattern pPersTag = Pattern.compile( "<I-PER>(.*?)</I-PER>" );
		final Matcher mPersTag = pPersTag.matcher( cleanedText );
		while (mPersTag.find()) {
			final String persTag = mPersTag.group(1);
			
			if (!this.commonNameExcluder.isCommon( persTag, true )) {
				cleanedText = mPersTag.replaceAll( NE_PERSON_REPLACEMENT );
			}
		}
		
		return cleanedText;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String, de.bht.fb6.s778455.bachelor.model.Board)
	 */
	public String anonymizeText( final String inputText, final Board belongingBoard )
			throws GeneralLoggingException {
		// we don't need the board instance here
		return this.anonymizeText( inputText ); 
	}
}
