/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.statistics;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;

/**
 * <p>This class {@link AStatisticsBuilder} offers statistics on the language in the models.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.01.2014
 *
 */
public class LanguageStatisticsBuilder extends DecoratingStatisticsBuilder {

    public LanguageStatisticsBuilder() {
        super();
    }

    public LanguageStatisticsBuilder( final AStatisticsBuilder builder ) {
        super( builder );
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder#buildStatistics
     * (java.util.Collection)
     */
    @Override
    public StatisticsModel buildStatistics( final LmsCourseSet courses ) {
        final StatisticsModel model = super.buildStatistics( courses );
        
        // variables of interest
        int numberGermanCourses = 0;
        int numberGermanBoards = 0;
        int numberGermanPostings = 0;
        
        int numberEnglishCourses = 0;
        int numberEnglishBoards = 0;
        int numberEnglishPostings = 0;
        
        int numberUnknownLangCourses = 0;
        int numberUnknownLangBoards = 0;
        int numberUnknownLangPostings = 0;
        
        for( final Course course : courses ) {
            switch( course.getLanguage() ) {
            case ENGLISH:
                numberEnglishCourses++;
                break;
            case GERMAN:
                numberGermanCourses++;
                break;
            default:
                numberUnknownLangCourses++;
            }
            
            for( final Board b : course.getBoards() ) {
                switch( b.getLang() ) {
                case ENGLISH:
                    numberEnglishBoards++;
                    break;
                case GERMAN:
                    numberGermanBoards++;
                    break;
                default:
                    numberUnknownLangBoards++;
                }
                
                for( final BoardThread t : b.getBoardThreads() ) {
                    for( final Posting p : t.getPostings() ) {
                        switch( p.getLang() ) {
                        case ENGLISH:
                            numberEnglishPostings++;
                            break;
                        case GERMAN:
                            numberGermanPostings++;
                            break;
                        default:
                            numberUnknownLangPostings++;
                        }
                    }
                }
            }
            
        }
        
        // set on statistics model
        model.setNumberGermanCourses( numberGermanCourses )
            .setNumberGermanBoards( numberGermanBoards )
            .setNumberGermanPostings( numberGermanPostings )
            .setNumberEnglishCourses( numberEnglishCourses )
            .setNumberEnglishBoards( numberEnglishBoards )
            .setNumberEnglishPostings( numberEnglishPostings )
            .setNumberUnknownLangCourses( numberUnknownLangCourses )
            .setNumberUnknownLangBoards( numberUnknownLangBoards )
            .setNumberUnknownLangPostings( numberUnknownLangPostings );
        
        return model;
    }
}
    
  
