/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import java.net.URISyntaxException;
import java.net.URL;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import de.bht.fb6.s778455.bachelor.model.IRdfUsable;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IDCTerms;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlClasses;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlDatatypeProperties;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlObjectProperties;

/**
 * <p>
 * An {@link ACreationStrategy} realizes the creation of a semantic network.
 * </p>
 * 
 * <p>
 * In general, the input is a {@link LmsCourseSet}. The strategy works with the
 * {@link RdfTripleStoreAdapter} to add, delete or modify {@link RDFNode}
 * instances.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 * 
 */
public abstract class ACreationStrategy implements IOwlClasses,
        IOwlDatatypeProperties, IOwlObjectProperties {
    /**
     * The {@link OntModel} is the one which all strategies in the configured
     * chain shall manipulate. So, the scope has to be static.
     */
    protected static OntModel singletonOntModel;

    private final RdfTripleStoreAdapter tripleStoreAdapter;
    private OntModel ontologyModel;

    /**
     * Create a new strategy which fetches the {@link RdfTripleStoreAdapter}
     * from the {@link ServiceFactory}.
     */
    public ACreationStrategy() {
        this.tripleStoreAdapter = ServiceFactory.getJenaStoreAdapter();
        
        this._prepareSingletonOntModel();
    }

   

    /**
     * Create a new strategy with an injected {@link RdfTripleStoreAdapter}.
     * 
     * @param adapter
     */
    public ACreationStrategy( final RdfTripleStoreAdapter adapter ) {
        if( null == adapter ) {
            throw new IllegalArgumentException(
                    "Null values are not allowed for parameters!" );
        }

        this.tripleStoreAdapter = adapter;       
        
        this._prepareSingletonOntModel();
    }

    /**
     * Get or prepare the singleton ont model.
     */
    protected void _prepareSingletonOntModel() {
        // check if singleton exists
        if( null == singletonOntModel ) {
            this.ontologyModel = this.tripleStoreAdapter.getPureOntologyModel();
            
            // set in static scope
            singletonOntModel = this.ontologyModel;
        } else {
            this.ontologyModel = singletonOntModel;
        }
    }
    
    /**
     * Get the single triple store adapter.
     * 
     * @return
     */
    protected RdfTripleStoreAdapter getTripleStoreAdapter() {
        return this.tripleStoreAdapter;
    }

    /**
     * Get the pure ontology model (shortener to
     * tripleStoreAdapter.getPureOntologyModel())
     * 
     * @return
     */
    protected OntModel getOntologyModel() {
        return this.ontologyModel;
    }

    /**
     * Get resource for the given lms class.
     * 
     * @param owlClass
     * @return
     */
    protected OntClass getClassResource( final String owlClass ) {
        final OntClass r = this.getOntologyModel().getOntClass( owlClass );
        return r;
    }

    /**
     * Create RDF triples which are stored by the {@link RdfTripleStoreAdapter}.
     * 
     * @param courseSet
     * @throws GeneralLoggingException
     */
    public abstract void createRdfTriples( final LmsCourseSet courseSet )
            throws GeneralLoggingException;

    /**
     * Get the {@link Model} which was enriched by this strategy.
     * 
     * @return
     */
    public Model getEnrichedModel() {
        return this.getOntologyModel();
    }

    /**
     * Create an {@link Individual} in the ontology model.
     * 
     * @param rdfUsable
     *            the model, offering a getRdfUri() implementation
     * @param classResource
     *            the Owl class, use the interface IOwlClasses
     * @return {@link Individual}. Consider that it is added to the ont model
     *         immediatly.
     * @throws URISyntaxException
     */
    protected Individual createIndividual( final IRdfUsable rdfUsable,
            final OntClass classResource ) throws URISyntaxException {
        final Individual individual = this.getOntologyModel().createIndividual(
                rdfUsable.getRdfUri().toString(), classResource );
        return individual;
    }

    /**
     * Add the property 'property_data_title' on the given resource. The
     * property range is a string. This method will transform it to the correct
     * XSD datatype.
     * 
     * @param resource
     * @param title
     * @throws GeneralLoggingException
     */
    protected void addPropertyDataTitle( final Resource resource,
            final String title ) throws GeneralLoggingException {
        // get property instance
        final Property titleProperty = this.getOntologyModel().createProperty(
                PROPERTY_DATA_TITLE );
        try {
            if( null == titleProperty
                    || !this.getOntologyModel()
                            .containsResource( titleProperty ) ) {
                // no title property found
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":addPropertyDataTitle(): No OWL data property found in the used ontology for the title. It must have the URI: "
                                + PROPERTY_DATA_TITLE,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final NullPointerException e ) {
            // no title property found
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":addPropertyDataTitle(): No OWL data property found in the used ontology for the title. It must have the URI: "
                            + PROPERTY_DATA_TITLE,
                    "Internal error in the CourseCreation. See the logs" );
        }
        // get litertal instance
        final Literal titleLiteral = ResourceFactory.createTypedLiteral( title,
                XSDDatatype.XSDstring );
        // now add the triple / statement
        this.getOntologyModel().add( resource, titleProperty, titleLiteral );
    }

    /**
     * Add the property 'property_data_web_url' on the given resource. The
     * property range is a dctermsUri.
     * 
     * @param courseIndividual
     * @param url
     * @throws GeneralLoggingException
     */
    public void addPropertyDataWebUrl( final Individual courseIndividual,
            final URL url ) throws GeneralLoggingException {
        // get property instance
        final Property uriProperty = this.getOntologyModel().createProperty(
                PROPERTY_DATA_WEB_URI );
        try {
            if( null == uriProperty
                    || !this.getOntologyModel().containsResource( uriProperty ) ) {
                // no title property found
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":addPropertyDataWebUrl(): No OWL data property found in the used ontology for the web url. It must have the URI: "
                                + PROPERTY_DATA_WEB_URI,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final NullPointerException e ) {
            // no title property found
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":addPropertyDataWebUrl(): No OWL data property found in the used ontology for the web url. It must have the URI: "
                            + PROPERTY_DATA_TITLE,
                    "Internal error in the CourseCreation. See the logs" );
        }
        // get litertal instance
        final Literal uriLiteral = this.getOntologyModel().createTypedLiteral(
                url, IDCTerms.DCTERMS_URI );
        // now add the triple / statement
        this.getOntologyModel().add( courseIndividual, uriProperty, uriLiteral );
    }

    /**
     * Add the connecting property 'property_object_[leftside]_[rightside]' to
     * the ontology model.
     * 
     * @param leftIndividual
     * @param rightIndividual
     * @param objProperty
     * @throws GeneralLoggingException
     */
    public void addPropertyObjectBetween( final Individual leftIndividual,
            final Individual rightIndividual, final String objProperty )
            throws GeneralLoggingException {
        final ObjectProperty objectProperty = this.getOntologyModel()
                .createObjectProperty( objProperty );
        try {
            if( null == objectProperty
                    || !this.getOntologyModel().containsResource(
                            objectProperty ) ) {
                // property not found
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":addPropertyObjectBetween(): No OWL object property found in the used ontology for the object property. It must have the URI: "
                                + objProperty,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":addPropertyObjectBetween(): No OWL object property found in the used ontology for the object property. It must have the URI: "
                            + objProperty,
                    "Internal error in the CourseCreation. See the logs" );
        }

        // add triple / statement
        this.getOntologyModel().add( leftIndividual, objectProperty,
                rightIndividual );
    }

    /**
     * Create a language edge for the given {@link IRdfUsable}.
     * 
     * @param course
     * @throws GeneralLoggingException
     */
    protected void createLanguageEdge( final IRdfUsable model,
            final Language lang ) throws GeneralLoggingException {
        if( null == model || null == lang ) {
            throw new IllegalArgumentException(
                    "Null values are not allowed as parameters!" );
        }
        // don't map unknown language. do a log entry and return to the calling
        // method
        if( lang.equals( Language.UNKNOWN ) ) {
            Application.log(
                    "createLanguageEdge(): unknown language can't be mapped. Model: "
                            + model, LogType.WARNING, this.getClass() );
            return;
        }

        final OntModel ontModel = this.getOntologyModel();
        try {
            final Individual indModel = ontModel.getIndividual( model
                    .getRdfUri().toString() );
            final Individual langInd = ontModel.getIndividual( lang.getRdfUri()
                    .toString() );

            // check if individuals exist
            if( null == indModel || !ontModel.containsResource( indModel ) ) {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createLanguageEdge(): No individual found for model: "
                                + model,
                        "Internal error in the CourseCreation. See the logs" );
            }
            if( null == langInd || !ontModel.containsResource( langInd ) ) {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createLanguageEdge(): No individual found for lang: "
                                + lang,
                        "Internal error in the CourseCreation. See the logs" );
            }

            // add node
            this.addPropertyObjectBetween( indModel, langInd,
                    PROPERTY_OBJECT_LANGUAGE );
        } catch( final URISyntaxException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createLanguageEdge(): Illegal URI for language or model: "
                            + lang + "; model: " + model,
                    "Internal error in the CourseCreation. See the logs" );
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException( this.getClass()
                    + ":createLanguageEdge(): No individual found for lang: "
                    + lang,
                    "Internal error in the CourseCreation. See the logs" );
        }
    }

}
