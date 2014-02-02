/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.ontology.IOwlClasses;
import de.bht.fb6.s778455.bachelor.semantic.store.ontology.IOwlDatatypeProperties;
import de.bht.fb6.s778455.bachelor.semantic.store.ontology.IOwlObjectProperties;

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
    protected Resource getLmsClassResource( final String owlClass ) {
        final Resource r = this.getOntologyModel().getResource( owlClass );      
        return r;
    }

    /**
     * Create RDF triples which are stored by the {@link RdfTripleStoreAdapter}.
     * 
     * @param courseSet
     * @throws GeneralLoggingException 
     */
    public abstract void createRdfTriples( final LmsCourseSet courseSet ) throws GeneralLoggingException;  

}
