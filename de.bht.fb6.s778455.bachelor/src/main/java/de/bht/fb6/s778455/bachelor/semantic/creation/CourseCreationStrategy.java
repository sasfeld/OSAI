/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import java.net.URISyntaxException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

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
        }
    }

    /**
     * Create a single OWL instance for the given course instance.
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
                
                // add title          
                super.addPropertyDataTitle( courseIndividual, course.getTitle() ); 
                super.addPropertyDataWebUrl( courseIndividual, course.getUrl() );                
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
        }
    }

    /**
     * Create a single OWL instance for the courseSet instance.
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
                final Individual lmsIndividual = this.createIndividual( courseSet, lmsClassResource );

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
        }
    }

}
