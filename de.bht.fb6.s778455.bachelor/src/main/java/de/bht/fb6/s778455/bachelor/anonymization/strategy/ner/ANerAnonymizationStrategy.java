/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.anonymization.strategy.ner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.corpus.CommonNameExcluder;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * 
 * <p>
 * This class is an implementation of an {@link AAnomyzationStrategy} using the
 * Stanford NER library.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 * 
 */
public abstract class ANerAnonymizationStrategy extends AAnomyzationStrategy {
	private AAnomyzationStrategy decoratingStrategy;
	protected File textCorpus;
	protected List< File > textCorpora;
	protected List< AbstractSequenceClassifier< CoreLabel > > classifierList;
	protected CommonNameExcluder commonNameExcluder;

	/**
	 * Create a new {@link AAnomyzationStrategy} which uses the Stanford NER
	 * library.
	 * 
	 * @param decoratingStrategy
	 *            the anonymization strategy which decorates this strategy.
	 * @param corpusFile
	 * @throws GeneralLoggingException 
	 */
	public ANerAnonymizationStrategy( AAnomyzationStrategy decoratingStrategy,
			File corpusFile ) throws GeneralLoggingException {
		this.decoratingStrategy = decoratingStrategy;
		this.textCorpus = corpusFile;
		this.textCorpora = null;
		this.commonNameExcluder = CommonNameExcluder.getInstance();

		this.initializeClassifier();
	}

	/**
	 * Create a new {@link AAnomyzationStrategy} which uses the Stanford NER
	 * library.
	 * 
	 * @param corpusFile
	 * @throws GeneralLoggingException 
	 */
	public ANerAnonymizationStrategy( File corpusFile ) throws GeneralLoggingException {
		this.decoratingStrategy = null;
		this.textCorpus = corpusFile;
		this.textCorpora = null;
		this.commonNameExcluder = CommonNameExcluder.getInstance();

		this.initializeClassifier();
	}

	/**
	 * Create a new {@link ANerAnonymizationStrategy} using cascades of corpus
	 * files.
	 * 
	 * @param corpusFiles
	 * @throws GeneralLoggingException 
	 */
	public ANerAnonymizationStrategy( List< File > corpusFiles ) throws GeneralLoggingException {
		this.decoratingStrategy = null;
		this.textCorpus = null;
		this.textCorpora = corpusFiles;
		this.commonNameExcluder = CommonNameExcluder.getInstance();

		this.initializeClassifier();
	}

	/**
	 * Create a new decorated {@link ANerAnonymizationStrategy} using cascades
	 * of corpus files.
	 * 
	 * @param decoratingStrategy
	 *            the anonymization strategy which decorates this strategy.
	 * @param corpusFiles
	 * @throws GeneralLoggingException 
	 */
	public ANerAnonymizationStrategy( AAnomyzationStrategy decoratingStrategy,
			List< File > corpusFiles ) throws GeneralLoggingException {
		this.decoratingStrategy = decoratingStrategy;
		this.textCorpus = null;
		this.textCorpora = corpusFiles;
		this.commonNameExcluder = CommonNameExcluder.getInstance();

		this.initializeClassifier();
	}

	/**
	 * Initialize the Stanford CRF classifier.
	 * @throws GeneralLoggingException 
	 */
	protected void initializeClassifier() throws GeneralLoggingException {
		if( null == this.textCorpus && null == this.textCorpora ) {
			throw new GeneralLoggingException(
					getClass()
							+ ":initializeClassifier(): null pointer for textCorpus and the list of text corpora. That must be an implementation error or the illegal usage of class members.",
					"An internal error in the anonymization module occured. Please read the logs." );
		}

		this.classifierList = new ArrayList< AbstractSequenceClassifier<CoreLabel> >();
		
		// create single corpus
		if( null != this.textCorpus && null == this.textCorpora ) {
			CRFClassifier< CoreLabel > classifier = CRFClassifier
					.getClassifierNoExceptions( this.textCorpus
							.getAbsolutePath() );
			this.classifierList.add( classifier );
		}
		// create multiple corpora
		else if( null == this.textCorpus && null != this.textCorpora ) {
			for( File corpusFile : this.textCorpora ) {
				CRFClassifier< CoreLabel > classifier = CRFClassifier
						.getClassifierNoExceptions( corpusFile
								.getAbsolutePath() );
				this.classifierList.add( classifier );
			}
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText ) throws GeneralLoggingException {
		String preparedText = inputText;
		
		// delegate to decorating strategy
		if( null != this.decoratingStrategy ) {
			preparedText = this.decoratingStrategy.anonymizeText( inputText );
		}

		return preparedText;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #removeSpecialTags(java.lang.String)
	 */
	public String removeSpecialTags( String inputText ) {
		String cleanedText = inputText;

		for( AbstractSequenceClassifier< ? > classifier : this.classifierList ) {
			Set< String > tags = classifier.labels();

			for( String tag : tags ) {
				// replace all tag appearances
				cleanedText = cleanedText.replaceAll( "</?" + tag + ">", "" );
			}
		}		

		return cleanedText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #getWrappedStrategies()
	 */
	public List< AAnomyzationStrategy > getWrappedStrategies() {
		List< AAnomyzationStrategy > strategyList = new ArrayList< AAnomyzationStrategy >();
		strategyList.add( this );

		if( null != this.decoratingStrategy ) {
			// use recursion to get decorating classes list from decorating
			// instances
			List< AAnomyzationStrategy > subList = this.decoratingStrategy
					.getWrappedStrategies();
			strategyList.addAll( subList );
		}

		return strategyList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #getDetails()
	 */
	public String getDetails() {
		StringBuilder detailsBuilder = new StringBuilder();
		detailsBuilder.append( "Strategy class: " + getClass() + "\n" ).append(
				"Text corpora: " + this.textCorpora );

		return detailsBuilder.toString();
	}

}
