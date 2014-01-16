/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Instances of this class are able to parse Part-of-Speech (POS) tagged
 * Strings.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 * 
 */
public class PosParser {

    /**
     * Get a {@link Set} of {@link String} (words) from the POS-tagged String.
     * 
     * @param posTag
     * @param taggedString
     * @return a {@link Set} of words.
     * @throws IllegalArgumentException
     */
    public Set< String > getWords( final String posTag,
            final String taggedString ) {
        if( null == posTag || null == taggedString ) {
            throw new IllegalArgumentException( this.getClass()
                    + "getWords: null value is not allowed in a parameter!" );
        }

        final Set< String > returnedSet = new HashSet< String >();

        // regex to match word and belonging tag
        final String regex = "([A-Za-z.!?,;]+)_(" + posTag + ")";
        // create pattern
        final Pattern pPosTaggedWord = Pattern.compile( regex );
        // process matching
        final Matcher mPosTaggedWord = pPosTaggedWord.matcher( taggedString );

        while( mPosTaggedWord.find() ) {
            final String word = mPosTaggedWord.group( 1 );
            final String matchedPosTag = mPosTaggedWord.group( 2 );

            // check if matched posTag and desired match
            if( !posTag.equals( matchedPosTag ) ) {
                throw new IllegalStateException(
                        this.getClass()
                                + ":getWords(): the given posTag "
                                + posTag
                                + " raised an error in the processing of the regular expression." );
            }
            
            returnedSet.add( word );
        }        

        return returnedSet;
    }

}
