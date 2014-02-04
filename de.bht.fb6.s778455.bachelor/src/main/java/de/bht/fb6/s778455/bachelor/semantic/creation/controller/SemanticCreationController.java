/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreException;

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
    private RdfTripleStoreAdapter rdfStoreAdapter;
    private URL releasedVersionUri;

    /**
     * Create a new controller. No info will be printed.
     * 
     * @throws InvalidConfigException
     */
    public SemanticCreationController() throws InvalidConfigException {
        this.initializeStrategies();
        this.initializeRdfStore();

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
        this.initializeRdfStore();

        this.printInfo = printInfo;
    }

    /**
     * Intialize the {@link RdfTripleStoreAdapter}
     */
    private void initializeRdfStore() {
        this.rdfStoreAdapter = ServiceFactory.getJenaStoreAdapter();
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
                System.out
                        .println( "Trying to initialize configured class for "
                                + creationStrategy );
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
        Model enrichedModel = null;
        for( final ACreationStrategy creationStrategy : this.creationStrategies ) {
            if( this.printInfo ) {
                System.out
                        .println( "Performing semantic creation with strategy "
                                + creationStrategy.getClass() );
            }

            try {
                creationStrategy.createRdfTriples( inputCourseSet );
                enrichedModel = creationStrategy.getEnrichedModel();
            } catch( final GeneralLoggingException e ) {
                System.err.println( "Semantic creation failed: "
                        + e.getMessage() );
            }

            if( this.printInfo ) {
                System.out.println( "Semantic creation was succesful!" );
            }

        }

        // release the ontology model which the creation strategy filled
        if( null != enrichedModel ) {
            try {
                this.rdfStoreAdapter.releaseModel( enrichedModel, false );
                this.releasedVersionUri = this.rdfStoreAdapter.getCurrentVersionUri();                
            } catch( final RdfTripleStoreException e ) {
                System.err
                        .println( "Releasing the enriched model to the Jena triple store failed: "
                                + e.getMessage() );
            }
        } else {
            System.err.println( "Realeasing the enriched model failed: no model instance given." );
        }
    }
    
    /**
     * Get the name (URI) of the named graph for the released model.
     * @return
     */
    public String getUriForReleasedModel() {
        if ( null == this.releasedVersionUri ) {
            throw new IllegalStateException( "You need to perform the creation before calling this method!" );
        }
        return this.releasedVersionUri.toExternalForm();
    }
}
