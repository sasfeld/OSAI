/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.anonymization.CommonNameExcluder;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>
 * The NameCorpusStrategy checks if a text contains a word which is contained in
 * a person name corpus.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 * 
 */
public class NameCorpusStrategy extends AAnomyzationStrategy {
	protected CommonNameExcluder commonNameExcluder;

	/**
	 * Create and initialize a new {@link NameCorpusStrategy}.
	 * 
	 * @throws InvalidConfigException
	 */
	public NameCorpusStrategy() throws InvalidConfigException {
		this.commonNameExcluder = CommonNameExcluder.getInstance();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #anonymizeText(java.lang.String, de.bht.fb6.s778455.bachelor.model.Board)
	 */
	public String anonymizeText( String inputText, Board belongingBoard )
			throws GeneralLoggingException {
		// use board specific name corpus here
		PersonNameCorpus nameCorpus = belongingBoard.getBelongingCourse()
				.getPersonNameCorpus();
		String anonymized = this.anonymizeNames( inputText, nameCorpus );
		return anonymized;
	}

	private String anonymizeNames( String inputText, PersonNameCorpus nameCorpus )
			throws GeneralLoggingException {
		String anonymizedText = super.prepareText( inputText );
		List< String > names = new ArrayList< String >();
		names.addAll( nameCorpus.getPrenames() );
		names.addAll( nameCorpus.getLastnames() );

		// check all names
		for( String name : names ) {
			// exclude common names
			if( !this.commonNameExcluder.isCommon( name, true ) ) {
				// case 1: name in a single line
				Pattern pNameSingleLine = Pattern.compile( "(?i)(?<=^)" + name
						+ "(?=$)(?![a-zA-Z0-9]+)", Pattern.MULTILINE );
				Matcher matcherSingeLine = pNameSingleLine
						.matcher( anonymizedText );
				anonymizedText = matcherSingeLine
						.replaceAll( NAME_CORPUS_REPLACEMENT );

				// case 2: name at the beginning of a line
				Pattern pNameAtBeginning = Pattern.compile( "(?i)(?<=^)" + name
						+ "(?=[\\s!?.,;-_]{1})(?![a-zA-Z0-9]+)",
						Pattern.MULTILINE );
				Matcher matcherBeginning = pNameAtBeginning
						.matcher( anonymizedText );
				anonymizedText = matcherBeginning
						.replaceAll( NAME_CORPUS_REPLACEMENT );

				// case 3: name at the end of a line
				Pattern pNameAtEnd = Pattern.compile( "(?i)(?<=\\s)" + name
						+ "(?=$)(?![a-zA-Z0-9]+)", Pattern.MULTILINE );
				Matcher matcherEnd = pNameAtEnd.matcher( anonymizedText );
				anonymizedText = matcherEnd
						.replaceAll( NAME_CORPUS_REPLACEMENT );

				// case 4: name anywhere in line
				Pattern pNameInLine = Pattern.compile( "(?i)(?<=\\s)" + name
						+ "(?=[\\s!?.,;-_]{1})(?![a-zA-Z0-9]+)" );
				Matcher matcherInLine = pNameInLine.matcher( anonymizedText );
				anonymizedText = matcherInLine
						.replaceAll( NAME_CORPUS_REPLACEMENT );
			}
		}

		return anonymizedText;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy
	 * #anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText )
			throws GeneralLoggingException {
		// use singleton name corpus here
		PersonNameCorpus nameCorpus = ServiceFactory
				.getPersonNameCorpusSingleton();
		String anonymized = this.anonymizeNames( inputText, nameCorpus );
		return anonymized;
	}

	@Override
	public String removeSpecialTags( String inputText ) {
		// TODO Auto-generated method stub
		return inputText;
	}

	@Override
	public List< AAnomyzationStrategy > getWrappedStrategies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
