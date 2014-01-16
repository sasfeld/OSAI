/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos;

import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PosTag;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;

/**
 * <p>
 * A PosTagMapper is responsible for taking POS-tagged Strings and do a mapping
 * of POS-tagged words to attributes in the models (AUserContribution or
 * {@link Course} for example)
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 * 
 */
public class PosTagMapper {
    protected Set< String > posTags;
    protected final PosParser posParser;

    /**
     * Create a new PosTagMapper with a given Set of posTags which have to get mapped.
     * @param posTags
     * @throws IllegalArgumentException
     */
    public PosTagMapper( final Set< String > posTags ) {
        if( null == posTags ) {
            throw new IllegalArgumentException( this.getClass()
                    + ": null value is not allowed in a parameter!" );
        }
        this.posTags = posTags;
        
        this.posParser = new PosParser();
    }
    
    /**
     * Map a POS-tagged string to a {@link AUserContribution}.
     * @param taggedString
     * @param contribution
     * @throws IllegalArgumentException
     */
    public void mapToContribution(final String taggedString, final AUserContribution contribution) {
        if ( null == taggedString || null == contribution ) {
            throw new IllegalArgumentException( this.getClass()
                    + "mapToContribution: null value is not allowed in a parameter!" );
        }
        
        for( final String posTag : this.posTags ) {
            final Set< String > words = this.posParser.getWords( posTag, taggedString );
            
            for( final String word : words ) {
                final Tag newPosTag = new PosTag( posTag, 0.0, word, "" );
                contribution.addTag( newPosTag, TagType.POS_TAG );
            }
        }
    }
    
    /**
     * Map a POS-tagged string to a {@link Course}.
     * @param taggedString
     * @param course
     * @throws IllegalArgumentException
     */
    public void mapToCourse(final String taggedString, final Course course) {
        if ( null == taggedString || null == course ) {
            throw new IllegalArgumentException( this.getClass()
                    + "mapToCourse: null value is not allowed in a parameter!" );
        }
        
        for( final String posTag : this.posTags ) {
            final Set< String > words = this.posParser.getWords( posTag, taggedString );
            
            for( final String word : words ) {
                final Tag newPosTag = new PosTag( posTag, 0.0, word, "" );
                course.addTag( newPosTag, TagType.POS_TAG );
            }
        }
    }

}
