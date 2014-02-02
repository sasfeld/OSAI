/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import java.net.URISyntaxException;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import de.bht.fb6.s778455.bachelor.model.IRdfUsable;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
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
public abstract class ACreationStrategy implements IOwlClasses, IOwlDatatypeProperties, IOwlObjectProperties {
    private final RdfTripleStoreAdapter tripleStoreAdapter;
    private final OntModel ontologyModel;
    
    public ACreationStrategy() {
        this.tripleStoreAdapter = ServiceFactory.getJenaStoreAdapter();
        this.ontologyModel = this.tripleStoreAdapter.getPureOntologyModel();
    }
    
    public ACreationStrategy( final RdfTripleStoreAdapter adapter ) {
        if ( null == adapter ) {
            throw new IllegalArgumentException( "Null values are not allowed for parameters!" );
        }
        
        this.tripleStoreAdapter = adapter;
        this.ontologyModel = this.tripleStoreAdapter.getPureOntologyModel();
    }
    
    /**
     * Get the single triple store adapter.
     * @return
     */
    protected RdfTripleStoreAdapter getTripleStoreAdapter() {
        return this.tripleStoreAdapter;
    }
    
    /**
     * Get the pure ontology model (shortener to tripleStoreAdapter.getPureOntologyModel())
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
    public abstract void createRdfTriples( final LmsCourseSet courseSet ) throws GeneralLoggingException;  

    /**
     * Create an {@link Individual} in the ontology model.
     * @param rdfUsable the model, offering a getRdfUri() implementation
     * @param courseClassResource the Owl class, use the interface IOwlClasses
     * @return {@link Individual}. Consider that it is added to the ont model immediatly.
     * @throws URISyntaxException 
     */
    protected Individual createIndividual( final IRdfUsable rdfUsable, final OntClass courseClassResource ) throws URISyntaxException {
        final Individual individual = this.getOntologyModel().createIndividual( rdfUsable.getRdfUri().toString(), courseClassResource );
        return individual;
    }
    
    /**
     * Add the property 'property_data_title' on the given resource. 
     * The property range is a string. This method will transform it to the correct XSD datatype.
     * @param resource
     * @param title
     * @throws GeneralLoggingException 
     */
    protected void addPropertyDataTitle( final Resource resource, final String title) throws GeneralLoggingException {
        // get property instance
        final Property titleProperty = this.getOntologyModel()
                .createProperty( PROPERTY_DATA_TITLE );
        if( null == titleProperty ) {
            // no title property found
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":addPropertyDataTitle(): No OWL data property found in the used ontology for the title. It must have the URI: "
                            + PROPERTY_DATA_TITLE,
                    "Internal error in the CourseCreation. See the logs" );
        }
        // get litertal instance
        final Literal titleLiteral = ResourceFactory
                .createTypedLiteral( title,
                        XSDDatatype.XSDstring );
        // now add the triple / statement
        this.getOntologyModel().add( resource, titleProperty, titleLiteral );
    }

    /**
     * Add the property 'property_data_web_url' on the given resource.
     * The property range is a dctermsUri.
     * @param courseIndividual
     * @param url
     * @throws GeneralLoggingException 
     */
    public void addPropertyDataWebUrl( final Individual courseIndividual, final String url ) throws GeneralLoggingException {
        // get property instance
        final Property uriProperty = this.getOntologyModel()
                .createProperty( PROPERTY_DATA_WEB_URI );
        if( null == uriProperty || !this.getOntologyModel().containsResource( uriProperty ) ) {
            // no title property found
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":addPropertyDataWebUrl(): No OWL data property found in the used ontology for the title. It must have the URI: "
                            + PROPERTY_DATA_WEB_URI,
                    "Internal error in the CourseCreation. See the logs" );
        }
        // get litertal instance
        final Literal uriLiteral = this.getOntologyModel().createTypedLiteral( url, IDCTerms.DCTERMS_URI );
        // now add the triple / statement
        this.getOntologyModel().add( courseIndividual, uriProperty, uriLiteral );        
    }
    
    
}
