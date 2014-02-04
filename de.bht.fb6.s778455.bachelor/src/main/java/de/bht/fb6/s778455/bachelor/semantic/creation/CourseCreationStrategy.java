/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import java.net.URISyntaxException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.IRdfUsable;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlObjectProperties;

/**
 * <p>
 * This class takes a {@link LmsCourseSet} and creates triples showing the
 * semantics of the entities..
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 * 
 */
public class CourseCreationStrategy extends ACreationStrategy {

    /**
     * Create a new strategy which fetches the {@link RdfTripleStoreAdapter}
     * from the {@link ServiceFactory}.
     */
    public CourseCreationStrategy() {
        super();
    }
    
    /**
     * Create a strategy with given adapter (otherwise use the constructor
     * without args)
     * 
     * @param adapter
     */
    public CourseCreationStrategy( final RdfTripleStoreAdapter adapter ) {
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
        // create individual for LMS instance first
        this.createLmsInstance( courseSet );

        // create individuals for course instances
        for( final Course course : courseSet ) {
            this.createCourseInstance( course );

            // create individuals for board instances
            for( final Board board : course.getBoards() ) {
                this.createBoardInstance( board );

                // create individuals for thread instances
                for( final BoardThread thread : board.getBoardThreads() ) {
                    this.createThreadInstance( thread );

                    // create individuals for postings
                    for( final Posting posting : thread.getPostings() ) {
                        this.createPostingInstance( posting );
                    }
                }
            }
        }

        this.createEdges( courseSet );
    }

    /**
     * Create the edges for the following object properties:
     * <ul>
     * <li>property_object_board_thread</li>
     * <li>property_object_course_board</li>
     * <li>property_object_thread_posting</li>
     * <li>property_object_language</li>
     * </ul>
     * 
     * @param courseSet
     * @throws GeneralLoggingException
     */
    private void createEdges( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        this.createCourseBoardEdges( courseSet );
    }

    /**
     * property_object_course_board
     * 
     * @param courseSet
     * @throws GeneralLoggingException
     */
    private void createCourseBoardEdges( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        for( final Course course : courseSet ) {
            if( null != course.getLanguage() ) {
                try {
                    super.createLanguageEdge( course, course.getLanguage() );
                } catch( final GeneralLoggingException e ) {
                    // don't react, already logged
                }
            }
            for( final Board board : course.getBoards() ) {
                if( null != board.getLang() ) {
                    try {
                        super.createLanguageEdge( board, board.getLang() );
                    } catch( final GeneralLoggingException e ) {
                        // don't react, already logged
                    }
                }
                this.createEdge( course, board,
                        IOwlObjectProperties.PROPERTY_OBJECT_COURSE_BOARD );

                for( final BoardThread thread : board.getBoardThreads() ) {
                    if( null != thread.getLang() ) {
                        try {
                            super.createLanguageEdge( thread, thread.getLang() );
                        } catch( final GeneralLoggingException e ) {
                            // don't react, already logged
                        }
                    }
                    this.createEdge( board, thread,
                            IOwlObjectProperties.PROPERTY_OBJECT_BOARD_THREAD );

                    for( final Posting posting : thread.getPostings() ) {
                        if( null != posting.getLang() ) {
                            try {
                                super.createLanguageEdge( posting,
                                        posting.getLang() );
                            } catch( final GeneralLoggingException e ) {
                                // don't react, already logged
                            }
                        }
                        this.createEdge(
                                thread,
                                posting,
                                IOwlObjectProperties.PROPERTY_OBJECT_THREAD_POSTING );
                    }
                }
            }
        }
    }

    /**
     * Create a single OWL object property between the given course and the
     * board.
     * 
     * @param leftSide
     * @param rightSide
     * @throws GeneralLoggingException
     */
    private void createEdge( final IRdfUsable leftSide,
            final IRdfUsable rightSide, final String objectProperty )
            throws GeneralLoggingException {
        final OntModel ontModel = super.getOntologyModel();
        try {
            final Individual indLeft = ontModel.getIndividual( leftSide
                    .getRdfUri().toString() );
            final Individual indRight = ontModel.getIndividual( rightSide
                    .getRdfUri().toString() );

            // check if individuals exist
            try {
                if( null == indLeft || !ontModel.containsResource( indLeft ) ) {
                    throw new GeneralLoggingException(
                            this.getClass()
                                    + ":createCourseBoardEdges(): No individual found for course: "
                                    + leftSide.getRdfUri(),
                            "Internal error in the CourseCreation. See the logs" );
                }
            } catch( final NullPointerException e ) {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createCourseBoardEdges(): No individual found for course: "
                                + leftSide.getRdfUri(),
                        "Internal error in the CourseCreation. See the logs" );
            }

            try {
                if( null == indRight || !ontModel.containsResource( indRight ) ) {
                    throw new GeneralLoggingException(
                            this.getClass()
                                    + ":createCourseBoardEdges(): No individual found for board: "
                                    + rightSide.getRdfUri(),
                            "Internal error in the CourseCreation. See the logs" );
                }
            } catch( final NullPointerException e ) {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createCourseBoardEdges(): No individual found for course: "
                                + leftSide.getRdfUri(),
                        "Internal error in the CourseCreation. See the logs" );
            }

            // add node between them
            super.addPropertyObjectBetween( indLeft, indRight, objectProperty );
        } catch( final URISyntaxException e ) {
            try {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createCourseBoardEdges(): Illegal URI for course or board: "
                                + leftSide.getRdfUri() + "; board: "
                                + rightSide.getRdfUri(),
                        "Internal error in the CourseCreation. See the logs" );
            } catch( final URISyntaxException e1 ) {
                // forget it
            }
        }
    }

    /**
     * Create a single OWL instance / individual for the given posting.
     * 
     * @param posting
     * @throws GeneralLoggingException
     */
    private void createPostingInstance( final Posting posting )
            throws GeneralLoggingException {
        final OntModel ontModel = super.getOntologyModel();

        final OntClass postingResource = this.getClassResource( CLASS_POSTING );

        try {
            if( null != postingResource
                    && ontModel.containsResource( postingResource ) ) {
                // create individual
                final Individual threadIndividual = this.createIndividual(
                        posting, postingResource );
                /*
                 * ###### add properties ######
                 */

                // add title
                super.addPropertyDataTitle( threadIndividual,
                        posting.getTitle() );

            } else {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createPostingInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                                + CLASS_POSTING,
                        "Internal error in the CourseCreation. See the logs" );
            }

        } catch( final URISyntaxException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createPostingInstance(): invalid URI returned by course: "
                            + posting.getTitle(),
                    "Internal error in the CourseCreation. See the logs" );
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createPostingInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                            + CLASS_POSTING,
                    "Internal error in the CourseCreation. See the logs" );
        }
    }

    /**
     * Create a single OWL instance / individual for the given thread instance.
     * 
     * @param thread
     * @throws GeneralLoggingException
     */
    private void createThreadInstance( final BoardThread thread )
            throws GeneralLoggingException {
        final OntModel ontModel = super.getOntologyModel();

        final OntClass threadResource = this.getClassResource( CLASS_THREAD );

        try {
            if( null != threadResource
                    && ontModel.containsResource( threadResource ) ) {
                // create individual
                final Individual threadIndividual = this.createIndividual(
                        thread, threadResource );
                /*
                 * ###### add properties ######
                 */

                // add title and web url
                if( null != thread.getTitle() ) {
                    super.addPropertyDataTitle( threadIndividual,
                            thread.getTitle() );
                }
                if( null != thread.getWebUrl() ) {
                    super.addPropertyDataWebUrl( threadIndividual,
                            thread.getWebUrl() );
                }
            } else {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createThreadInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                                + CLASS_THREAD,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final URISyntaxException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createThreadInstance(): invalid URI returned by course: "
                            + thread.getTitle(),
                    "Internal error in the CourseCreation. See the logs" );
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createPostingInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                            + CLASS_POSTING,
                    "Internal error in the CourseCreation. See the logs" );
        }

    }

    /**
     * Create a single OWL instance / individual for the given board instance.
     * 
     * @param board
     * @throws GeneralLoggingException
     */
    private void createBoardInstance( final Board board )
            throws GeneralLoggingException {
        final OntModel ontModel = super.getOntologyModel();

        final OntClass courseBoardResource = this
                .getClassResource( CLASS_BOARD );

        try {
            if( null != courseBoardResource
                    && ontModel.containsResource( courseBoardResource ) ) {
                // create individual
                final Individual boardIndividual = this.createIndividual(
                        board, courseBoardResource );
                /*
                 * ###### add properties ######
                 */

                // add title and web url
                super.addPropertyDataTitle( boardIndividual, board.getTitle() );

                if( null != board.getWebUrl() ) {
                    super.addPropertyDataWebUrl( boardIndividual,
                            board.getWebUrl() );
                }
            } else {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createBoardInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                                + CLASS_BOARD,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final URISyntaxException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createBoardInstance(): invalid URI returned by course: "
                            + board.getTitle(),
                    "Internal error in the CourseCreation. See the logs" );
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createPostingInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                            + CLASS_POSTING,
                    "Internal error in the CourseCreation. See the logs" );
        }

    }

    /**
     * Create a single OWL instance / individual for the given course instance.
     * 
     * @param course
     * @throws GeneralLoggingException
     */
    private void createCourseInstance( final Course course )
            throws GeneralLoggingException {
        final OntModel ontModel = super.getOntologyModel();

        final OntClass courseClassResource = this
                .getClassResource( CLASS_COURSE );

        try {
            if( null != courseClassResource
                    && ontModel.containsResource( courseClassResource ) ) {
                // create individual
                final Individual courseIndividual = this.createIndividual(
                        course, courseClassResource );

                /*
                 * ###### add properties ######
                 */

                // add title and web url
                super.addPropertyDataTitle( courseIndividual, course.getTitle() );
                if( null != course.getWebUrl() ) {
                    super.addPropertyDataWebUrl( courseIndividual,
                            course.getWebUrl() );
                }
            } else {
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createCourseInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                                + CLASS_COURSE,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final URISyntaxException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createCourseInstance(): invalid URI returned by course: "
                            + course.getShortName(),
                    "Internal error in the CourseCreation. See the logs" );
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createPostingInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                            + CLASS_POSTING,
                    "Internal error in the CourseCreation. See the logs" );
        }
    }

    /**
     * Create a single OWL instance / individual for the courseSet instance.
     * 
     * @param courseSet
     * @throws GeneralLoggingException
     */
    protected void createLmsInstance( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        final OntModel ontModel = super.getOntologyModel();

        try {
            final OntClass lmsClassResource = this.getClassResource( CLASS_LMS );

            if( null != lmsClassResource
                    && ontModel.containsResource( lmsClassResource ) ) {
                // found resource
                final Individual lmsIndividual = this.createIndividual(
                        courseSet, lmsClassResource );

                // add title
                super.addPropertyDataTitle( lmsIndividual, courseSet.getName() );

            } else {
                // no resource found for LMS class
                throw new GeneralLoggingException(
                        this.getClass()
                                + ":createLmsInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                                + CLASS_LMS,
                        "Internal error in the CourseCreation. See the logs" );
            }
        } catch( final URISyntaxException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createLmsInstance(): invalid URI returned by courseSet: "
                            + courseSet.getName(),
                    "Internal error in the CourseCreation. See the logs" );
        } catch( final NullPointerException e ) {
            throw new GeneralLoggingException(
                    this.getClass()
                            + ":createPostingInstance(): No OWL class found in the used ontology for the LMS course set. The class must have the URI: "
                            + CLASS_POSTING,
                    "Internal error in the CourseCreation. See the logs" );
        }
    }

}
