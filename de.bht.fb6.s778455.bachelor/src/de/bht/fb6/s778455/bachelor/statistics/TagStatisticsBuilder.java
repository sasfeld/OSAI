/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.tools.CourseUtil;

/**
 * <p>
 * This {@link AStatisticsBuilder} fills a {@link StatisticsModel} with
 * information about the tags in {@link Posting} models.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class TagStatisticsBuilder extends DecoratingStatisticsBuilder {

	public TagStatisticsBuilder() {
		super();
	}

	public TagStatisticsBuilder( AStatisticsBuilder builder ) {
		super( builder );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder#buildStatistics
	 * (java.util.Collection)
	 */
	@SuppressWarnings( "unused" )
	public StatisticsModel buildStatistics( final Collection< Course > courses ) {
		final StatisticsModel model = super.buildStatistics( courses );

		/*
		 * number of tags in general
		 */
		int numberTopicZoomTags = 0;
		int numberNerTags = 0;
		int numberTags = 0;

		/*
		 * number of tags belonging to course, board or postings.
		 */
		int numberTagsForPostings = 0;
		List< Tag > distinctTagsForPostings = new ArrayList< Tag >();
		int numberTagsForBoards = 0;
		List< Tag > distinctTagsForBoards = new ArrayList< Tag >();
		int numberTagsForCourses = 0;
		List< Tag > distinctTagsForCourses = new ArrayList< Tag >();

		/*
		 * number of postings with tags
		 */
		int numberPostingsTzTags = 0;
		int numberPostingsNerTags = 0;
		int numberPostingsTags = 0;
		int numberPostingsTzAndNerTags = 0;

		/*
		 * number of courses with tags
		 */
		int numberCoursesTzTags = 0;
		int numberCoursesNerTags = 0;
		int numberCoursesTags = 0;
		int numberCoursesTzAndNerTags = 0;

		/*
		 * number of boards with tags
		 */
		int numberBoardsTzTags = 0;
		int numberBoardsNerTags = 0;
		int numberBoardsTags = 0;
		int numberBoardsTzAndNerTags = 0;

		List< Posting > untaggedPostings = new ArrayList< Posting >();

		for( Course course : courses ) {
			// TopicZoomTags
			List< Tag > topicTags = course.getTags( TagType.TOPIC_ZOOM );
			if( course.isTopicZoomTagged() ) {
				numberTopicZoomTags += topicTags.size();
				numberCoursesTzTags++;
			}

			// NER tags
			List< Tag > nerTags = course.getTags( TagType.NER_TAGS );
			if( course.isNerTagged() ) {
				numberNerTags += nerTags.size();
				numberCoursesNerTags++;
			}

			// general tags
			if( course.isTagged() ) {
				numberTags += course.getNumberTags();
				numberCoursesTags++;

				if( null != topicTags ) {
					distinctTagsForCourses.addAll( topicTags );
				}
				if( null != nerTags ) {
					distinctTagsForCourses.addAll( nerTags );
				}
			}

			// intersection stats
			if( course.isNerTagged() && course.isTopicZoomTagged() ) {
				numberCoursesTzAndNerTags += 1;
			}

			for( Board board : course.getBoards() ) {
				// TopicZoomTags
				List< Tag > ctopicTags = board.getTags( TagType.TOPIC_ZOOM );
				if( board.isTopicZoomTagged() ) {
					numberTopicZoomTags += board.getTags( TagType.TOPIC_ZOOM )
							.size();
					numberBoardsTzTags++;
				}

				// NER tags
				List< Tag > cnerTags = board.getTags( TagType.NER_TAGS );
				if( board.isNerTagged() ) {
					numberNerTags += board.getTags( TagType.NER_TAGS ).size();
					numberBoardsNerTags++;
				}

				// general tags
				if( board.isTagged() ) {
					numberTags += board.getNumberTags();
					numberBoardsTags++;

					if( null != ctopicTags ) {
						distinctTagsForBoards.addAll( board
								.getTags( TagType.TOPIC_ZOOM ) );
					}
					if( null != cnerTags ) {
						distinctTagsForBoards.addAll( board
								.getTags( TagType.NER_TAGS ) );
					}
				}

				// intersection stats
				if( board.isNerTagged() && board.isTopicZoomTagged() ) {
					numberBoardsTzAndNerTags += 1;
				}

				for( BoardThread thread : board.getBoardThreads() ) {
					for( Posting posting : thread.getPostings() ) {
						// TopicZoomTags
						List< Tag > ptopicTags = posting.getTags( TagType.TOPIC_ZOOM );
						if( posting.isTopicZoomTagged() ) {
							numberTopicZoomTags += posting.getTags(
									TagType.TOPIC_ZOOM ).size();
							numberPostingsTzTags++;
						}

						// NER tags
						List< Tag > pnerTags = posting.getTags( TagType.NER_TAGS );
						if( posting.isNerTagged() ) {
							numberNerTags += posting.getTags( TagType.NER_TAGS )
									.size();
							numberPostingsNerTags++;
						}

						// general tags
						if( posting.isTagged() ) {
							numberTags += posting.getNumberTags();
							numberPostingsTags++;

							if( null != ptopicTags ) {
								distinctTagsForPostings.addAll( posting
										.getTags( TagType.TOPIC_ZOOM ) );
							}
							if( null != pnerTags ) {
								distinctTagsForPostings.addAll( posting
										.getTags( TagType.NER_TAGS ) );
							}
						} else { // add posting to untagged list
							untaggedPostings.add( posting );
						}

						// intersection stats
						if( posting.isNerTagged()
								&& posting.isTopicZoomTagged() ) {
							numberPostingsTzAndNerTags += 1;
						}
					}
				}
			}
		}

		// fetch number of distinct tags (rdfId and value are equal)
		int numberDistinctTzTags = this.getDistinctTags( courses,
				TagType.TOPIC_ZOOM );
		int numberDistinctNerTags = this.getDistinctTags( courses,
				TagType.NER_TAGS );

		model.setNumberTopicZoomTaggedPostings( numberPostingsTzTags )
				.setNumberNerTaggedPostings( numberPostingsNerTags )
				.setNumberTaggedPostings( numberPostingsTags )
				.setNumberTzAndNerTaggedPostings( numberPostingsTzAndNerTags )
				.setNumberTopicZoomTags( numberTopicZoomTags )
				.setNumberNerTags( numberNerTags )
				.setNumberTags( numberTags )
				.setNumberDistinctTopicZoomTags( numberDistinctTzTags )
				.setNumberDistinctNerTags( numberDistinctNerTags )
				.setUntaggedPostings( untaggedPostings )
				.setNumberTaggedCourses( numberCoursesTags )
				.setNumberTaggedBoards( numberBoardsTags )
				.setNumberTaggedPostings( numberPostingsTags )
				.setDistinctNumberCourseTags(
						CourseUtil.getDistinctTags( distinctTagsForCourses )
								.size() )
				.setDistinctNumberBoardTags(
						CourseUtil.getDistinctTags( distinctTagsForBoards )
								.size() )
				.setDistinctNumberPostingTags(
						CourseUtil.getDistinctTags( distinctTagsForPostings )
								.size() );

		return model;
	}

	/**
	 * Get the number of distinct tags, so that equal tags (the URL and the
	 * value of a tag are equal) are only counted once.
	 * 
	 * @param courses
	 * @param topicZoom
	 * @return
	 */
	private int getDistinctTags( final Collection< Course > courses,
			final TagType tagType ) {
		Set< Tag > distinctTags = CourseUtil.getDistinctTags( courses, tagType );

		return distinctTags.size();
	}
}
