/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom;

import java.util.Comparator;

import de.bht.fb6.s778455.bachelor.model.Tag;

/**
 * <p>This class realizes a comparator for sorting {@link Tag} models.</p>
 * <p>The first sorting criteria is the weight of the tag.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 *
 */
public class TagComparator implements Comparator< Tag > {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare( final Tag leftTag, final Tag rightTag ) {
		if (leftTag.getWeight() < rightTag.getWeight()) {
			return -1; 
		} else if (leftTag.getWeight() > rightTag.getWeight()) {
			return 1;
		} else { // both weights are equal
			return 0;
		}
	}

}
