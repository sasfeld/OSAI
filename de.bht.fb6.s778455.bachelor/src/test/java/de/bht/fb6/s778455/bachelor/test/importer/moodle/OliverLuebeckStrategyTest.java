/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.test.importer.moodle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.OliverLuebeckStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * Unit tests of the {@link OliverLuebeckStrategy}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.05.2014
 * 
 */
public class OliverLuebeckStrategyTest {
	/**
	 * Path to the folder containing the testdata to be imported.
	 * This is hard-coded and not in a config file because we don't want to have unit test configuration in production config files.
	 */
	protected static final String UNIT_TEST_BOARD_PATH = "./data/unittests/importer/oliver_luebeck_test";

	protected AImportStrategy importStrategy; 
	protected File boardDirectory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.importStrategy = ServiceFactory.newOliverLuebeckStrategy();
		this.boardDirectory = new File(UNIT_TEST_BOARD_PATH);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.importStrategy = null;
		this.boardDirectory = null;
	}

	@Test
	public void testImportBoardFromFile() throws GeneralLoggingException {
		final LmsCourseSet courses = this.importStrategy
				.importBoardFromFile( this.boardDirectory );

		assertTrue( null != courses );

		assertEquals( 2, courses.size() );
		// iterate through courses, assert constellation of instances
		int courseNum = 0;
		for( final Course course : courses ) {
			if( 0 == courseNum ) {
				assertEquals("oliver_luebeck_course_1", course.getTitle());
				assertTrue( null == course.getShortName());
				assertTrue( null == course.getSummary() );
				assertEquals( Language.GERMAN, course.getLanguage() );					
		
				int boardNum = 0;
				for( final Board board : course.getBoards() ) {
					assertEquals(course, board.getBelongingCourse());
					if( 0 == boardNum ) {
						assertEquals( "forum1", board.getTitle() );
						assertEquals( 1, board.getId() );
						assertEquals(Language.GERMAN, board.getLang());
						
						assertEquals(null, board.getType());
						assertEquals(null, board.getIntro());
						assertEquals(null, board.getCreationDate());
						assertEquals(null, board.getModificationDate());
						assertEquals(null, board.getWebUrl());
						
						// iterate through threads
						int threadNum = 0;
						for( final BoardThread thread: board.getBoardThreads()) {
							if ( 5 == threadNum ) {
								assertEquals( "Mehr ueber generische Typen", thread.getTitle() );
								assertEquals(1, thread.getId());
								assertEquals( board, thread.getBelongingBoard() );
								assertEquals(Language.GERMAN, thread.getLang());
								assertEquals(1, thread.getFirstPostingId());
								assertEquals(null, thread.getWebUrl());
								assertEquals(null, thread.getCreationDate());
								assertEquals(null, thread.getModificationDate());
								assertEquals(null, thread.getEndDate());
								
								// iterate through postings
								int postingNum = 0;
								for( final Posting posting : thread.getPostings() ) {
									if( 0 == postingNum ) {
										assertEquals( 1, posting.getId() );
										assertEquals(null, posting.getParentPostingId());
										// we don't want CDATA annotations in the content
										assertEquals(-1, posting.getContent().indexOf("<![CDATA["));
										SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");
										assertEquals("12/11/2007", format.format(posting.getCreationDate()));
										assertEquals(Language.GERMAN, posting.getLang());
										
										assertEquals(null, thread.getWebUrl());
									}
									// make sure that parent posting is 1
									if( 1 == postingNum ) {								
										assertEquals(new Long(1), posting.getParentPostingId());
									}
									
									
									postingNum++;
								}
							}			
							threadNum++;
						}				
					}
					
					boardNum++;
				}				
			}
			courseNum++;
		}

	}

}
