/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization.corpus;

import java.util.ArrayList;
import java.util.List;

import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>This class offers functionality to exclude common words (e.g. articles in German or English) from the anonymization process.</p>
 * <p>Strategies which use the common name excluder have to poll whether an entity shall be excluded.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 19.12.2013
 *
 */
public class CommonNameExcluder {
	protected static CommonNameExcluder instance;
	protected CommonWordCorpus corpus;


    protected CommonNameExcluder() throws InvalidConfigException {
		this.corpus = CommonWordCorpus.getCommonWordCorpus();
	}	


	/**
	 * Get the singleton {@link CommonNameExcluder}. 
	 * @return
	 * @throws InvalidConfigException
	 */
	public static CommonNameExcluder getInstance() throws InvalidConfigException {
		if ( null == instance) {
			instance = new CommonNameExcluder();
		}
		
		return instance;
	}
	
	/**
	 * Check whether a given word is common in any language which the excluder checks.
	 * @param word
	 * @param caseSensitive set false if you want the word to be checked case insensitive (upper and lower characters don't matter)
	 * @return true if the word is a common word. 
	 */
	public boolean isCommon( final String word, final boolean caseSensitive ) {
		String checkedWord = word.trim();
		
		final List< String > combinedLists = new ArrayList<String>();
		combinedLists.addAll( this.corpus.getCommonEnglishWords() );
		combinedLists.addAll( this.corpus.getCommonEnglishWords() );
		
		for( final String commonWord : combinedLists ) {
			String checkingWord = commonWord;
			
			// lower both words to compare them
			if ( caseSensitive ) {
				checkedWord = checkedWord.toLowerCase();
				checkingWord = commonWord.toLowerCase();
			}
			
			// check equality of both words
			if ( checkedWord.equals( checkingWord )) {
				return true;
			}
		}
		
		return false;
	}
}
