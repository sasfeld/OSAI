/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.statistics;

import java.util.Collection;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;

/**
 * <p>This {@link AStatisticsBuilder} builds general statistics, such as: 
 * <ul>
 * 	<li>Number of courses, boards, threads and postings</li>
 * ...
 * </ul></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 *
 */
public class GeneralStatisticsBuilder extends DecoratingStatisticsBuilder{

	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder#buildStatistics(java.util.Collection)
	 */
	public StatisticsModel buildStatistics( final Collection< Course > courses ) {
		final StatisticsModel model = super.buildStatistics( courses );
		
		int numberCourses = courses.size();
		int numberBoards = 0;
		int numberThreads = 0;
		int numberPostings = 0;

		for( Course course : courses ) {
			numberBoards += course.getBoards().size();
			for( Board board : course.getBoards() ) {				
				numberThreads += board.getBoardThreads().size();
				for( BoardThread boardThread : board.getBoardThreads() ) {					
					numberPostings += boardThread.getPostings().size();
				}				
			}
		}
		
		model.setNumberCourses( numberCourses )
			.setNumberBoards( numberBoards )
			.setNumberThreads( numberThreads )
			.setNumberPostings( numberPostings );
		
		
		return model;
	}
	
}
