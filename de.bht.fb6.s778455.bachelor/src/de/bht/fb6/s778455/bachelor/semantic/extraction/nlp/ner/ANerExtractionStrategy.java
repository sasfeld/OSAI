/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner;

import java.io.File;
import java.util.List;

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
public abstract class ANerExtractionStrategy extends AExtractionStrategy {
	protected CRFClassifier< CoreLabel > classifier;
	protected NerTagMapper nerTagMapper;

	/**
	 * Create a new {@link ANerExtractionStrategy} using the given corpusFile.
	 * 
	 * @param classifierFile
	 */
	public ANerExtractionStrategy( final File classifierFile,
			String... allowedCorpusNames ) {
		if( null == classifierFile ) {
			throw new IllegalArgumentException(
					"Null is not allowed in a parameter!" );
		}

		// check if the classifier file is allowed for the language (sub classes
		// define that)
		// so we try to forbid the usage of classifiers for other languages than
		// the given subclass
		boolean isAllowed = false;
		for( String allowedCorpusName : allowedCorpusNames ) {
			if( classifierFile.getName().startsWith( allowedCorpusName ) ) {
				isAllowed = true;
			}
		}

		if( !isAllowed ) {
			throw new IllegalArgumentException(
					getClass()
							+ ": The given classifierFile doesn't seem to be allowed for this language. It mus be one of the following: "
							+ allowedCorpusNames );
		}

		this._initializeClassifier( classifierFile );
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
	
	/**
	 * Check whether the expected classifier labels are included in the classifier.
	 */
	protected void _checkClassifierLabels() {
		boolean notContained = false;
		for( final String expectedLabel : this.getExpectedNerTags() ) {
			if ( !classifier.labels().contains( expectedLabel )) {
				notContained = true;
			}
		}
		
		// throw exception if not contained
		if ( notContained ) {
			throw new IllegalArgumentException(
					getClass()
							+ ": The given classifierFile doesn't seem to be allowed for this language.");
		}
	}
	
	/**
	 * Each subclass needs to define the NER tags/labels that it expects from
	 * the classifier. So we can do an error handling if a foreign classifier is
	 * used.
	 */
	public abstract List< String > getExpectedNerTags();
	

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
		final String taggedStr = classifier.classifyWithInlineXML( strBuilder
				.toString() );
		this.nerTagMapper.mapToCourse( taggedStr, course );
	}

}
