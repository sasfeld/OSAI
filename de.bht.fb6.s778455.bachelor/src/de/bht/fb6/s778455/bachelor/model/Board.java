/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * <p>This class defines a model for a Board. A Board is included in a {@link Course}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class Board extends AUserContribution {
	protected Course course;
	protected List< BoardThread > boardThreads;
	
	/**
	 * Create a new Board. A Board is included in the given course.
	 * @param course
	 */
	public Board( Course course) {
		this.course = course;
		
		this.boardThreads = new ArrayList<BoardThread>();
	}

	/**
	 * Add a {@link BoardThread}. Sort it immediatly.
	 * @param boardThread
	 */
	public void addThread( BoardThread boardThread ) {
		Comparator< BoardThread > dateComparator = new Comparator< BoardThread >() {			
			@Override
			/**
			 * Compare the two board threads. A board thread is less than another, if the creation date is less than the other.
			 * @param o1
			 * @param o2
			 * @return 
			 */
			public int compare( BoardThread o1, BoardThread o2 ) {
				if (o1.getCreationDate().getTime() < o2.getCreationDate().getTime()) {
					return -1;
				}
				if (o1.getCreationDate().getTime() > o2.getCreationDate().getTime()) {
					return 1;
				}
				return 0; // o1 == o2
			}
		};
		this.boardThreads.add( boardThread );
		// sort ascending
		Collections.sort( this.boardThreads, dateComparator );		
	}
	
	/**
	 * Get a sorted list of {@link BoardThread}.
	 * {@link BoardThread} are sorted by the creation date.
	 * @return
	 */
	public List< BoardThread > getBoardThreads() {
		return this.boardThreads;
	}
}
