/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;

/**
 * <p>This static util class offers general methods to work with Moodle postings.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.01.2014
 *
 */
public class MoodleHelper {
    
    /**
     * Remove special characters from imported texts from moodle. 
     */
    public static String removeMoodleChars( final String cleanedText ) {
        String newCleanedText = cleanedText;
        
        final Pattern pTag = Pattern.compile( "</?.*?/?>" );
        final Matcher mTag = pTag.matcher( newCleanedText );
        final List< String > matchedSequences = new ArrayList<String>();
        
        while ( mTag.find() ) {
            final String matchedSequence = mTag.group();
            // exclude anonymization replacement tags
            if ( !matchedSequence.contains( AAnomyzationStrategy.LEARNED_PERSON_NAME_REPLACEMENT )
                && !matchedSequence.contains( AAnomyzationStrategy.NAME_CORPUS_REPLACEMENT )
                && !matchedSequence.contains( AAnomyzationStrategy.PERSONAL_DATA_REPLACEMENT )
                && !matchedSequence.contains( AAnomyzationStrategy.PERSONAL_GREETING_REPLACEMENT )
                && !matchedSequence.contains( AAnomyzationStrategy.NE_PERSON_REPLACEMENT )) {
                matchedSequences.add( matchedSequence );
            }
        }
        for( final String matchedSequence : matchedSequences ) {
            try {
            newCleanedText = newCleanedText.replaceAll( matchedSequence, "" );
            } catch ( final Exception e) { 
                // be defensive here
            }
        }
        newCleanedText = newCleanedText.replaceAll( "(\\\\r\\\\n|\\\\n|\\\\t)", " " );
        newCleanedText = newCleanedText.replaceAll( "&quot;", "" );
        
        return newCleanedText;
    }
}
