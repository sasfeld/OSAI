/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation;

import com.hp.hpl.jena.ontology.OntModel;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;

/**
 * <p>This class takes a {@link LmsCourseSet} and creates triples showing the semantics of the entities..</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 *
 */
public class CourseCreationStrategy extends ACreationStrategy {

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy#createRdfTriples(de.bht.fb6.s778455.bachelor.model.LmsCourseSet)
     */
    @Override
    public void createRdfTriples( final LmsCourseSet courseSet ) {
        this.createLmsInstance( courseSet );
        
        
       for( final Course course : courseSet ) {
           
       }
    }

    /**
     * Create a single OWL instance for the courseSet instance.
     * @param courseSet
     */
    protected void createLmsInstance( final LmsCourseSet courseSet ) {
        final OntModel ontModel = super.getOntologyModel();
        
        
    }

}
