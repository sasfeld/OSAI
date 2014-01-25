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
 * <p>
 * This {@link AStatisticsBuilder} builds general statistics, such as:
 * <ul>
 * <li>Number of courses, boards, threads and postings</li>
 * ...
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class GeneralStatisticsBuilder extends DecoratingStatisticsBuilder {

    public GeneralStatisticsBuilder() {
        super();
    }

    public GeneralStatisticsBuilder( final AStatisticsBuilder builder ) {
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

        final int numberCourses = courses.size();
        int numberBoards = 0;
        int numberThreads = 0;
        int numberPostings = 0;
        int numberPostingsEmptyContent = 0;

        for( final Course course : courses ) {
            numberBoards += course.getBoards().size();

            if( null != course.getBoards() ) {
                for( final Board board : course.getBoards() ) {
                    numberThreads += board.getBoardThreads().size();

                    if( null != board.getBoardThreads() ) {
                        for( final BoardThread boardThread : board
                                .getBoardThreads() ) {
                            numberPostings += boardThread.getPostings().size();

                            if( null != boardThread.getPostings() ) {
                                for( final Posting p : boardThread
                                        .getPostings() ) {
                                    if( null != p.getContent() ) {
                                        final String postingContent = p
                                                .getContent();
                                        if( 0 == postingContent.trim().length() ) {
                                            numberPostingsEmptyContent++;
                                        }
                                    }
                                }
                            } else {
                                numberPostingsEmptyContent++;
                            }
                        }
                    }
                }
            }
        }

        model.setNumberCourses( numberCourses ).setNumberBoards( numberBoards )
                .setNumberThreads( numberThreads )
                .setNumberPostings( numberPostings )
                .setNumberPostingsEmptyContent( numberPostingsEmptyContent );

        return model;
    }

}
