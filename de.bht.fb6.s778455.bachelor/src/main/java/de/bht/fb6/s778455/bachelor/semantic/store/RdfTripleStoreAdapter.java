/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.CollectionUtil;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IUniqueGraphNames;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IUniqueProperties;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IUniqueResources;

/**
 * <p>
 * This class realizes the adapter to access the RdfTripleStore from the Jena
 * library.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 * 
 */
public class RdfTripleStoreAdapter implements IUniqueProperties,
        IUniqueResources, IUniqueGraphNames {
    protected static final String STARTING_VERSION = "Initial version";
    protected Dataset jenaStore;
    protected boolean inTransaction;
    protected boolean wasCommited;
    protected ReadWrite lastMode;
    protected OntModel ontologyModel;
    protected File ontologyFile;
    protected URL ontologyBaseUri;

    /**
     * The version to work with. The name of the named graph.
     */
    protected String workingVersion;
    /**
     * The current version of the dataset. The different graphes are accessible
     * by the name of the version (NamedGraphes). The current version will be
     * stored in the default model. Format: Vdd_mm_YYYY_incrementNumber
     */
    protected String currentVersion;
    private List< String > errors;

    /**
     * Create the adapter which access the given {@link Dataset} from the Jena
     * TDB layer.
     * 
     * @param jenaStore
     */
    public RdfTripleStoreAdapter( final Dataset jenaStore,
            final File ontologyFile, final String ontologyBaseUri ) {
        this.initialize( jenaStore, ontologyFile, ontologyBaseUri );
    }

    /**
     * @param jenaStore
     */
    protected void initialize( final Dataset jenaStore,
            final File ontologyFile, final String ontologyBaseUri ) {
        if( null == jenaStore || null == ontologyFile || !ontologyFile.exists()
                || null == ontologyBaseUri ) {
            throw new IllegalArgumentException(
                    "Null values are not allowed for arguments or the ontology file doesn't exist!" );
        }
        // check ontology base uri
        try {
            this.ontologyBaseUri = new URL( ontologyBaseUri );
        } catch( final MalformedURLException e ) {
            throw new IllegalArgumentException(
                    "The ontologyBaseUri is not a valid URI!" );
        }

        this.jenaStore = jenaStore;
        this.inTransaction = false;
        this.wasCommited = false;
        this.lastMode = null;
        this.ontologyFile = ontologyFile;
        this.errors = new ArrayList<>();
        this.currentVersion = STARTING_VERSION;
        this.loadCurrentVersion();
        
        // load ontology model immediatly
        this.getPureOntologyModel();
    }

    /**
     * Load the current version contained in the default model.
     */
    protected void loadCurrentVersion() {
        this.beginTransaction( ReadWrite.READ );

        try {
            if( !this.jenaStore.containsNamedModel( URI_INFO_GRAPH ) ) {
                this.currentVersion = STARTING_VERSION;
            } else {
                final Model infoM = this.jenaStore
                        .getNamedModel( URI_INFO_GRAPH );

                // does default model contain the version resource and property?
                final Resource resource = ResourceFactory
                        .createResource( URI_OWL_ONTOLOGY );
                final Property property = ResourceFactory
                        .createProperty( VERSION );

                if( infoM.contains( resource, property ) ) {
                    final NodeIterator it = infoM.listObjectsOfProperty(
                            resource, property );

                    final Set< RDFNode > nodesToRemove = CollectionUtil
                            .buildSetFromIterator( it );

                    // iteratore through
                    for( final RDFNode nodeToRemove : nodesToRemove ) {
                        // set current version
                        this.currentVersion = nodeToRemove.asLiteral()
                                .getLexicalForm();
                    }
                } else { // set starting version
                    this.currentVersion = STARTING_VERSION;
                }
            }
        } finally {
            this.endTransaction( false );
        }
    }

    protected void deleteCurrentVersion() {
        this.beginTransaction( ReadWrite.WRITE );

        try {
            if( this.jenaStore.containsNamedModel( URI_INFO_GRAPH ) ) {
                final Model defaultM = this.jenaStore
                        .getNamedModel( URI_INFO_GRAPH );

                // does default model contain the version resource and property?
                final Resource resource = ResourceFactory
                        .createResource( URI_OWL_ONTOLOGY );
                final Property property = ResourceFactory
                        .createProperty( VERSION );

                if( defaultM.contains( resource, property ) ) {
                    final NodeIterator it = defaultM.listObjectsOfProperty(
                            resource, property );

                    final Set< RDFNode > nodesToRemove = CollectionUtil
                            .buildSetFromIterator( it );

                    // remove old version triples
                    for( final RDFNode nodeToRemove : nodesToRemove ) {
                        final Statement stmToRemove = ResourceFactory
                                .createStatement( resource, property,
                                        nodeToRemove );

                        if( !defaultM.contains( stmToRemove ) ) {
                            // add to internal error list
                            this.addError( "Version statement doesn't exist in default model, but it should exist: "
                                    + stmToRemove );
                        } else {
                            defaultM.remove( stmToRemove );
                        }
                    }
                } else {
                    this.addError( "No version statement exists in the info model." );
                }
            } else {
                this.addError( "No info graph exists." );
            }
            this.commitTransaction();
        } finally {
            this.endTransaction( false );
        }
    }

    protected void addError( final String errorMsg ) {
        this.errors.add( errorMsg );
    }

    /**
     * Get the current error messages. This will flush the error list.
     * 
     * @return
     */
    public List< String > getErrors() {
        // copy list
        final List< String > errors = new ArrayList<>( this.errors );

        this.errors.clear();
        return errors;
    }

    /**
     * Execute a SPARQL insert command.
     * 
     * @param sparqlComannd
     */
    public void executeInsert( final String sparqlComannd ) {
        this.beginTransaction( ReadWrite.WRITE );

        try {

        } finally {
            this.endTransaction( false );
        }
    }

    /**
     * @return the workingVersion
     */
    public final String getWorkingVersion() {
        return this.workingVersion;
    }

    /**
     * Set the version to work with. This means, all operation done here will be
     * performed on the named model belonging to the given version.
     * 
     * @param workingVersion
     *            the workingVersion to set
     */
    public final void setWorkingVersion( final String workingVersion ) {
        if( null == workingVersion ) {
            throw new IllegalArgumentException(
                    "Null is not allowed as workingVersion!" );
        }
        if( !this.hasVersion( workingVersion ) ) {
            throw new IllegalArgumentException(
                    "A graph for the given version doesn't exist!" );
        }

        this.workingVersion = workingVersion;
    }

    /**
     * Check if the jena store has a graph for the given version.
     * 
     * @param version
     * @return
     */
    public boolean hasVersion( final String version ) {
        this.beginTransaction( ReadWrite.READ );

        try {
            final boolean exists = null != this.jenaStore
                    .getNamedModel( version );
            return exists;
        } finally {
            this.endTransaction( false );
        }
    }

    /**
     * Get a fresh {@link OntModel} from the original ontology file. That means,
     * the {@link OntModel} only contains the coneptualization and is not filled
     * with instances.
     */
    public OntModel getPureOntologyModel() {
        if( null == this.ontologyModel ) {
            // create default ontology model: OWL full language, in-memory
            // storage, RDFS inference
            this.ontologyModel = ModelFactory.createOntologyModel();
            try {
                final FileInputStream fIn = new FileInputStream(
                        this.ontologyFile );
                this.ontologyModel.read( fIn,
                        this.ontologyBaseUri.toExternalForm() );
            } catch( final FileNotFoundException e ) {
                Application.log( "Ontology file not found: "
                        + this.ontologyFile, LogType.CRITICAL, this.getClass() );
            }
        }

        return this.ontologyModel;
    }

    /**
     * Start a transaction with the given {@link ReadWrite} mode.
     * 
     * @param mode
     */
    protected void beginTransaction( final ReadWrite mode ) {
        if( this.inTransaction ) {
            throw new IllegalStateException(
                    "There's already an active transaction!" );
        }

        this.jenaStore.begin( mode );
        this.inTransaction = true;
        this.lastMode = mode;
    }

    /**
     * End the last transaction.
     * 
     * @param ignoreCommit
     *            set to true if you want to supress an illegal state exception
     *            caused by a missing commit.
     */
    protected void endTransaction( final boolean ignoreCommit ) {
        if( !this.inTransaction ) {
            throw new IllegalStateException( "There's no active transaction!" );
        }
        if( !ignoreCommit
                && ( null != this.lastMode && ReadWrite.WRITE == this.lastMode && ( !this.wasCommited ) ) ) {
            throw new IllegalStateException(
                    "The transaction was ended without commit!" );
        }

        this.jenaStore.end();
        this.inTransaction = false;
        this.lastMode = null;
        this.wasCommited = false;
    }

    /**
     * Commit the changes in the last transaction.
     */
    protected void commitTransaction() {
        if( !this.inTransaction ) {
            throw new IllegalStateException( "There's no active transaction!" );
        }
        if( this.wasCommited ) {
            throw new IllegalStateException( "The commit was already done!" );
        }

        this.jenaStore.commit();
        this.wasCommited = true;
    }

    /**
     * Release a given model. Releasing means, the given model will be added to
     * the pure/original ontology model and then a new named graph under the
     * returned version {@link String} will be added to this TDB.
     * 
     * @param newModel
     * @param integrateOntology
     *            boolean whether the model shall be added to the pure ontology
     * @return String the version (name of the named graph to which the given
     *         model was added) or null if no valid URL for the new version was generated
     * @throws Exception
     */
    public String releaseModel( final Model newModel,
            final boolean integrateOntology ) throws RdfTripleStoreException {
        this.incrementVersion( true );
        final URL versionUri = this.getCurrentVersionUri();

        if( null != versionUri ) {
            this.beginTransaction( ReadWrite.WRITE );
            try {
                // add newModel to ontModel
                if( integrateOntology ) {
                    final OntModel ontModel = this.getPureOntologyModel();
                    ontModel.add( newModel );
                    // now add ontologyModel to new named
                    this.jenaStore.addNamedModel( versionUri.toExternalForm(),
                            ontModel );
                } else {
                    this.jenaStore.addNamedModel( versionUri.toExternalForm(),
                            newModel );
                }

                // commit the transaction
                this.commitTransaction();
                return this.getCurrentVersion();
            } catch( final Exception e ) {
                throw new RdfTripleStoreException( e );
            } finally {
                this.endTransaction( true );
            }
        }
        return null;
    }

    /**
     * Get the URL to the named graph of the current version-
     * 
     * @return
     */
    private URL getCurrentVersionUri() {
        try {
            final URL newUrl = new URL( this.ontologyBaseUri.toExternalForm()
                    + "/" + this.getCurrentVersion() );
            return newUrl;
        } catch( final MalformedURLException e ) {
            this.addError( "Illegal version URI: " + e );
            return null;
        }
    }

    /**
     * Increment the current version string.
     * 
     * @return
     * @throws Exception
     */
    protected String incrementVersion( final Boolean setInGraph )
            throws RdfTripleStoreException {
        final Date d = new Date();
        String completeNo = "V_"
                + new SimpleDateFormat( "dd_MM_yyyy" ).format( d );
        int incrementNo = 0;

        // initial version
        if( !STARTING_VERSION.equals( this.currentVersion )
                && null != this.currentVersion
                && 0 != this.currentVersion.trim().length() ) {
            final String[] spl = this.currentVersion.split( "_" );

            try {
                incrementNo = Integer.parseInt( spl[spl.length - 1] ) + 1;
            } catch( final NumberFormatException e ) {
                // continue, leave incrementNo untouched
            }
        }

        completeNo += "_" + incrementNo;

        this.currentVersion = completeNo;

        if( setInGraph ) {
            // set current version in default graph
            this.setCurrentVersionInGraph();
        }

        return completeNo;
    }

    /**
     * Set the current version on the dataset.
     * 
     * @throws Exception
     */
    protected void setCurrentVersionInGraph() throws RdfTripleStoreException {
        // delete old version
        this.deleteCurrentVersion();

        final Resource r = ResourceFactory.createResource( URI_OWL_ONTOLOGY );
        final Property p = ResourceFactory.createProperty( VERSION );
        final Literal l = ResourceFactory.createTypedLiteral(
                this.currentVersion, XSDDatatype.XSDstring );

        // if an info model doesn't exist yet, create one
        this.beginTransaction( ReadWrite.WRITE );
        try {          
            if( !this.jenaStore.containsNamedModel( URI_INFO_GRAPH ) ) {
                final Model defaultM = ModelFactory.createDefaultModel();
                defaultM.addLiteral( r, p, l );
                this.jenaStore.addNamedModel( URI_INFO_GRAPH, defaultM );               
            } else {
                final Model m = this.jenaStore.getNamedModel( URI_INFO_GRAPH );
                m.addLiteral( r, p, l );                
            }
            
            this.commitTransaction();
            
        } catch( final Exception e ) {
            throw new RdfTripleStoreException( e );
        } finally {
            this.endTransaction( true );
        }
    }

    public String showOntologyTriples() {
        final StringBuilder b = new StringBuilder();

        final OntModel ontM = this.getPureOntologyModel();

        // imported ontologies
        for( final String importedOnt : ontM.listImportedOntologyURIs() ) {
            b.append( "Imported ontology: " + importedOnt + "\n" );
        }

        return b.toString();
    }

    /**
     * Get a list of those ontologies which were imported in the OWL ontology.
     * 
     * @return
     */
    public Set< String > getImportedOntologies() {
        return this.getPureOntologyModel().listImportedOntologyURIs();
    }

    /**
     * Get the set of {@link OntClass}
     * 
     * @return
     */
    public Set< OntClass > getOntologieClasses() {
        final ExtendedIterator< OntClass > it = this.getPureOntologyModel()
                .listClasses();
        return CollectionUtil.buildSetFromIterator( it );
    }

    /**
     * Get the set of {@link ObjectProperty}
     * 
     * @return
     */
    public Set< ObjectProperty > getOntologieObjectProperties() {
        final ExtendedIterator< ObjectProperty > it = this
                .getPureOntologyModel().listObjectProperties();
        return CollectionUtil.buildSetFromIterator( it );
    }

    /**
     * Get the set of {@link DatatypeProperty}
     * 
     * @return
     */
    public Set< DatatypeProperty > getOntologieDatatypeProperties() {
        final ExtendedIterator< DatatypeProperty > it = this
                .getPureOntologyModel().listDatatypeProperties();
        return CollectionUtil.buildSetFromIterator( it );
    }

    /**
     * Get the set of {@link Individual}
     * 
     * @return
     */
    public Set< Individual > getOntologieIndividuals() {
        final ExtendedIterator< Individual > it = this.getPureOntologyModel()
                .listIndividuals();
        return CollectionUtil.buildSetFromIterator( it );
    }

    /**
     * Get the current (latest) version of the graph in this TDB / jena store.
     * You can use it to get the named graph containing the tripels.
     * 
     * @return
     */
    public String getCurrentVersion() {
        return this.currentVersion;
    }

    /**
     * Get the set of statements.
     * 
     * @return
     */
    public Set< Statement > getStatements() {
        if( null == this.workingVersion ) {
            throw new IllegalStateException(
                    "You need to set a working version before calling this method!" );
        }

        this.beginTransaction( ReadWrite.READ );
        try {
            final Iterator< Statement > it = this.jenaStore.getNamedModel(
                    this.workingVersion ).listStatements();
            final Set< Statement > set = CollectionUtil
                    .buildSetFromIterator( it );
            return set;
        } finally {
            this.endTransaction( false );
        }
    }

    /**
     * Get all available versions (graph names). The related SparQL is the
     * following:
     * 
     * <pre>
     *  SELECT DISTINCT ?g
     *             WHERE {
     *               GRAPH ?g {
     *                 ?s ?p ?o
     *               }
     *         }
     * </pre>
     * 
     * @return
     */
    public Set< String > getAvailableVersions() {
        this.beginTransaction( ReadWrite.READ );

        try {
            final Iterator< String > it = this.jenaStore.listNames();
            final Set< String > set = CollectionUtil.buildSetFromIterator( it );
            return set;
        } finally {
            this.endTransaction( false );
        }

    }
}
