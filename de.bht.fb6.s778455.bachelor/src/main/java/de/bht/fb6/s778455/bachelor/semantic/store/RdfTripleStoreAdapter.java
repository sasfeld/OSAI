/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.CollectionUtil;

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
    protected String ontologyBaseUri;

    /**
     * Create the adapter which access the given {@link Dataset} from the Jena TDB layer.
     * @param jenaStore
     */
    public RdfTripleStoreAdapter( final Dataset jenaStore, final File ontologyFile, final String ontologyBaseUri ) {
        this.initialize( jenaStore, ontologyFile, ontologyBaseUri );
    }

    /**
     * @param jenaStore
     */
    protected void initialize( final Dataset jenaStore, final File ontologyFile, final String ontologyBaseUri ) {
        if ( null == jenaStore || null == ontologyFile || ! ontologyFile.exists() ) {
            throw new IllegalArgumentException( "Null values are not allowed for arguments or the ontology file doesn't exist!" );
        }
        
        this.jenaStore = jenaStore;        
        this.inTransaction = false;
        this.wasCommited = false;
        this.lastMode = null;
        this.ontologyFile = ontologyFile;    
        this.ontologyBaseUri = ontologyBaseUri;
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
            try {
                final FileInputStream fIn = new FileInputStream( this.ontologyFile );
                this.ontologyModel.read( fIn, this.ontologyBaseUri );             
            } catch( final FileNotFoundException e ) {
                Application.log( "Ontology file not found: " + this.ontologyFile, LogType.CRITICAL, this.getClass() );
            }           
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
    
    /**
     * Get a list of those ontologies which were imported in the OWL ontology.
     * @return
     */
    public Set<String> getImportedOntologies() {
        return this.getOntologyModel().listImportedOntologyURIs();
    }
    
    /**
     * Get the set of {@link OntClass}
     * @return
     */
    public Set< OntClass > getOntologieClasses() {       
        final ExtendedIterator< OntClass > it = this.getOntologyModel().listClasses();
        return CollectionUtil.buildSetFromIterator( it );
    }
    
    /**
     * Get the set of {@link ObjectProperty}
     * @return
     */
    public Set< ObjectProperty > getOntologieObjectProperties() {
        final ExtendedIterator<ObjectProperty> it = this.getOntologyModel().listObjectProperties();
        return CollectionUtil.buildSetFromIterator( it );
    }
    
    /**
     * Get the set of {@link DatatypeProperty}
     * @return
     */
    public Set< DatatypeProperty > getOntologieDatatypeProperties() {
        final ExtendedIterator<DatatypeProperty> it = this.getOntologyModel().listDatatypeProperties();
        return CollectionUtil.buildSetFromIterator( it );
    }
    
    
}
