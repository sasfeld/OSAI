/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class realizes an {@link APosExtractionStrategy} for the German
 * language.
 * </p>
 * <p>
 * Please make sure that you don't commit a POS model for another language or
 * models that don't explicitly represent German contents.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 * 
 */
public class GermanPosExtractionStrategy extends APosExtractionStrategy {
    protected List< String > expectedPosTags;

    /**
     * Create a new {@link APosExtractionStrategy} for the German language.
     * 
     * <p>
     * Please make sure that you don't commit a POS model for another language
     * or models that don't explicitly represent German contents.
     * </p>
     * 
     * @param model
     */
    public GermanPosExtractionStrategy( final File model ) {
        super( model, "german-dewac", "german-fast", "german-hgc" );       
        
        this.expectedPosTags = new ArrayList<String>();
        this.expectedPosTags.add( "NN" ); // normal noun is currently the only tag of interest
        
        super._checkTaggerLabels();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.APosExtractionStrategy#getExpectedPosTags()
     */
    public List< String > getExpectedPosTags() {
        return this.expectedPosTags;
    }

}
