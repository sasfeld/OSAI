/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.moodle;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodlePostgreSqlImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * This class contains tests fo the {@link MoodlePostgreSqlImportStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 * 
 */
public class MoodlePostgreSqlImportStrategyTest {
	protected AImportStrategy importStrategy;
	protected File testFolder;

	/*
	 * ################################## # # test preparation #
	 * ##################################
	 */
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.importStrategy = new MoodlePostgreSqlImportStrategy();
		this.testFolder = new File( ServiceFactory.getConfigReader()
				.fetchValue( IConfigKeys.IMPORT_STRATEGY_POSTGRESQL_TESTFOLDER ) );

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.importStrategy = null;
	}

	/*
	 * ################################## # # tests #
	 * ##################################
	 */
	@Test
	public void testImportFromFile() throws GeneralLoggingException {
		Collection< Course > courses = this.importStrategy
				.importBoardFromFile( this.testFolder );

		assertTrue( null != courses );

		assertEquals( 2, courses.size() );
		// iterate through courses, assert constellation of instances
		int courseNum = 0;
		for( Course course : courses ) {
			if( 0 == courseNum ) {
				assertEquals( "Beuth Hochschule Test Moodle", course.getTitle() );
				assertEquals( "Beuth HS Test", course.getShortName() );
				assertTrue( 0 < course.getSummary().length() );
			} else {
				// iterate through boards, assert them
				if( 1 == courseNum ) {
					int boardNum = 0;
					for( Board board : course.getBoards() ) {
						if( 0 == boardNum ) {
							assertEquals( "Nachrichtenforum", board.getTitle() );
							assertEquals( "Nachrichten und Ankündigungen", board.getIntro() );
						}
						else {
							// iterate through threads
							if ( 1 == boardNum ) {
								int threadNum = 0;
								for( BoardThread thread: board.getBoardThreads()) {
									if ( 0 == threadNum ) {
										assertEquals( "Optionale Aufgaben", thread.getTitle() );
										assertEquals( board, thread.getBelongingBoard() );
										
										// iterate through postings
										for( Posting posting : thread.getPostings() ) {
											// only one posting exists in discussion = 1
											assertEquals( "Optionale Aufgaben", posting.getTitle() );
											assertTrue( 0 < posting.getContent().length());
										}
									}			
									else {
										assertTrue( null != thread.getTitle());
										assertTrue( null != thread.getBelongingBoard());
									}
									threadNum++;
								}				
								
							}
							assertTrue( null != board.getTitle());
							assertTrue( null != board.getIntro());
						}

						boardNum++;
					}
				}

				assertTrue( null != course.getTitle() );
				assertTrue( null != course.getShortName() );
				assertTrue( null != course.getSummary() );
			}

			courseNum++;
		}

		System.out.println( courses );
	}

}
