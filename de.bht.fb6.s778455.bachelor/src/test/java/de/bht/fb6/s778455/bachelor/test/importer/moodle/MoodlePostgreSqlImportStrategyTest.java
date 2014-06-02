/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.moodle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests fo the {@link MoodlePostgreSqlImportStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 * 
 */
public class MoodlePostgreSqlImportStrategyTest extends NoLoggingTest {
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
		final Collection< Course > courses = this.importStrategy
				.importBoardFromFile( this.testFolder );

		assertTrue( null != courses );

		assertEquals( 2, courses.size() );
		// iterate through courses, assert constellation of instances
		int courseNum = 0;
		for( final Course course : courses ) {
			if( 0 == courseNum ) {
				assertTrue( 0 < course.getTitle().length() );
				assertTrue( 0 < course.getShortName().length() );
				assertTrue( 0 < course.getSummary().length() );
			} else {
				// iterate through boards, assert them
				if( 1 == courseNum ) {
					int boardNum = 0;
					for( final Board board : course.getBoards() ) {
						if( 0 == boardNum ) {
							assertEquals( "Nachrichtenforum", board.getTitle() );
							assertTrue( null != board.getIntro() );
						}
						else {
							// iterate through threads
							if ( 1 == boardNum ) {
								int threadNum = 0;
								for( final BoardThread thread: board.getBoardThreads()) {
									if ( 0 == threadNum ) {
										assertEquals( "Optionale Aufgaben", thread.getTitle() );
										assertEquals( board, thread.getBelongingBoard() );
										
										// iterate through postings
										for( final Posting posting : thread.getPostings() ) {
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
	
	@Test
	public void testFillFromFile() throws GeneralLoggingException {
		final Collection< Course > courses = this.importStrategy
				.importBoardFromFile( this.testFolder );
		
		for( final Course course : courses ) {
			System.out.println("++++++++++++++++++++++++++++");
			System.out.println("course name: " + course.getTitle());
			System.out.println("Corpus: \n " + course.getPersonNameCorpus());
			System.out.println("++++++++++++++++++++++++++++");
		}
	}

}
