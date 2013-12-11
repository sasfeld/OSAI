/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.controller;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.controller.AnonymizationController;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

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
		Collection< Course > anonymizedCourses = this.anonymizationController
				.performAnonymization();
		assertTrue( null != anonymizedCourses );

		// some sysouts
		for( Course course : anonymizedCourses ) {
			System.out.println( "##########################" );
			System.out.println( "#" );
			System.out.println( "# course: " + course.getTitle() );
			for( Board courseBoard : course.getBoards() ) {
				System.out.println( "........................." );
				System.out.println( "." );
				System.out.println(". board: " + courseBoard.getTitle());
				for( BoardThread boardThread : courseBoard.getBoardThreads() ) {
					System.out.println( "+++++++++++++++++++++++++++" );
					System.out.println( "+" );
					System.out.println( "+ thread: " + boardThread );

					for( Posting posting : boardThread.getPostings() ) {
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
