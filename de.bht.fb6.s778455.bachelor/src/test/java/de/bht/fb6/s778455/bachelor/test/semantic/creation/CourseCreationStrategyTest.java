/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.creation.ACreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.creation.CourseCreationStrategy;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IDCTerms;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlClasses;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlDatatypeProperties;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlIndividuals;
import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlObjectProperties;
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
public class CourseCreationStrategyTest extends LoggingAwareTest implements
        IOwlClasses, IOwlDatatypeProperties {
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
    public void testCreateEdges() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            URISyntaxException, GeneralLoggingException {
        // createCourseBoardEdges
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course course = new Course( "unittestcourse", courseSet );
        course.setWebUrl( "http://example.org" );
        course.setId( 1 );
        course.setLanguage( Language.GERMAN );
        courseSet.add( course );
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );
        board.setWebUrl( "http://board.example.org" );
        board.setLang( Language.GERMAN );
        course.addBoard( board );
        final BoardThread thread = new BoardThread( board );
        thread.setId( 1 );
        thread.setTitle( "Some nice topic" );
        thread.setWebUrl( "http://board.example.org/topic.php?id=1" );
        thread.setLang( Language.GERMAN );
        board.addThread( thread ); 
        final Posting posting = new Posting( thread );
        posting.setId( 1 );
        posting.setTitle( "Some nice posting" );
        posting.setLang( Language.ENGLISH );
        thread.addPosting( posting );

        this.strategy.createRdfTriples( courseSet );

        final Set< Statement > statements = this.adapter.getOntologyStatements();

        final Statement s1 = new StatementImpl( new ResourceImpl( course
                .getRdfUri().toString() ), new PropertyImpl(
                IOwlObjectProperties.PROPERTY_OBJECT_COURSE_BOARD ),
                new ResourceImpl( board.getRdfUri().toString() ) );
        final Statement s2 = new StatementImpl( new ResourceImpl( board
                .getRdfUri().toString() ), new PropertyImpl(
                        IOwlObjectProperties.PROPERTY_OBJECT_BOARD_THREAD ),
                        new ResourceImpl( thread.getRdfUri().toString() ) );
        final Statement s3 = new StatementImpl( new ResourceImpl( thread
                .getRdfUri().toString() ), new PropertyImpl(
                        IOwlObjectProperties.PROPERTY_OBJECT_THREAD_POSTING ),
                        new ResourceImpl( posting.getRdfUri().toString() ) );
        final Statement s4 = new StatementImpl( new ResourceImpl( thread
                .getRdfUri().toString() ), new PropertyImpl(
                        IOwlObjectProperties.PROPERTY_OBJECT_LANGUAGE ),
                        new ResourceImpl( IOwlIndividuals.INSTANCE_LANG_DE ) );
        final Statement s5 = new StatementImpl( new ResourceImpl( posting
                .getRdfUri().toString() ), new PropertyImpl(
                        IOwlObjectProperties.PROPERTY_OBJECT_LANGUAGE ),
                        new ResourceImpl( IOwlIndividuals.INSTANCE_LANG_EN ) );

        for( final Statement statement : statements ) {
            System.out.println("statement: " + statement + "\n");
        }
        assertTrue( statements.contains( s1 ) );
        assertTrue( statements.contains( s2 ) );
        assertTrue( statements.contains( s3 ) );
    }

    @Test
    public void testCreatePostingInstance() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course course = new Course( "unittestcourse", courseSet );
        course.setWebUrl( "http://example.org" );
        course.setId( 1 );
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );
        board.setWebUrl( "http://board.example.org" );
        final BoardThread thread = new BoardThread( board );
        thread.setId( 1 );
        thread.setTitle( "Some nice topic" );
        thread.setWebUrl( "http://board.example.org/topic.php?id=1" );
        final Posting posting = new Posting( thread );
        posting.setId( 1 );
        posting.setTitle( "Some nice posting" );

        final Method m = CourseCreationStrategy.class.getDeclaredMethod(
                "createPostingInstance", Posting.class );
        m.setAccessible( true );

        m.invoke( this.strategy, posting );

        // assert statements for the courseSet are contained
        final Set< Individual > indis = this.adapter.getOntologieIndividuals();
        System.out.println( "Indis: \n\n" + indis );

        boolean classMatched = false;
        for( final Individual individual : indis ) {
            System.out.println( "indi uri: " + individual.getURI() + "\n" );
            if( individual.getOntClass().equals(
                    this.adapter.getPureOntologyModel().getOntClass(
                            CLASS_POSTING ) ) ) {
                classMatched = true;
                final RDFNode title = individual.getPropertyValue( this.adapter
                        .getPureOntologyModel().getProperty(
                                PROPERTY_DATA_TITLE ) );

                // assert datatype of object 'title'
                assertTrue( title.isLiteral() );
                assertTrue( title.asLiteral().getDatatypeURI()
                        .equals( XSDDatatype.XSDstring.getURI() ) );
                assertEquals( "Some nice posting", title.asLiteral()
                        .getString() );
            }
        }

        assertTrue( classMatched );
    }

    @Test
    public void testCreateThreadInstance() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course course = new Course( "unittestcourse", courseSet );
        course.setWebUrl( "http://example.org" );
        course.setId( 1 );
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );
        board.setWebUrl( "http://board.example.org" );
        final BoardThread thread = new BoardThread( board );
        thread.setId( 1 );
        thread.setTitle( "Some nice topic" );
        thread.setWebUrl( "http://board.example.org/topic.php?id=1" );

        final Method m = CourseCreationStrategy.class.getDeclaredMethod(
                "createThreadInstance", BoardThread.class );
        m.setAccessible( true );

        m.invoke( this.strategy, thread );

        // assert statements for the courseSet are contained
        final Set< Individual > indis = this.adapter.getOntologieIndividuals();
        System.out.println( "Indis: \n\n" + indis );

        boolean classMatched = false;
        for( final Individual individual : indis ) {
            System.out.println( "indi uri: " + individual.getURI() + "\n" );
            if( individual.getOntClass().equals(
                    this.adapter.getPureOntologyModel().getOntClass(
                            CLASS_THREAD ) ) ) {
                classMatched = true;
                final RDFNode title = individual.getPropertyValue( this.adapter
                        .getPureOntologyModel().getProperty(
                                PROPERTY_DATA_TITLE ) );

                // assert datatype of object 'title'
                assertTrue( title.isLiteral() );
                assertTrue( title.asLiteral().getDatatypeURI()
                        .equals( XSDDatatype.XSDstring.getURI() ) );
                assertEquals( "Some nice topic", title.asLiteral().getString() );

                // assert datatype 'property_data_web_url'
                final RDFNode webUrl = individual
                        .getPropertyValue( this.adapter.getPureOntologyModel()
                                .getProperty( PROPERTY_DATA_WEB_URI ) );
                assertTrue( webUrl.isLiteral() );
                assertTrue( webUrl.asLiteral().getDatatypeURI()
                        .equals( IDCTerms.DCTERMS_URI ) );
                assertEquals( "http://board.example.org/topic.php?id=1", webUrl
                        .asLiteral().getString() );
            }
        }

        assertTrue( classMatched );
    }

    @Test
    public void testCreateBoardInstance() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course course = new Course( "unittestcourse", courseSet );
        course.setWebUrl( "http://example.org" );
        course.setId( 1 );
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );
        board.setWebUrl( "http://board.example.org" );

        final Method m = CourseCreationStrategy.class.getDeclaredMethod(
                "createBoardInstance", Board.class );
        m.setAccessible( true );

        m.invoke( this.strategy, board );

        // assert statements for the courseSet are contained
        final Set< Individual > indis = this.adapter.getOntologieIndividuals();
        System.out.println( "Indis: \n\n" + indis );

        boolean classMatched = false;
        for( final Individual individual : indis ) {
            System.out.println( "indi uri: " + individual.getURI() + "\n" );
            if( individual.getOntClass().equals(
                    this.adapter.getPureOntologyModel().getOntClass(
                            CLASS_BOARD ) ) ) {
                classMatched = true;
                final RDFNode title = individual.getPropertyValue( this.adapter
                        .getPureOntologyModel().getProperty(
                                PROPERTY_DATA_TITLE ) );

                // assert datatype of object 'title'
                assertTrue( title.isLiteral() );
                assertTrue( title.asLiteral().getDatatypeURI()
                        .equals( XSDDatatype.XSDstring.getURI() ) );
                assertEquals( "unittestboard", title.asLiteral().getString() );

                // assert datatype 'property_data_web_url'
                final RDFNode webUrl = individual
                        .getPropertyValue( this.adapter.getPureOntologyModel()
                                .getProperty( PROPERTY_DATA_WEB_URI ) );
                assertTrue( webUrl.isLiteral() );
                assertTrue( webUrl.asLiteral().getDatatypeURI()
                        .equals( IDCTerms.DCTERMS_URI ) );
                assertEquals( "http://board.example.org", webUrl.asLiteral()
                        .getString() );
            }
        }

        assertTrue( classMatched );
    }

    @Test
    public void testCreateCourseInstance() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final LmsCourseSet courseSet = new LmsCourseSet( "unittestcourseset" );
        final Course course = new Course( "unittestcourse", courseSet );
        course.setWebUrl( "http://example.org" );
        course.setId( 1 );

        final Method m = CourseCreationStrategy.class.getDeclaredMethod(
                "createCourseInstance", Course.class );
        m.setAccessible( true );

        m.invoke( this.strategy, course );

        // assert statements for the courseSet are contained
        final Set< Individual > indis = this.adapter.getOntologieIndividuals();
        System.out.println( "Indis: \n\n" + indis );

        boolean classMatched = false;
        for( final Individual individual : indis ) {
            System.out.println( "indi uri: " + individual.getURI() + "\n" );
            if( individual.getOntClass().equals(
                    this.adapter.getPureOntologyModel().getOntClass(
                            CLASS_COURSE ) ) ) {
                classMatched = true;
                final RDFNode title = individual.getPropertyValue( this.adapter
                        .getPureOntologyModel().getProperty(
                                PROPERTY_DATA_TITLE ) );

                // assert datatype of object 'title'
                assertTrue( title.isLiteral() );
                assertTrue( title.asLiteral().getDatatypeURI()
                        .equals( XSDDatatype.XSDstring.getURI() ) );
                assertEquals( "unittestcourse", title.asLiteral().getString() );

                // assert datatype 'property_data_web_url'
                final RDFNode webUrl = individual
                        .getPropertyValue( this.adapter.getPureOntologyModel()
                                .getProperty( PROPERTY_DATA_WEB_URI ) );
                assertTrue( webUrl.isLiteral() );
                assertTrue( webUrl.asLiteral().getDatatypeURI()
                        .equals( IDCTerms.DCTERMS_URI ) );
                assertEquals( "http://example.org", webUrl.asLiteral()
                        .getString() );
            }
        }

        assertTrue( classMatched );
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
        System.out.println( "Indis: \n\n" + indis );

        boolean classMatched = false;
        for( final Individual individual : indis ) {
            System.out.println( "indi uri: " + individual.getURI() + "\n" );
            if( individual.getOntClass()
                    .equals(
                            this.adapter.getPureOntologyModel().getOntClass(
                                    CLASS_LMS ) ) ) {
                classMatched = true;
                final RDFNode title = individual.getPropertyValue( this.adapter
                        .getPureOntologyModel().getProperty(
                                PROPERTY_DATA_TITLE ) );

                // assert datatype of object 'title'
                assertTrue( title.isLiteral() );
                assertTrue( title.asLiteral().getDatatypeURI()
                        .equals( XSDDatatype.XSDstring.getURI() ) );
                assertEquals( "unittestcourseset", title.asLiteral()
                        .getString() );
            }
        }

        assertTrue( classMatched );
    }

}
