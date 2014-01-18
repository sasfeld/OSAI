/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class implements an {@link APosExtractionStrategy} for the English
 * language.
 * </p>
 * <p>
 * Please make sure to commit only English POS taggers and models with English
 * contents.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 * 
 */
public class EnglishPosExtractionStrategy extends APosExtractionStrategy {
    protected List< String > expectedPosTags;
    
    /**
     * Create a new {@link APosExtractionStrategy} for the English language.
     * 
     * <p>
     * Please make sure that you don't commit a POS model for another language
     * or models that don't explicitly represent English contents.
     * </p>
     * 
     * @param model
     */
    public EnglishPosExtractionStrategy( final File model ) {
        super( model, "english-bidirectional-distsim", "english-caseless-left3words-distsim", "english-left3words-distsim" );       
        
        this.expectedPosTags = new ArrayList<String>();
        this.expectedPosTags.add( "NN" ); // common noun, singular or mass
        this.expectedPosTags.add( "NNS" ); // common noun, plural        
        
        super._checkTaggerLabels();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.
     * APosExtractionStrategy#getExpectedPosTags()
     */
    @Override
    public List< String > getExpectedPosTags() {       
        return this.expectedPosTags;
    }

}
