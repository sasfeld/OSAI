/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.controller;

import java.util.ArrayList;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;

/**
 * <p>
 * This controller handles the pipeline for the SemanticCreation module..
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 * 
 */
public class SemanticCreationController {
    private List< ACreationStrategy > creationStrategies;
    private final boolean printInfo;

    /**
     * Create a new controller. No info will be printed.
     * 
     * @throws InvalidConfigException
     */
    public SemanticCreationController() throws InvalidConfigException {
        this.initializeStrategies();

        this.printInfo = false;
    }

    /**
     * Create a new controller. Set the printInfo flag.
     * 
     * @param printInfo
     *            set to true if you desire to print the working status on
     *            stdout.
     * @throws InvalidConfigException
     */
    public SemanticCreationController( final boolean printInfo )
            throws InvalidConfigException {
        this.initializeStrategies();

        this.printInfo = printInfo;
    }

    /**
     * Can be overwritten by unit tests.
     * 
     * @return
     */
    protected ServiceFactory getServiceFactory() {
        return ServiceFactory.getInstance();
    }

    /**
     * Initialize the configured {@link ACreationStrategy}.
     * 
     * @throws InvalidConfigException
     */
    @SuppressWarnings( "static-access" )
    private void initializeStrategies() throws InvalidConfigException {
        this.creationStrategies = new ArrayList<>();
        final List< String > creationChain = this
                .getServiceFactory()
                .getConfigReader()
                .fetchMultipleValues(
                        IConfigKeys.SEMANTICS_CREATION_STRATEGY_CHAIN );

        for( final String creationStrategy : creationChain ) {
            if( this.printInfo ) {
                System.out.println( "Trying to initialize configured class for " + creationStrategy );
            }
            
            final ACreationStrategy strat = this
                    .getServiceFactory()
                    .getConfigReader()
                    .getConfiguredClass(
                            this.getServiceFactory().getConfigReader()
                                    .fetchValue( creationStrategy ) );
            
            if( this.printInfo ) {
                System.out.println( "Created class " + strat.getClass() );
            }
            
            this.creationStrategies.add( strat );
        }
    }

    /**
     * Perform the creation of the semantic network.
     */
    public void performSemanticCreation( final LmsCourseSet inputCourseSet ) {
        for( final ACreationStrategy creationStrategy : this.creationStrategies ) {
            if( this.printInfo ) {
               System.out.println( "Performing semantic creation with strategy " + creationStrategy.getClass() );
            }            
            
            try {
                creationStrategy.createRdfTriples( inputCourseSet );
            } catch( final GeneralLoggingException e ) {
               System.err.println( "Semantic creation failed: " + e.getMessage() );
            }
            
            if ( this.printInfo ) {
                System.out.println( "Semantic creation was succesful!" );
            }
        }
    }
}
