/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.auditorium;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.auditorium.AuditoriumDbQuerier;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * This class contains tests of the {@link AuditoriumDbQuerier}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 * 
 */
public class AuditoriumDbQuerierTest {

	protected AuditoriumDbQuerier dbQuerier;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.dbQuerier = new AuditoriumDbQuerier();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.dbQuerier = null;
	}

	@Test
	public void testFetchCourses() throws GeneralLoggingException {
		Map< Integer, Course > fetchedCourses = this.dbQuerier.fetchCourses();

		assertTrue( null != fetchedCourses );

		assertTrue( 0 < fetchedCourses.size() );

		System.out.println( "Number of fetched courses: "
				+ fetchedCourses.size() );

		for( Course course : fetchedCourses.values() ) {
			assertTrue( 0 != course.getId() );
			System.out.println( course );
		}

		// test fetchBoards
		Map< Integer, Board > fetchedBoards = this.dbQuerier
				.fetchBoards( fetchedCourses );

		assertTrue( null != fetchedBoards );

		assertTrue( 0 < fetchedBoards.size() );

		System.out
				.println( "Number of fetched boards: " + fetchedBoards.size() );

		for( Board board : fetchedBoards.values() ) {
			assertTrue( 0 != board.getId() );
			System.out.println( board );
		}

		// test fetchBoardThreads
		Map< Integer, BoardThread > fetchedThreads = this.dbQuerier
				.fetchBoardThreads( fetchedBoards );
		
		assertTrue ( null != fetchedThreads );
		assertTrue ( 0 < fetchedThreads.size() );
		
		System.out.println(" Number of fetched threads: " + fetchedThreads.size());
		
		for( BoardThread thread: fetchedThreads.values() ) {
			assertTrue( 0 != thread.getId() );
			System.out.println( thread );
		}
		
		// test fetchPostings
		Collection< Posting > fetchedPostings = this.dbQuerier.fetchPostings( fetchedThreads );
		
		assertTrue( null != fetchedPostings );
		assertTrue( 0 < fetchedPostings.size() );
		
		System.out.println("Number of fetched postings: " + fetchedPostings.size());
		
		for( Posting posting : fetchedPostings ) {
			assertTrue ( 0 != posting.getId());
			System.out.println(posting);
		}
	}

}
