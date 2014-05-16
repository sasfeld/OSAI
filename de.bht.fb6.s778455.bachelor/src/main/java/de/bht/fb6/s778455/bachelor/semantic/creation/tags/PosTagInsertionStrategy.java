/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.tags;

import java.net.URISyntaxException;
import java.util.Set;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PosTag;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.tools.CourseUtil;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>This class realizes the insertion of {@link PosTag} instances.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 19.02.2014
 *
 */
public class PosTagInsertionStrategy extends ATagInsertionStrategy {
    /**
     * Create a new strategy which fetches the {@link RdfTripleStoreAdapter}
     * from the {@link ServiceFactory}.
     */
    public PosTagInsertionStrategy() {
        super();
    }

    /**
     * Create a new strategy with an injected {@link RdfTripleStoreAdapter}.
     * 
     * @param adapter
     */
    public PosTagInsertionStrategy( final RdfTripleStoreAdapter adapter ) {
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
        this.addNodesForPosTags( courseSet );

        for( final Course course : courseSet ) {
            if( course.isPosTagged() ) {
                this.addTopics( course, course.getTags( TagType.POS_TAG ), TagType.POS_TAG );
            }

            for( final Board board : course.getBoards() ) {
                if( board.isPosTagged() ) {
                    this.addTopics( board, board.getTags( TagType.POS_TAG ), TagType.POS_TAG );
                }

                for( final BoardThread boardThread : board.getBoardThreads() ) {
                    if( boardThread.isPosTagged() ) {
                        this.addTopics( boardThread,
                                boardThread.getTags( TagType.POS_TAG ), TagType.POS_TAG );
                    }

                    for( final Posting posting : boardThread.getPostings() ) {
                        if( posting.isPosTagged() ) {
                            this.addTopics( posting,
                                    posting.getTags( TagType.POS_TAG ), TagType.POS_TAG );
                        }
                    }
                }
            }
        }
    }

    /**
     * Add the RDF nodes / OWL individuals for the {@link PosTag} instances.
     * Here, the GermaNet or WordNet ontologies could be connected....
     * @param courseSet
     * @throws GeneralLoggingException
     */
    protected void addNodesForPosTags( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        if( null == courseSet ) {
            throw new IllegalArgumentException(
                    "Null value is not allowed for parameter!" );
        }

        final Set< Tag > tzTags = CourseUtil.getDistinctTags( courseSet,
                TagType.POS_TAG );

        final boolean validTzTags = this.validateTags( tzTags, TagType.POS_TAG );
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
                        if( null != tag.getValue() ) {
                            super.addPropertyDataTitle( topicIndividual,
                                    tag.getValue() );
                        }

                        // add the original URI to the new individual
                        if( null != tag.getRelatedConceptUri() ) {
                            super.addPropertyObjectTopicConceptUri(
                                    topicIndividual, tag.getRelatedConceptUri() );
                        }

                    } catch( final URISyntaxException e ) {
                        Application
                                .log( this.getClass()
                                        + ":addNodesForTzTags(): illegal URI for tag: "
                                        + tag.getRelatedConceptUri()
                                        + "! I will not add it to the semantic network.",
                                        LogType.ERROR );
                    }
                }
            } else {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":addNodesForTzTags(): no OWL class found in the ontology for: "
                                + CLASS_TOPIC,
                        "Error in TopicZoomTagInsertion. Read the logs." );
            }
        }
    }
}
