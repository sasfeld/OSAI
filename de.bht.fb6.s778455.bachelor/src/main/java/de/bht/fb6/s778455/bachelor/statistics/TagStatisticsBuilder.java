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
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
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

	public TagStatisticsBuilder( final AStatisticsBuilder builder ) {
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
    @SuppressWarnings( "unused" )
	public StatisticsModel buildStatistics( final LmsCourseSet courses ) {
		final StatisticsModel model = super.buildStatistics( courses );

		/*
		 * number of tags in general
		 */
		int numberTopicZoomTags = 0;
		int numberNerTags = 0;
		int numberPosTags = 0;
		int numberTags = 0;

		/*
		 * number of tags belonging to course, board or postings.
		 */
		final int numberTagsForPostings = 0;
		final List< Tag > distinctTagsForPostings = new ArrayList< Tag >();
		final int numberTagsForBoards = 0;
		final List< Tag > distinctTagsForBoards = new ArrayList< Tag >();
		final int numberTagsForCourses = 0;
		final List< Tag > distinctTagsForCourses = new ArrayList< Tag >();

		/*
		 * number of postings with tags
		 */
		int numberPostingsTzTags = 0;
		int numberPostingsNerTags = 0;
		int numberPostingsPosTags = 0;
		int numberPostingsTags = 0;
		int numberPostingsTzAndNerTags = 0;

		/*
		 * number of courses with tags
		 */
		int numberCoursesTzTags = 0;
		int numberCoursesNerTags = 0;
		int numberCoursesPosTags = 0;
		int numberCoursesTags = 0;
		int numberCoursesTzAndNerTags = 0;

		/*
		 * number of boards with tags
		 */
		int numberBoardsTzTags = 0;
		int numberBoardsNerTags = 0;
		int numberBoardsPosTags = 0;
		int numberBoardsTags = 0;
		int numberBoardsTzAndNerTags = 0;

		final List< Posting > untaggedPostings = new ArrayList< Posting >();

		for( final Course course : courses ) {
			// TopicZoomTags
			final List< Tag > topicTags = course.getTags( TagType.TOPIC_ZOOM );
			if( course.isTopicZoomTagged() ) {
				numberTopicZoomTags += topicTags.size();
				numberCoursesTzTags++;
			}

			// NER tags
			final List< Tag > nerTags = course.getTags( TagType.NER_TAG );
			if( course.isNerTagged() ) {
				numberNerTags += nerTags.size();
				numberCoursesNerTags++;
			}
			
			// Pos tags
			final List< Tag > posTags = course.getTags( TagType.POS_TAG );
			if( course.isPosTagged() ) {
			    numberPosTags += posTags.size();
			    numberCoursesPosTags++;
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
				if( null != posTags ) {
				    distinctTagsForCourses.addAll( posTags );
                }
			}

			// intersection stats
			if( course.isNerTagged() && course.isTopicZoomTagged() ) {
				numberCoursesTzAndNerTags += 1;
			}

			for( final Board board : course.getBoards() ) {
				// TopicZoomTags
				final List< Tag > ctopicTags = board.getTags( TagType.TOPIC_ZOOM );
				if( board.isTopicZoomTagged() ) {
					numberTopicZoomTags += board.getTags( TagType.TOPIC_ZOOM )
							.size();
					numberBoardsTzTags++;
				}

				// NER tags
				final List< Tag > cnerTags = board.getTags( TagType.NER_TAG );
				if( board.isNerTagged() ) {
					numberNerTags += board.getTags( TagType.NER_TAG ).size();
					numberBoardsNerTags++;
				}
				
				// Pos tags
	            final List< Tag > cposTags = board.getTags( TagType.POS_TAG );
	            if( course.isPosTagged() ) {
	                numberPosTags += cposTags.size();
	                numberBoardsPosTags++;
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
								.getTags( TagType.NER_TAG ) );
					}					
					if( null != cposTags ) {
					    distinctTagsForBoards.addAll( board
					            .getTags( TagType.POS_TAG ) );
					}
					
				}

				// intersection stats
				if( board.isNerTagged() && board.isTopicZoomTagged() ) {
					numberBoardsTzAndNerTags += 1;
				}

				for( final BoardThread thread : board.getBoardThreads() ) {
					for( final Posting posting : thread.getPostings() ) {
						// TopicZoomTags
						final List< Tag > ptopicTags = posting.getTags( TagType.TOPIC_ZOOM );
						if( posting.isTopicZoomTagged() ) {
							numberTopicZoomTags += posting.getTags(
									TagType.TOPIC_ZOOM ).size();
							numberPostingsTzTags++;
						}

						// NER tags
						final List< Tag > pnerTags = posting.getTags( TagType.NER_TAG );
						if( posting.isNerTagged() ) {
							numberNerTags += posting.getTags( TagType.NER_TAG )
									.size();
							numberPostingsNerTags++;
						}
						
						// Pos tags
		                final List< Tag > pposTags = posting.getTags( TagType.POS_TAG );
		                if( posting.isPosTagged() ) {
		                    numberPosTags += pposTags.size();
		                    numberPostingsPosTags++;
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
										.getTags( TagType.NER_TAG ) );
							}
							if( null != pposTags ) {
							    distinctTagsForPostings.addAll( posting
		                                .getTags( TagType.POS_TAG ) );
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
		final int numberDistinctTzTags = this.getDistinctTags( courses,
				TagType.TOPIC_ZOOM );
		final int numberDistinctNerTags = this.getDistinctTags( courses,
				TagType.NER_TAG );
		final int numberDistinctPosTags = this.getDistinctTags( courses,
		        TagType.POS_TAG );

		model.setNumberTopicZoomTaggedPostings( numberPostingsTzTags )
				.setNumberNerTaggedPostings( numberPostingsNerTags )
				.setNumberPosTaggedPostings( numberPostingsPosTags )
				.setNumberTaggedPostings( numberPostingsTags )
				.setNumberTzAndNerTaggedPostings( numberPostingsTzAndNerTags )
				.setNumberTopicZoomTags( numberTopicZoomTags )
				.setNumberNerTags( numberNerTags )
				.setNumberPosTags( numberPosTags ) 
				.setNumberTags( numberTags )
				.setNumberDistinctTopicZoomTags( numberDistinctTzTags )
				.setNumberDistinctNerTags( numberDistinctNerTags )
				.setNumberDistinctPosTags ( numberDistinctPosTags )
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
		final Set< Tag > distinctTags = CourseUtil.getDistinctTags( courses, tagType );

		return distinctTags.size();
	}
}
