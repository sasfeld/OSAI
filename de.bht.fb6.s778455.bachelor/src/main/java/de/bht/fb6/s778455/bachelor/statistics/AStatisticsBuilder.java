/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.statistics;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;


/**
 * <p>The statistics builder follows the decorator pattern.</p>
 * <p>It's possible to decorate several DecoratingStatisticsBuilder.<br />
 * The central API for all statistics builder is contained here.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 *
 */
public abstract class AStatisticsBuilder {
	protected StatisticsModel model;
	
	/**
	 * @return the model
	 */
	protected final StatisticsModel getModel() {
		return this.model;
	}

	/**
	 * @param model the model to set
	 */
	protected final void setModel( final StatisticsModel model ) {
		this.model = model;
	}
	
	/**
	 * Build a {@link StatisticsModel} for a given collection of {@link Course}.
	 * @param courses
	 * @return a fresh {@link StatisticsModel}
	 */
	public abstract StatisticsModel buildStatistics(final LmsCourseSet courses);
}
