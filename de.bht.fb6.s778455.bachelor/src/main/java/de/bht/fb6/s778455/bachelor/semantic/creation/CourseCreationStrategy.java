/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import java.net.URISyntaxException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

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

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy#
     * createRdfTriples(de.bht.fb6.s778455.bachelor.model.LmsCourseSet)
     */
    @Override
    public void createRdfTriples( final LmsCourseSet courseSet )
            throws GeneralLoggingException {
        this.createLmsInstance( courseSet );

        for( final Course course : courseSet ) {

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
            final Resource lmsClassResource = this
                    .getLmsClassResource( CLASS_LMS );

            if( null != lmsClassResource ) {
                // found resource
                final Individual lmsIndividual = ontModel.createIndividual(
                        courseSet.getRdfUri().toString(), lmsClassResource );

                // add title
                final Property titleProperty = ontModel
                        .createProperty( PROPERTY_DATA_TITLE );
                if( null == titleProperty ) {
                    // no title property found
                    throw new GeneralLoggingException(
                            this.getClass()
                                    + ":createLmsInstance(): No OWL data property found in the used ontology for the title. It must have the URI: "
                                    + PROPERTY_DATA_TITLE,
                            "Internal error in the CourseCreation. See the logs" );
                }        

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
