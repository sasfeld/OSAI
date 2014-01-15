/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class realizes an {@link ANerExtractionStrategy} for the English language.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.01.2014
 *
 */
public class EnglishNerExtractionStrategy extends ANerExtractionStrategy {
	protected List< String > expectedNerTags;
	
	/**
	 * Create a new {@link EnglishNerExtractionStrategy}. Make sure that the classifierFile has a name one of: english.all.3class.distsim, english.conll.4class.distsim, english.muc.7class.distsim 
	 * @param classifierFile
	 */
	public EnglishNerExtractionStrategy( File classifierFile ) {
		super( classifierFile, "english.all.3class.distsim", "english.conll.4class.distsim", "english.muc.7class.distsim" );	
		
		this.expectedNerTags = new ArrayList<String>();
		
		// minimum required
		this.expectedNerTags.add( "LOCATION" );
		this.expectedNerTags.add( "ORGANIZATION" );		
		
		// 4class and up
		if ( classifierFile.getName().contentEquals( "4class" )) {
			this.expectedNerTags.add( "MISC" );
		} else if ( classifierFile.getName().contentEquals( "7class" )) {
			this.expectedNerTags.add( "TIME" );
			this.expectedNerTags.add( "MONEY" );
			this.expectedNerTags.add( "PERCENT" );
			this.expectedNerTags.add( "DATE" );
		}
		
		super._checkClassifierLabels();
	}
	
	
	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner.ANerExtractionStrategy#getExpectedNerTags()
	 */
	@Override
	public List< String > getExpectedNerTags() {
		return this.getExpectedNerTags();
	}

}
