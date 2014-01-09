/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp;

import java.io.File;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * <p>
 * This class realizes a strategy to extract semantical information using
 * Stanford NLP.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class NerExtractionStrategy extends AExtractionStrategy {
	protected CRFClassifier< CoreLabel > classifier;
	protected NerTagMapper nerTagMapper;

	/**
	 * Create a new {@link NerExtractionStrategy} using the given corpusFile.
	 * 
	 * @param corpusFile
	 */
	public NerExtractionStrategy( final File corpusFile ) {
		if( null == corpusFile ) {
			throw new IllegalArgumentException(
					"Null is not allowed in a parameter!" );
		}

		this._initializeClassifier( corpusFile );
	}

	/**
	 * Initialize the Stanford classifier.
	 * 
	 * @param corpusFile
	 */
	private void _initializeClassifier( File corpusFile ) {
		this.classifier = CRFClassifier.getClassifierNoExceptions( corpusFile
				.getAbsolutePath() );
		this.nerTagMapper = new NerTagMapper( classifier.labels() );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#
	 * extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
	 */
	@Override
	public void extractSemantics( AUserContribution userContribution )
			throws GeneralLoggingException {
		if( null == userContribution ) {
			throw new IllegalArgumentException(
					"Null is not allowed in a parameter!" );
		}

		String strToTag = "";
		if( userContribution instanceof Posting ) {
			// posting: sent the content of the posting
			strToTag = ( ( Posting ) userContribution ).getContent();
		} else if( userContribution instanceof Board ) {
			Board b = ( Board ) userContribution;

			// board: concat title and intro
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append( b.getTitle() + "\n\n" ).append( b.getIntro() );

			strToTag = strBuilder.toString();
		} else {
			// return because the given userContribution isn't supported
			return;
		}

		// let classifier NER tag
		final String taggedStr = classifier.classifyWithInlineXML( strToTag );
		this.nerTagMapper.mapToContribution( taggedStr, userContribution );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#
	 * extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
	 */
	@Override
	public void extractSemantics( Course course )
			throws GeneralLoggingException {
		// concat the title and summary of the course to get a maxmimum of words
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append( course.getTitle() + "\n\n" ).append(
				course.getSummary() );

		// let classifier NER tag
		final String taggedStr = classifier.classifyWithInlineXML( strBuilder.toString() );
		this.nerTagMapper.mapToCourse( taggedStr, course );
	}

}
