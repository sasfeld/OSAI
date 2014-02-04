/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.tags;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>This abstract class contains generic work done by those strategies which insert {@link Tag} instances to the semantic network..</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 *
 */
public abstract class ATagInsertionStrategy extends ACreationStrategy {

    /**
     * Create a new strategy which fetches the {@link RdfTripleStoreAdapter}
     * from the {@link ServiceFactory}.
     */
    public ATagInsertionStrategy() {
        super();
    }
    
    /**
     * Create a new strategy with an injected {@link RdfTripleStoreAdapter}.
     * @param adapter
     */
    public ATagInsertionStrategy( final RdfTripleStoreAdapter adapter ) {
        super( adapter );
    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy#createRdfTriples(de.bht.fb6.s778455.bachelor.model.LmsCourseSet)
     */
    @Override
    public void createRdfTriples( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        // TODO identify generic work
    }

    /**
     * Add the object property to a concept URI which is not part of the own ontology.
     * @param topicIndividual
     * @param uri
     * @throws GeneralLoggingException 
     */
    public void addPropertyObjectTopicConceptUri( final Individual topicIndividual,
            final String uri ) throws GeneralLoggingException {
        final ObjectProperty objectProperty = this.getOntologyModel().createObjectProperty( PROPERTY_OBJECT_TOPIC_URI );
        if ( null == objectProperty || !this.getOntologyModel().containsResource( objectProperty )) {
            // property not found
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":addPropertyObjectTopicConceptUri(): No OWL object property found in the used ontology for the topic uri. It must have the URI: "
                            + PROPERTY_OBJECT_TOPIC_URI,
                    "Internal error in the ATagInsertionStrategy. See the logs" );
        }
        
        // add triple / statement
        this.getOntologyModel().add( topicIndividual, objectProperty, uri );        
    }
}
