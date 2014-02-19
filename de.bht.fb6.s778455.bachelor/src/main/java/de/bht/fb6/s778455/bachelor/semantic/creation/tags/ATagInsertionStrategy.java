/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.tags;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;

import de.bht.fb6.s778455.bachelor.model.IRdfUsable;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
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
    
    /**
     * Add the {@link TopicZoomTag} instances of the given IRdfUsable to the
     * semantic network.
     * 
     * @param model
     * @param tags
     * @param topicZoom 
     * @throws GeneralLoggingException
     */
    protected void addTopics( final IRdfUsable model, final List< Tag > tags, final TagType tagType )
            throws GeneralLoggingException {
        if( null == model ) {
            throw new IllegalArgumentException(
                    "Null value is not allowed for parameter!" );
        }

        final boolean validModelTags = this.validateTags( tags, tagType );
        if( validModelTags ) {
            // try to get individual for model
            try {
                final Individual modelIndividual = super.getOntologyModel()
                        .getIndividual( model.getRdfUri().toString() );

                try {
                    if( null == modelIndividual
                            && !super.getOntologyModel().containsResource(
                                    modelIndividual ) ) {
                        throw new GeneralLoggingException(
                                this.getClass()
                                        + ":addTopic(): no OWL individual found in the ontology for: "
                                        + model,
                                "Error in TopicZoomTagInsertion. Read the logs." );

                    }
                } catch( final NullPointerException e ) {
                    throw new GeneralLoggingException(
                            this.getClass()
                                    + ":addTopic(): no OWL individual found in the ontology for: "
                                    + model,
                            "Error in TopicZoomTagInsertion. Read the logs." );
                }

                // try to get individual for each tag instance
                for( final Tag tag : tags ) {
                    final Individual tagIndividual = super.getOntologyModel()
                            .getIndividual( tag.getRdfUri().toString() );

                    try {
                        if( null == tagIndividual
                                && !super.getOntologyModel().containsResource(
                                        tagIndividual ) ) {
                            throw new GeneralLoggingException(
                                    this.getClass()
                                            + ":addTopic(): no OWL individual found in the ontology for tag: "
                                            + tag,
                                    "Error in TopicZoomTagInsertion. Read the logs." );
                        }
                    } catch( final NullPointerException e ) {
                        throw new GeneralLoggingException(
                                this.getClass()
                                        + ":addTopic(): no OWL individual found in the ontology for tag: "
                                        + tag,
                                "Error in TopicZoomTagInsertion. Read the logs." );
                    }

                    // add object property edge between model and tag
                    try {
                        super.addPropertyObjectBetween( modelIndividual,
                                tagIndividual, PROPERTY_OBJECT_HASTOPIC );
                    } catch( final GeneralLoggingException e ) {
                        // don't do anything, continue with other tags, alread
                        // logged
                    }
                }

            } catch( final URISyntaxException e ) {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":addTopic(): no OWL individual found in the ontology for: "
                                + model,
                        "Error in TopicZoomTagInsertion. Read the logs." );
            }
        }
    }
    
    /**
     * Check if the given TZ tags are valid {@link TopicZoomTag} instances.
     * 
     * @param tzTags
     * @return
     */
    protected boolean validateTags( final Collection< Tag > tzTags, final TagType tagType ) {
        if( null == tzTags ) {
            throw new IllegalArgumentException(
                    "Null value is not allowed for parameter!" );
        }

        for( final Tag tag : tzTags ) {
            final Class< ? > clazz = tagType.getCorrespondingClass();
            if( !( tag.getClass().equals( clazz ) ) ) {
                return false;
            }
        }
        return true;
    }
}
