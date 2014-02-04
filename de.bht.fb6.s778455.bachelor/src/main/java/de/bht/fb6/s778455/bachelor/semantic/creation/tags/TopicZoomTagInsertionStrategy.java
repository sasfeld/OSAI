/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.tags;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
import de.bht.fb6.s778455.bachelor.model.tools.CourseUtil;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>
 * This class realizes the insertion of {@link TopicZoomTag} instances.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 * 
 */
public class TopicZoomTagInsertionStrategy extends ATagInsertionStrategy {
    /**
     * Create a new strategy with an injected {@link RdfTripleStoreAdapter}.
     * 
     * @param adapter
     */
    public TopicZoomTagInsertionStrategy( final RdfTripleStoreAdapter adapter ) {
        super( adapter );
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy#
     * createRdfTriples(de.bht.fb6.s778455.bachelor.model.LmsCourseSet)
     */
    @Override
    public void createRdfTriples( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        // add nodes for each TopicZoomTag
        this.addNodesForTzTags( courseSet );

        // for( final Course course : courseSet ) {
        // this.addCourseTags( course );
        // }
    }

    protected void addNodesForTzTags( final LmsCourseSet courseSet ) throws GeneralLoggingException {
        if( null == courseSet ) {
            throw new IllegalArgumentException(
                    "Null value is not allowed for parameter!" );
        }

        final Set< Tag > tzTags = CourseUtil.getDistinctTags( courseSet,
                TagType.TOPIC_ZOOM );

        final boolean validTzTags = this.validateTags( tzTags );
        if( validTzTags ) {
            final OntModel ontModel = super.getOntologyModel();

            // try to get class for Topic from ontology
            final OntClass tagClass = ontModel.getOntClass( CLASS_TOPIC );
            if( null != tagClass && ontModel.containsResource( tagClass ) ) {
                for( final Tag tag : tzTags ) {
                    try {
                        final Individual topicIndividual = super
                                .createIndividual( tag, tagClass );
                        
                        // add title property to new individual
                        if ( null != tag.getValue() ) {
                            super.addPropertyDataTitle( topicIndividual, tag.getValue() );
                        }
                        
                        // add the original URI to the new individual
                        if ( null != tag.getUri() ) {
                            super.addPropertyObjectTopicConceptUri( topicIndividual, tag.getUri());
                        }
                        
                    } catch( final URISyntaxException e ) {
                        Application
                                .log( this.getClass()
                                        + ":addNodesForTzTags(): illegal URI for tag: "
                                        + tag.getUri()
                                        + "! I will not add it to the semantic network.",
                                        LogType.ERROR );
                    }
                }
            } else {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":addNodesForTzTags(): no OWL class found in the ontology for: "
                                + CLASS_TOPIC, "Error in TopicZoomTagInsertion. Read the logs." );
            }
        }
    }

    /**
     * Check if the given TZ tags are valid {@link TopicZoomTag} instances.
     * 
     * @param tzTags
     * @return
     */
    private boolean validateTags( final Set< Tag > tzTags ) {
        if( null == tzTags ) {
            throw new IllegalArgumentException(
                    "Null value is not allowed for parameter!" );
        }

        for( final Tag tag : tzTags ) {
            if( !( tag instanceof TopicZoomTag ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add the {@link TopicZoomTag} instances of the given {@link Course} to the
     * semantic network
     * 
     * @param course
     */
    protected void addCourseTags( final Course course ) {
        if( null == course ) {
            throw new IllegalArgumentException(
                    "Null value is not allowed for parameter!" );
        }

        final List< Tag > courseTags = course.getTags( TagType.TOPIC_ZOOM );

    }
}
