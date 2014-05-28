/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.model.tools;

import java.util.Comparator;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;

/**
 * <p>
 * This comparator compares two {@link AUserContribution}.<br />
 * 
 * The method compare will return 1, if the left {@link AUserContribution} has an ID that is greater than the one of the right handside.<br />
 * If the ID of the {@link AUserContribution} on the right handside is greater, than the method will return -1.<br />
 * If both are equal, the method will return 0.<br />
 * Use this comparator implementation in collection sortings.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.05.2014
 * 
 */
public class IdComparator implements Comparator<Course> {

	@Override
	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Course leftInstance, Course rightInstance) {
		if (leftInstance.getId() > rightInstance.getId()) {
			return 1;
		}
		if (leftInstance.getId() < rightInstance.getId()) {
			return -1;
		}
		
		// both ids are equal
		return 0;
	}

}
