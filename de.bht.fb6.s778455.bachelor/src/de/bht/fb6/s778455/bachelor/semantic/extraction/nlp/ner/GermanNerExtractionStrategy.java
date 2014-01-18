/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class is an implementation of an {@link ANerExtractionStrategy} for the German language.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.01.2014
 *
 */
public class GermanNerExtractionStrategy extends ANerExtractionStrategy {
	protected List< String > expectedNerTags;
	
	/**
	 * Create a new GermanNerExtractionStrategy. Make sure to hand in the correct German classifier.
	 * @param corpusFile a file which must have a name of 'hgc_175m_600' or 'dewac_175m_600'
	 * 
	 */
	public GermanNerExtractionStrategy( File corpusFile ) {
		super( corpusFile, "hgc_175m_600", "dewac_175m_600" );	
		
		this.expectedNerTags = new ArrayList<String>();
		this.expectedNerTags.add( "I-ORG" );
		this.expectedNerTags.add( "I-LOC" );
		this.expectedNerTags.add( "I-MISC" );
		
		super._checkClassifierLabels();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner.ANerExtractionStrategy#getExpectedNerTags()
	 */
	public List< String > getExpectedNerTags() {
		return this.expectedNerTags;
	}

}
