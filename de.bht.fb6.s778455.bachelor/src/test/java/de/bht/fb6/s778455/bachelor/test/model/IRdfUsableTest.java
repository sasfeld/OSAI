/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.model;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.IRdfUsable;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;

/**
 * <p>
 * This class contains tests of the {@link IRdfUsable} implementation.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 * 
 */
public class IRdfUsableTest {
    protected static String COURSE_SET_NAME = "unittestcourseset";
    protected LmsCourseSet courseSet;

    @Before
    public void setUp() throws Exception {
        this.courseSet = new LmsCourseSet( COURSE_SET_NAME );
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.courseSet = null;
    }

    @Test
    public void testForCourse() throws URISyntaxException, MalformedURLException {
        final Course course = new Course( "unittestcourse", this.courseSet );
        course.setId( 1 );
        final IRdfUsable rdfUsable = course;

        URI expected = new java.net.URI(
                "http://saschafeldmann.de/bachelor/ontology/individuals/"
                        + this.courseSet.getName() + "/" + course.getId() );

        assertEquals( expected, rdfUsable.getRdfUri() );
        
        // assert that predefined uris are prefered
        course.setWebUrl( new URL ("http://example.org" ) );        
        expected = new URI( "http://example.org" );        
        assertEquals( expected, rdfUsable.getRdfUri() );
    }
    
    @Test
    public void testForBoard() throws URISyntaxException {
        final Course course = new Course( "unittestcourse", this.courseSet );
        course.setId( 1 );
        
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );
        final IRdfUsable rdfUsable = board;
        
        final URI expected = new java.net.URI(
                "http://saschafeldmann.de/bachelor/ontology/individuals/"
                        + this.courseSet.getName() +  "/" + course.getId() +"/board" + "/" + board.getId() );
        
        assertEquals( expected, rdfUsable.getRdfUri() );      
    }
    
    @Test
    public void testForBoardThread() throws URISyntaxException {
        final Course course = new Course( "unittestcourse", this.courseSet );
        course.setId( 1 );
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );
        final BoardThread boardThread = new BoardThread( board );
        final IRdfUsable rdfUsable = boardThread;
        
        final URI expected = new java.net.URI(
                "http://saschafeldmann.de/bachelor/ontology/individuals/"
                        + this.courseSet.getName() +  "/" + course.getId() + "/boardthread" + "/" + boardThread.getId() );
        
        assertEquals( expected, rdfUsable.getRdfUri() );      
    }
    
    @Test
    public void testForPosting() throws URISyntaxException {
        final Course course = new Course( "unittestcourse", this.courseSet );
        course.setId( 1 );
        final Board board = new Board( course, "unittestboard" );
        board.setId( 1 );        
        final BoardThread boardThread = new BoardThread( board );
        final Posting posting = new Posting( boardThread );
        final IRdfUsable rdfUsable = posting;
        
        final URI expected = new java.net.URI(
                "http://saschafeldmann.de/bachelor/ontology/individuals/"
                        + this.courseSet.getName() + "/" + course.getId() + "/posting" + "/" + posting.getId() );
        
        assertEquals( expected, rdfUsable.getRdfUri() );      
    }

}
