/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model.tools;

import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;

/**
 * <p>
 * This class realizes a comparator for sorting the {@link TopicZoomTag}.
 * </p>
 * <p>
 * An important criteria for TopicZoom tags is the significance.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 * 
 */
public class TopicZoomTagComparator extends TagComparator {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare( final Tag leftTag, final Tag rightTag ) {
		TopicZoomTag leftTopicTag;
		TopicZoomTag rightTopicTag;
		// try to cast to TopicZoom Tag
		if( leftTag instanceof TopicZoomTag ) {
			leftTopicTag = ( TopicZoomTag ) leftTag;
		} else {
			throw new IllegalArgumentException(
					getClass()
							+ "The given instance for the parameter 'leftTag' is not of the subtype TopicZoomTag!" );
		}

		// cast of tag on the right handside
		if( rightTag instanceof TopicZoomTag ) {
			rightTopicTag = ( TopicZoomTag ) rightTag;
		} else {
			throw new IllegalArgumentException(
					getClass()
							+ "The given instance for the parameter 'rightTag' is not of the subtype TopicZoomTag!" );
		}

		// primary sorting criteria is the weight in the superclass
		int primarySorting = super.compare( leftTag, rightTag );

		// secondary sorting criteria is the significance
		int secondarySorting = 0;
		if( leftTopicTag.getSignificance() < rightTopicTag.getSignificance() ) {
			secondarySorting = -1;
		} else if( leftTopicTag.getSignificance() > rightTopicTag
				.getSignificance() ) {
			secondarySorting = 1;
		} // else: both are equal: value will be 0

		// combine sorting criterias
		if ( (-1 ==  primarySorting && -1 == secondarySorting) || 
				(-1 == primarySorting && 0 == secondarySorting) ||
				(0 == primarySorting && -1 == secondarySorting) ||
				(1 == primarySorting && -1 == secondarySorting)) {
			return -1;
		} else if ( (-1 == primarySorting && 1 == secondarySorting ) || 
				(0 == primarySorting && 0 == secondarySorting) ||
				(1 == primarySorting && 0 == secondarySorting)) {
			return 0;
		} else { // prim == 1 && sec == 1 || prim == 0 && sec == 1 
			return 1;
		}
		
	}
}
