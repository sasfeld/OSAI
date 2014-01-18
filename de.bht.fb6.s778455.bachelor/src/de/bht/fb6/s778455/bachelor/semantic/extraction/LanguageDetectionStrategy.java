/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import java.rmi.UnexpectedException;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.organization.StringUtil;
import de.bht.fb6.s778455.bachelor.organization.corpus.CommonWordCorpus;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;

/**
 * <p>
 * This class realizes an {@link AExtractionStrategy} which detects the language
 * of {@link AUserContribution} or an {@link Course}.
 * </p>
 * <p>
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.01.2014
 * 
 */
public class LanguageDetectionStrategy extends AExtractionStrategy {

    protected final CommonWordCorpus commonWordCorpus;
    protected double mimumPercentageGerman;
    protected double minumPercentageEnglish;
    protected double minumPercentageDiff;

    /**
     * Create a new LanguageDetectionStrategy.
     * 
     * @throws InvalidConfigException
     */
    public LanguageDetectionStrategy() throws InvalidConfigException {
        this.commonWordCorpus = CommonWordCorpus.getCommonWordCorpus();

        this.mimumPercentageGerman = Double.parseDouble( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_LANGUAGEDETECTION_MIMIMUM_GERMAN ) );
        this.minumPercentageEnglish = Double.parseDouble( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_LANGUAGEDETECTION_MIMIMUM_ENGLISH ) );
        this.minumPercentageDiff =  Double.parseDouble( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_LANGUAGEDETECTION_MIMIMUM_DIFFERENCE ));
    }

    /**
     * Decide which {@link Language} the given string represents.
     * 
     * @param inputStr
     * @return
     * @throws UnexpectedException
     *             no decission can be done
     */
    protected Language decideLanguage( final String inputStr )
            throws UnexpectedException {
        // prepare string
        final String preparedStr = super.prepareText( inputStr );
        // first, split the string in to words (by whitespaces)
        final Set<String> words = StringUtil.getWords( preparedStr );

        // get the word coverage of English words
        final double englishCoverage = this._calculateCoverage( words, Language.ENGLISH );
        final double germanCoverage = this._calculateCoverage( words, Language.GERMAN );

        // both percentages are large enough
        if( englishCoverage > this.minumPercentageEnglish
                && germanCoverage > this.mimumPercentageGerman ) {
            // calculate difference to decide. If the difference is not large
            // enough, than we cannot decide
            final double coverageDiff = germanCoverage - englishCoverage;

            // case 1: german coverage is much more higher
            if( coverageDiff > this.minumPercentageDiff ) {
                return Language.GERMAN;
            }
            // case 2: english coverage is much more higher
            if( coverageDiff < 0
                    && ( coverageDiff * -1 ) > this.minumPercentageDiff ) {
                return Language.ENGLISH;
            }

            // case 3: difference is not large enough
            throw new UnexpectedException(
                    "The difference of "
                            + coverageDiff
                            + " is not large enough to decide if the string is more German or English." );
        }
        // only German percentage is large enough
        if( germanCoverage > this.mimumPercentageGerman ) {
            return Language.GERMAN;
        }
        // only English percentage is large enough
        if( englishCoverage > this.minumPercentageEnglish ) {
            return Language.ENGLISH;
        }

        // throw exception if none of the percentages is high enoguh
        throw new UnexpectedException(
                "None of the coverages is high enough to decide the languages. English word coverage: "
                        + englishCoverage
                        + "; German word coverage: "
                        + germanCoverage );
    }

    /**
     * Get the coverage of English words in the words array.
     * 
     * @param words
     * @param english 
     * @return {@link Double} the coverage in percent. Minimum value: 0,
     *         maximum: 100
     */
    private double _calculateCoverage( final Set< String > words, final Language lang ) {
        double numberCommonWords = 0;
        final double numberWords = words.size();

        // iterate through words, check if a word is common
        for( final String word : words ) {
            if ( lang.equals( Language.GERMAN )) {
                if( this.commonWordCorpus.getCommonGermanWords().contains( word ) ) {
                    numberCommonWords++;
                }
            } else if (lang.equals( Language.ENGLISH ) ) {
                if( this.commonWordCorpus.getCommonEnglishWords().contains( word ) ) {
                    numberCommonWords++;
                }
            } else {
                throw new IllegalArgumentException( "lang must be one of English or German!" );
            }
            
        }

        // return the percentage of common words ( number of common words /
        // number of all words )
        if( 0 != numberWords ) {
            return ( numberCommonWords / numberWords ) * 100;
        }

        if( numberCommonWords > numberWords ) {
            return 100;
        }

        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#
     * extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
     */
    @Override
    public void extractSemantics( final AUserContribution userContribution )
            throws GeneralLoggingException {
        // string of interest
        String strofInterest = "";
        if( userContribution instanceof Posting ) {
            // posting: sent the content of the posting
            strofInterest = ( ( Posting ) userContribution ).getContent();
        } else if( userContribution instanceof Board ) {
            final Board b = ( Board ) userContribution;

            // board: concat title and intro
            final StringBuilder strBuilder = new StringBuilder();
            strBuilder.append( b.getTitle() + "\n\n" ).append( b.getIntro() );

            strofInterest = strBuilder.toString();
        } else {
            // return because the given userContribution isn't supported
            return;
        }

        // try decision of language
        try {
            final Language lang = this.decideLanguage( strofInterest );
            userContribution.setLang( lang );
        } catch( final UnexpectedException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":extractSemantics(): couldn't decide the language for the UserContribution with title: "
                            + userContribution.getTitle() + "\nexception: "
                            + e.getMessage(),
                    "Internal error in the LanguageDetectionStrategy. Read the logs." );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#
     * extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
     */
    @Override
    public void extractSemantics( final Course course )
            throws GeneralLoggingException {
        // concat the title and summary of the course to get a maxmimum of words
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append( course.getTitle() + "\n\n" ).append(
                course.getSummary() );
        // try decision of language
        try {
            final Language lang = this.decideLanguage( strBuilder.toString() );
            course.setLanguage( lang );
        } catch( final UnexpectedException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":extractSemantics(): couldn't decide the language for the Course with title: "
                            + course.getTitle() + "\nexception: "
                            + e.getMessage(),
                    "Internal error in the LanguageDetectionStrategy. Read the logs." );
        }
    }

}
