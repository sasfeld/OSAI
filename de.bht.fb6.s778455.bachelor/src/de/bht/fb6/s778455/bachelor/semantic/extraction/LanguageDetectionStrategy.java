/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import java.rmi.UnexpectedException;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.organization.corpus.CommonNameExcluder;

/**
 * <p>This class realizes an {@link AExtractionStrategy} which detects the language of {@link AUserContribution} or an {@link Course}.</p>
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.01.2014
 *
 */
public class LanguageDetectionStrategy extends AExtractionStrategy {
    
    protected final CommonNameExcluder commonNameExcluder;
    protected double mimumPercentageGerman;
    protected double minumPercentageEnglish;
    protected double minumPercentageDiff;

    /**
     * Create a new LanguageDetectionStrategy.
     * @throws InvalidConfigException
     */
    public LanguageDetectionStrategy() throws InvalidConfigException {
        this.commonNameExcluder = CommonNameExcluder.getInstance();
        
        this.mimumPercentageGerman = 40;
        this.minumPercentageEnglish = 40;
        this.minumPercentageDiff = 10;
    }
    
    /**
     * Decide which {@link Language} the given string represents.
     * @param inputStr
     * @return
     * @throws UnexpectedException no decission can be done
     */
    protected Language decideLanguage( final String inputStr ) throws UnexpectedException {
        // first, split the string in to words (by whitespaces)
        final String[] words = inputStr.split( " " );
        
        // get the word coverage of English words
        final double englishCoverage = this._calculateCoverage(words);
        final double germanCoverage = this._calculateCoverage(words);
        
        // both percentages are large enough
        if ( englishCoverage > this.minumPercentageEnglish && germanCoverage > this.mimumPercentageGerman ) {
            // calculate difference to decide. If the differnce is not large enough, than we cannot decide
            final double coverageDiff = germanCoverage - englishCoverage;         
            
            // case 1: german coverage is much more higher
            if ( coverageDiff > this.minumPercentageDiff ) {
                return Language.GERMAN;
            }
            // case 2: english coverage is much more higher
            if ( coverageDiff < 0 && (coverageDiff * -1) > this.minumPercentageDiff) {
                return Language.ENGLISH;
            }
            
            // case 3: difference is not large enough
            throw new UnexpectedException( "The difference of " + coverageDiff + " is not large enough to decide if the string is more German or English." );
        } 
        // only German percentage is large enough
        if ( germanCoverage > this.mimumPercentageGerman ) {
            return Language.GERMAN;
        }
        // only English percentage is large enough
        if ( englishCoverage > this.minumPercentageEnglish ) {
            return Language.ENGLISH;
        }
        
        return Language.UNKNOWN;
    }

    
    /**
     * Get the coverage of English words in the words array.
     * @param words
     * @return {@link Double} the coverage in percent. Minimum value: 0, maximum: 100
     */
    private double _calculateCoverage( final String[] words ) {
        double numberCommonWords = 0;
        final double numberWords = words.length;
        
        // iterate through words, check if a word is common
        for( final String word : words ) {
            if ( this.commonNameExcluder.isCommon( word, false ) ) {
                numberCommonWords++; 
            }
        }
        
        // return the percentage of common words ( number of common words / number of all words )
        if ( 0 != numberWords ) {
            return (numberCommonWords / numberWords) * 100;
        }
        
        if ( numberCommonWords > numberWords ) {
            return 100;
        }
        
        return 0;
    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
     */
    @Override
    public void extractSemantics( final AUserContribution userContribution )
            throws GeneralLoggingException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
     */
    @Override
    public void extractSemantics( final Course course )
            throws GeneralLoggingException {
        // TODO Auto-generated method stub

    }

}
