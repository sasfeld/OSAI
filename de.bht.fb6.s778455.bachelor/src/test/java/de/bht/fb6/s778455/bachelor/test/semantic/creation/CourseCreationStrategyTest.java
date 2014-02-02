/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.RDFNode;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.creation.CourseCreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlClasses;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlDatatypeProperties;
import de.bht.fb6.s778455.bachelor.test.framework.JenaFrameworkTest;
import de.bht.fb6.s778455.bachelor.test.framework.LoggingAwareTest;

/**
 * <p>
 * This class contains tests of the {@link CourseCreationStrategy}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 * 
 */
public class CourseCreationStrategyTest extends LoggingAwareTest implements IOwlClasses, IOwlDatatypeProperties {
    protected ACreationStrategy strategy;
    protected RdfTripleStoreAdapter adapter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.adapter = JenaFrameworkTest.getRdfTripleStoreAdapter();
        this.strategy = new CourseCreationStrategy( this.adapter );
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strategy = null;
        this.adapter = null;
    }

    @Test
    public void testCreateLmsInstance() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );

        final Method m = CourseCreationStrategy.class.getDeclaredMethod(
                "createLmsInstance", LmsCourseSet.class );
        m.setAccessible( true );

        m.invoke( this.strategy, courseSet );

        // assert statements for the courseSet are contained
        final Set< Individual > indis = this.adapter.getOntologieIndividuals();
        System.out.println("Indis: \n\n" + indis);
        
        boolean classMatched = false;
        for( final Individual individual : indis ) {
            System.out.println("indi uri: " + individual.getURI() + "\n");
            if( individual.getOntClass()
                    .equals(
                            this.adapter.getPureOntologyModel().getOntClass(
                                    CLASS_LMS ) ) ) {
                classMatched = true;
                final RDFNode title = individual.getPropertyValue( this.adapter.getPureOntologyModel().getProperty( PROPERTY_DATA_TITLE ) );
                
                // assert datatype of object 'title'
                assertTrue ( title.isLiteral() );
                assertTrue ( title.asLiteral().getDatatypeURI().equals( XSDDatatype.XSDstring.getURI() ));
                assertEquals ( "unittestcourseset", title.asLiteral().getString() );
            }
        }
        
        assertTrue( classMatched );
    }

}
