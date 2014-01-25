/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import com.hp.hpl.jena.rdf.model.RDFNode;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

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
public abstract class ACreationStrategy {
    private final RdfTripleStoreAdapter tripleStoreAdapter;
    
    public ACreationStrategy() {
        this.tripleStoreAdapter = ServiceFactory.getJenaStoreAdapter();
    }
    
    protected RdfTripleStoreAdapter getTripleStoreAdapter() {
        return this.tripleStoreAdapter;
    }

    /**
     * Create RDF triples which are stored by the {@link RdfTripleStoreAdapter}.
     * 
     * @param courseSet
     */
    public abstract void createRdfTriples( final LmsCourseSet courseSet );  

}
