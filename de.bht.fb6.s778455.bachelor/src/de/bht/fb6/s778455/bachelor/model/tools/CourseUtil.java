/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;

/**
 * <p>
 * This static class offers common util methods to work with {@link Course}
 * instances or collections.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class CourseUtil {

	/**
	 * Get a {@link Set} of distinct {@link Tag} instances so that the set
	 * doesn't contain a duplication of {@link Tag} instances which have the
	 * same value and the same URI. This method is helpful if you want to get an
	 * overview of topics.
	 * 
	 * @param courses
	 * @param tagType
	 * @return
	 */
	public static Set< Tag > getDistinctTags(
			final Collection< Course > courses, TagType tagType ) {
		Set< Tag > distinctTags = new HashSet< Tag >();
		for( Course course : courses ) {
			for( Board board : course.getBoards() ) {
				for( BoardThread thread : board.getBoardThreads() ) {
					for( Posting posting : thread.getPostings() ) {
						List< Tag > tags = posting.getTags( tagType );

						if( null != tags ) {
							// check equality of posting's tags and the already
							// collected tags
							for( Tag tag : tags ) {
								boolean found = false;
								for( Tag distinctTag : distinctTags ) {
									if( tag.getUri().equals(
											distinctTag.getUri() )
											&& tag.getValue().equals(
													distinctTag.getValue() ) ) {
										found = true;
									}
								}

								if( !found ) {
									distinctTags.add( tag );
								}

							}
						}
					}
				}
			}
		}
		
		return distinctTags;
	}
}
