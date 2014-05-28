/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model.tools;

import java.util.Comparator;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Posting;

/**
 * <p>This is a date comparator for all {@link AUserContribution}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 *
 */
public class DateComparator implements Comparator< AUserContribution > {

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare( AUserContribution o1, AUserContribution o2 ) {
		if( null == o1.getCreationDate()
				|| null == o2.getCreationDate() ) {
			// compare modification date
			if( null == o1.getModificationDate() || null == o2.getModificationDate()) {
				// fallback: if the contribution is a thread, try to get the dates from the posting
				if ( o1 instanceof BoardThread && o2 instanceof BoardThread ) {
					BoardThread b1 = (BoardThread) o1;
					BoardThread b2 = (BoardThread) o2;
					
					Posting p1 = b1.getPostings().get(0);
					Posting p2 = b2.getPostings().get(0);
					// try modification date first
					if (p1.getModificationDate() != null && p2.getModificationDate() != null
							&& (p1.getModificationDate().getTime() < p2.getModificationDate().getTime())) {
						return -1;
					}
					if (p1.getModificationDate() != null && p2.getModificationDate() != null
							&& (p1.getModificationDate().getTime() > p2.getModificationDate().getTime())) {
						return 1;
					}
					
					if (p1.getCreationDate() != null && p2.getCreationDate() != null
							&& (p1.getCreationDate().getTime() < p2.getCreationDate().getTime())) {
						return -1;
					}
					if (p1.getCreationDate() != null && p2.getCreationDate() != null
							&& (p1.getCreationDate().getTime() > p2.getCreationDate().getTime())) {
						return 1;
					}
					
					return 0;
				}
				
				return 0; // no  comparison possible
			}
			else {
				if( o1.getModificationDate().getTime() < o2.getModificationDate()
						.getTime() ) {
					return -1;
				}
				if( o1.getModificationDate().getTime() > o2.getModificationDate()
						.getTime() ) {
					return 1;
				}
				return 0;
			}
		} else { 
			// compare creation date otherwise
			if( o1.getCreationDate().getTime() < o2.getCreationDate()
					.getTime() ) {
				return -1;
			}
			if( o1.getCreationDate().getTime() > o2.getCreationDate()
					.getTime() ) {
				return 1;
			}
			return 0; // o1 == o2
		}
	}

}
