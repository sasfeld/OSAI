/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store;

import java.io.File;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * <p>This class realizes the adapter to access the RdfTripleStore from the Jena library.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 *
 */
public class RdfTripleStoreAdapter {
    protected Dataset jenaStore;
    protected boolean inTransaction;
    protected boolean wasCommited;
    protected ReadWrite lastMode;
    protected OntModel ontologyModel;
    protected File ontologyFile;

    /**
     * Create the adapter which access the given {@link Dataset} from the Jena TDB layer.
     * @param jenaStore
     */
    public RdfTripleStoreAdapter( final Dataset jenaStore, final File ontologyFile ) {
        this.initialize( jenaStore, ontologyFile );
    }

    /**
     * @param jenaStore
     */
    protected void initialize( final Dataset jenaStore, final File ontologyFile ) {
        if ( null == jenaStore || null == ontologyFile ) {
            throw new IllegalArgumentException( "Null values are not allowed for arguments!" );
        }
        
        this.jenaStore = jenaStore;        
        this.inTransaction = false;
        this.wasCommited = false;
        this.lastMode = null;
        this.ontologyFile = ontologyFile;        
    }
    
    /**
     * Execute a SPARQL insert command.
     * @param sparqlComannd
     */
    public void executeInsert( final String sparqlComannd ) {
        this.beginTransaction( ReadWrite.WRITE );
        
        try {
            
        } finally {
            this.endTransaction();
        }
    }
    
    protected OntModel getOntologyModel() {
        if ( null == this.ontologyModel ) {     
            // create default ontology model: OWL full language, in-memory storage, RDFS inference
            this.ontologyModel = ModelFactory.createOntologyModel();
            this.ontologyModel.read( this.ontologyFile.getAbsolutePath() );
        }
        
        return this.ontologyModel;
    }
    
    /**
     * Start a transaction with the given {@link ReadWrite} mode.
     * @param mode
     */
    protected void beginTransaction( final ReadWrite mode) {
        if ( this.inTransaction ) {
            throw new IllegalStateException( "There's already an active transaction!" );
        }
        
        this.jenaStore.begin( mode );
        this.inTransaction = true;
        this.lastMode = mode;
    }
    
    /**
     * End the last transaction.
     */
    protected void endTransaction() {
        if ( ! this.inTransaction ) {
            throw new IllegalStateException( "There's no active transaction!" );
        }
        if ( null != this.lastMode && ReadWrite.WRITE == this.lastMode && (! this.wasCommited) ) {
            throw new IllegalStateException( "The transaction was ended without commit!" );
        }
        
        this.jenaStore.end();
        this.inTransaction = false;
        this.lastMode = null;
    }
    
    /**
     * Commit the changes in the last transaction.
     */
    protected void commitTransaction() {
        if ( ! this.inTransaction ) {
            throw new IllegalStateException( "There's no active transaction!" );
        }
        if ( this.wasCommited ) {
            throw new IllegalStateException( "The commit was already done!" );
        }
        
        this.jenaStore.commit();
        this.wasCommited = true;
    }
    
    public String showOntologyTriples() {
        final StringBuilder b = new StringBuilder();
        
        final OntModel ontM = this.getOntologyModel();
        
        // imported ontologies
        for(  final String importedOnt : ontM.listImportedOntologyURIs() ) {
            b.append( "Imported ontology: " + importedOnt + "\n" );
        }
        
        return b.toString();
    }
}
