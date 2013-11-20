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
 * <p>This class represents a Thread of a board, e.g. a 'Moodle board'.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class BoardThread extends AUserContribution {
	protected List< Posting > postings;
	
	public BoardThread() {
		this.postings = new ArrayList< Posting >(); 
	}
	
	/**
	 * Add a posting to the board thread.
	 * The Posting will be sorted immediatly.
	 * @param p
	 */
	public void addPosting(Posting p) {
		Comparator< Posting > dateComparator = new Comparator< Posting >() {			
			@Override
			/**
			 * Compare the two postings. A posting is less than another, if the creation date is less than the other.
			 * @param o1
			 * @param o2
			 * @return 
			 */
			public int compare( Posting o1, Posting o2 ) {
				if (o1.getCreationDate().getTime() < o2.getCreationDate().getTime()) {
					return -1;
				}
				if (o1.getCreationDate().getTime() > o2.getCreationDate().getTime()) {
					return 1;
				}
				return 0; // o1 == o2
			}
		};
		postings.add( p );
		// sort ascending
		Collections.sort( this.postings, dateComparator );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BoardThread [creationDate="
				+ creationDate + ", creator=" + creator + ", title=" + title
				+ "]";
	}

	/**
	 * Get a sorted list of {@link Posting}.
	 * {@link Posting} are sorted by the creation date.
	 * @return
	 */
	public List< Posting > getPostings() {
		return this.postings;
	}
}
