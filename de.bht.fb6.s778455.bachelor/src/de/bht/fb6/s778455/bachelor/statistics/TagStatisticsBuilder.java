/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Posting.TagType;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;

/**
 * <p>This {@link AStatisticsBuilder} fills a {@link StatisticsModel} with information about the tags in {@link Posting} models.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 *
 */
public class TagStatisticsBuilder extends DecoratingStatisticsBuilder {

	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder#buildStatistics(java.util.Collection)
	 */
	public StatisticsModel buildStatistics( final Collection< Course > courses ) {
		final StatisticsModel model = super.buildStatistics( courses );
		
		int numberTopicZoomTags = 0;
		int numberNerTags = 0;
		int numberTags = 0;
		
		int numberPostingsTzTags = 0;
		int numberPostingsNerTags = 0;
		int numberPostingsTags = 0;
		
		int numberPostingsTzAndNerTags = 0;
		
		List< Posting > untaggedPostings = new ArrayList< Posting >();
		
		for( Course course : courses ) {
			for( Board board : course.getBoards() ) {
				for( BoardThread thread : board.getBoardThreads() ) {
					for( Posting posting : thread.getPostings() ) {
						// TopicZoomTags
						if ( posting.isTopicZoomTagged() ) {
							numberTopicZoomTags += posting.getTags( TagType.TOPIC_ZOOM ).size();
							numberPostingsTzTags++;
						}
						
						// NER tags
						if ( posting.isNerTagged() ) {
							numberNerTags += posting.getTags( TagType.NER_TAGS ).size();
							numberPostingsNerTags++;
						}
						
						// general tags
						if ( posting.isTagged() ) {
							numberTags += posting.getNumberTags();
							numberPostingsTags++;
						} else { // add posting to untagged list
							untaggedPostings.add( posting );
						}
						
						// intersection stats
						if ( posting.isNerTagged() && posting.isTopicZoomTagged() ) {
							numberPostingsTzAndNerTags += 1;
						} 
					}					
				}				
			}
		}
		
		model.setNumberTopicZoomTaggedPostings( numberPostingsTzTags )
			.setNumberNerTaggedPostings( numberPostingsNerTags )
			.setNumberTaggedPostings( numberPostingsTags )
			.setNumberTzAndNerTaggedPostings( numberPostingsTzAndNerTags)
			.setNumberTopicZoomTags( numberTopicZoomTags )
			.setNumberNerTags( numberNerTags )
			.setNumberTags( numberTags );
		
		return model;
	}
}
