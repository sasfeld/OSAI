/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.statistics;

import java.util.Date;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;

/**
 * <p>
 * This class is super class for all {@link AStatisticsBuilder} which can be
 * decorated.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public abstract class DecoratingStatisticsBuilder extends AStatisticsBuilder {
    protected AStatisticsBuilder decoratedStrategy;

    /**
     * Create a new statistics builder handing in a strategy.
     * 
     * @param decoratedStrategy
     */
    public DecoratingStatisticsBuilder(
            final AStatisticsBuilder decoratedStrategy ) {
        this.decoratedStrategy = decoratedStrategy;
    }

    /**
     * Create a statistics builder which is not decorated.
     */
    public DecoratingStatisticsBuilder() {
        this.decoratedStrategy = null;
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder#buildStatistics
     * (java.util.Collection)
     */
    public StatisticsModel buildStatistics( final LmsCourseSet courses ) {
        // just pass the model to the decorating strategies.
        if( null == this.getModel() ) {
            final StatisticsModel newModel = new StatisticsModel();
            newModel.setDateCreation( new Date() ); // current datetime
            this.setModel( newModel );
        }

        if( null != this.decoratedStrategy ) {
            this.decoratedStrategy.setModel( this.getModel() );
            // let the decorated strategy and its decorated strategies do their
            // work
            this.decoratedStrategy.buildStatistics( courses );
        }

        return this.getModel();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder#getStringRepresentation()
     */
    public String getStringRepresentation() {
        if( null == this.getModel() ) {
            throw new IllegalStateException( "You need to call buildStatistics() before!" );
        }
        
        final StringBuilder builder = new StringBuilder();
        // delegate to decorated strategies
        if( null != this.decoratedStrategy ) {
            builder.append( this.decoratedStrategy.getStringRepresentation() );
        }
        
        return builder.toString();
    }

}
