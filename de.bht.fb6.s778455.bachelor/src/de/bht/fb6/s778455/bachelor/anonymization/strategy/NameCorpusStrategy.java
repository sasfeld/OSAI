/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>The NameCorpusStrategy checks if a text contains a word which is contained in a person name corpus.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 *
 */
public class NameCorpusStrategy extends AAnomyzationStrategy{

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String, de.bht.fb6.s778455.bachelor.model.Board)
	 */
	public String anonymizeText( String inputText, Board belongingBoard )
			throws GeneralLoggingException {
		// use board specific name corpus here 
		PersonNameCorpus nameCorpus = belongingBoard.getPersonNameCorpus();
		String anonymized = this.anonymizeNames(inputText, nameCorpus);
		return anonymized;
	}

	private String anonymizeNames( String inputText, PersonNameCorpus nameCorpus ) {
		String anonymizedText = super.prepareText( inputText );
		
		// check prenames
		for( String prename : nameCorpus.getPrenames() ) {
			// case 1: name in a single line
			Pattern pNameSingleLine = Pattern
					.compile( "(?i)(?<=^)" + prename
							+ "(?=$)", Pattern.MULTILINE );
			Matcher matcherSingeLine = pNameSingleLine
					.matcher( anonymizedText );
			anonymizedText = matcherSingeLine.replaceAll( NAME_CORPUS_REPLACEMENT );

			// case 2: name at the beginning of a line
			Pattern pNameAtBeginning = Pattern
					.compile( "(?i)(?<=^)" + prename
							+ "(?=[\\s!?.,;-_]{1})", Pattern.MULTILINE );
			Matcher matcherBeginning = pNameAtBeginning
					.matcher( anonymizedText );
			anonymizedText = matcherBeginning.replaceAll( NAME_CORPUS_REPLACEMENT );
			
			// case 3: name at the end of a line
			Pattern pNameAtEnd = Pattern
					.compile( "(?i)(?<=\\s)" + prename
							+ "(?=$)", Pattern.MULTILINE );
			Matcher matcherEnd = pNameAtEnd
					.matcher( anonymizedText );
			anonymizedText = matcherEnd.replaceAll( NAME_CORPUS_REPLACEMENT );
			
			// case 4: name anywhere in line
			Pattern pNameInLine = Pattern
					.compile( "(?i)(?<=\\s)" + prename
							+ "(?=[\\s!?.,;-_]{1})", Pattern.MULTILINE );
			Matcher matcherInLine = pNameInLine
					.matcher( anonymizedText );
			anonymizedText = matcherInLine.replaceAll( NAME_CORPUS_REPLACEMENT );			
		}
		
		return anonymizedText;		
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy#anonymizeText(java.lang.String)
	 */
	public String anonymizeText( String inputText )
			throws GeneralLoggingException {
		// use singleton name corpus here
		PersonNameCorpus nameCorpus = ServiceFactory.getPersonNameCorpusSingleton();
		String anonymized = this.anonymizeNames(inputText, nameCorpus);
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
