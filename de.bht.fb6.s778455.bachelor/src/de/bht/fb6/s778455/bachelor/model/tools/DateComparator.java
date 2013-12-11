/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model.tools;

import java.util.Comparator;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;

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
