/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.creation.tags;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.creation.CourseCreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.creation.tags.TopicZoomTagInsertionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlDatatypeProperties;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlObjectProperties;
import de.bht.fb6.s778455.bachelor.test.framework.JenaFrameworkTest;
import de.bht.fb6.s778455.bachelor.test.framework.LoggingAwareTest;

/**
 * <p>
 * This class contains tests of the {@link TopicZoomTagInsertionStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 * 
 */
public class TopicZoomTagInsertionStrategyTest extends LoggingAwareTest implements
        IOwlDatatypeProperties, IOwlObjectProperties {

    private RdfTripleStoreAdapter adapter;
    private TopicZoomTagInsertionStrategy strategy;
    private CourseCreationStrategy dependingStrategy;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.adapter = JenaFrameworkTest.getRdfTripleStoreAdapter();
        this.strategy = new TopicZoomTagInsertionStrategy( this.adapter );
        this.dependingStrategy = new CourseCreationStrategy(  this.adapter );
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.adapter = null;
        this.strategy = null;
    }

    @Test
    public void testCreateRdfTriples() throws GeneralLoggingException, URISyntaxException {
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course testCourse = new Course( "unittestcourse", courseSet );
        final TopicZoomTag tzTag = new TopicZoomTag( 6, 3, 7, "unittest",
                "http://notexisting.tld/unittest" );
        testCourse.addTag( tzTag, TagType.TOPIC_ZOOM );
        courseSet.add( testCourse );
        
        // first, let course creation strategy fill the model with individuals
        this.dependingStrategy.createRdfTriples( courseSet );
        
        // now, fill the model with topic triples
        this.strategy.createRdfTriples( courseSet );
        
        // make sure that topic node is enriched with information
        assertTrue( this.adapter.getPureOntologyModel().contains(
                this.adapter.getPureOntologyModel().getResource(
                        tzTag.getRdfUri().toString() ),
                this.adapter.getPureOntologyModel()
                        .getProperty( PROPERTY_DATA_TITLE ) ) );
        assertTrue( this.adapter.getPureOntologyModel().contains(
                this.adapter.getPureOntologyModel().getResource(
                        tzTag.getRdfUri().toString() ),
                        this.adapter.getPureOntologyModel()
                        .getProperty( PROPERTY_OBJECT_TOPIC_URI ) ) );
        
        // make sure that course and topic are connected
        assertTrue( this.adapter.getPureOntologyModel().contains( 
                this.adapter.getPureOntologyModel().getResource( testCourse.getRdfUri().toString() ), 
                this.adapter.getPureOntologyModel().getProperty( PROPERTY_OBJECT_HASTOPIC ),
                this.adapter.getPureOntologyModel().getResource( tzTag.getRdfUri().toString() ) ));
        
    }
    
    @Test
    public void testAddNodesForTzTags() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, URISyntaxException {
        final Method m = this.strategy.getClass().getDeclaredMethod(
                "addNodesForTzTags", LmsCourseSet.class );
        m.setAccessible( true );

        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course testCourse = new Course( "unittestcourse", courseSet );
        final TopicZoomTag tzTag = new TopicZoomTag( 6, 3, 7, "unittest",
                "http://notexisting.tld/unittest" );
        testCourse.addTag( tzTag, TagType.TOPIC_ZOOM );
        courseSet.add( testCourse );

        m.invoke( this.strategy, courseSet );

        assertTrue( this.adapter.getPureOntologyModel().contains(
                this.adapter.getPureOntologyModel().getResource(
                        tzTag.getRdfUri().toString() ),
                this.adapter.getPureOntologyModel()
                        .getProperty( PROPERTY_DATA_TITLE ) ) );
        assertTrue( this.adapter.getPureOntologyModel().contains(
                this.adapter.getPureOntologyModel().getResource(
                        tzTag.getRdfUri().toString() ),
                        this.adapter.getPureOntologyModel()
                        .getProperty( PROPERTY_OBJECT_TOPIC_URI ) ) );
    }

}
