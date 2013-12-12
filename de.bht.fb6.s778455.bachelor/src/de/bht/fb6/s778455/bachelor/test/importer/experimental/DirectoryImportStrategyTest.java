/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.experimental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * This is a test of the {@link DirectoryImportStrategy} class.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class DirectoryImportStrategyTest {
	protected AImportStrategy importStrategy;

	/*
	 * ################################## # # test preparation #
	 * ##################################
	 */
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.importStrategy = new DirectoryImportStrategy();
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
	/**
	 * Test of DirectoryImportStrategy#importFromFile(). 
	 */
	public void testImportFromFile() throws GeneralLoggingException {
		File testDir = new File( ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_TESTFOLDER ) );

		Collection< Course > resultingSet = this.importStrategy
				.importBoardFromFile( testDir );
		
		System.out.println(resultingSet);

		assertTrue( null != resultingSet );

		// assert board/course names
		for( Course course : resultingSet ) {
			// there should only be one course
			assertTrue( course.getTitle().equals( "Test course" ) );

			for( Board board : course.getBoards() ) {
				// there should be only one board
				assertTrue( board.getTitle().equals( "Test board" ) );
				assertTrue( null != board.getBoardThreads() );
				assertTrue( 2 == board.getBoardThreads().size() );

				int i = 0;
				// test sorting of threads (the timestamps from the postings are
				// used to determine the creation date)
				for( BoardThread boardThread : board
						.getBoardThreads() ) {
					if( 0 == i ) { // timestamp is smaller -> so it should be
									// first in the list
						assertEquals( "Test thread", boardThread.getTitle() );
						assertEquals( 1384093141, boardThread.getCreationDate()
								.getTime() );
					} else if( 1 == i ) { // timestamp is greater -> so it
											// should be last in the list
						assertEquals( "1 Another test thread",
								boardThread.getTitle() );
						assertEquals( 1384093191, boardThread.getCreationDate()
								.getTime() );
					}
					i++;
				}

				// assert postings within a thread
				List< Posting > postings = board
						.getBoardThreads().get( 0 ).getPostings();
				assertTrue( null != postings );
				assertTrue( 2 == postings.size() );

				i = 0;
				for( Posting posting : postings ) {
					if( 0 == i ) {
						assertEquals( 1384093141, posting.getCreationDate()
								.getTime() );
						assertTrue( posting.getContent().contains(
								"Das ist nur ein Dummy-Content." ) );
						assertTrue( posting
								.getTaggedContent()
								.contains(
										"This is only a <I-PERS>dummy</I-PERS> content." ) );
						assertTrue( posting.getContent().contains(
								"This is only a dummy content." ) );
					}
					i++;
				}
			}
		}

	}

	@Test
	public void testFillFromFile() throws GeneralLoggingException {
		PersonNameCorpus bareCorpus = new PersonNameCorpus();
		bareCorpus = this.importStrategy.fillFromFile( new File(
				"./data/anonymization/personnames/testprenames.txt" ),
				bareCorpus, PersonNameType.PRENAME );

		assertEquals( 22, bareCorpus.getPrenames().size() );

		assertTrue( bareCorpus.isPrename( "Aryan", false ) );
		assertTrue( bareCorpus.isPrename( "aryan", false ) );
	}
}
