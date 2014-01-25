/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.controller;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.controller.AnonymizationController;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * This class contains UnitTests of the {@link AnonymizationController}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class AnonymizationControllerTest {
	protected AnonymizationController anonymizationController;

	/*
	 * ################################## # # test preparation #
	 * ##################################
	 */
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.anonymizationController = new AnonymizationController();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.anonymizationController = null;
	}

	/*
	 * ################################## # # tests #
	 * ##################################
	 */
	@Test
	/**
	 * This is a general test of the anonymization controller.
	 * @throws GeneralLoggingException
	 */
	public void testPerformAnonymization() throws GeneralLoggingException {
		final AImportStrategy strategy = new DirectoryImportStrategy();
		final LmsCourseSet courses = strategy.importBoardFromFile( new File( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_TESTFOLDER ) ));
		final LmsCourseSet anonymizedCourses = this.anonymizationController
				.performAnonymization(courses );
		assertTrue( null != anonymizedCourses );

		// some sysouts
		for( final Course course : anonymizedCourses ) {
			System.out.println( "##########################" );
			System.out.println( "#" );
			System.out.println( "# course: " + course.getTitle() );
			for( final Board courseBoard : course.getBoards() ) {
				System.out.println( "........................." );
				System.out.println( "." );
				System.out.println(". board: " + courseBoard.getTitle());
				for( final BoardThread boardThread : courseBoard.getBoardThreads() ) {
					System.out.println( "+++++++++++++++++++++++++++" );
					System.out.println( "+" );
					System.out.println( "+ thread: " + boardThread );

					for( final Posting posting : boardThread.getPostings() ) {
						System.out.println( "---------------------------" );
						System.out.println( "-" );
						System.out.println( "- posting: " + posting );
						System.out.println( "-" );
						System.out.println( "---------------------------" );
					}

					System.out.println( "+" );
					System.out.println( "+++++++++++++++++++++++++++" );
				}
				System.out.println( "." );
				System.out.println( "........................." );
			}
			System.out.println( "#" );
			System.out.println( "##########################" );
		}

	}

}
