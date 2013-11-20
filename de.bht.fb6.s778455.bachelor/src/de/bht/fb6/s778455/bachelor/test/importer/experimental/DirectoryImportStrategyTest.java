/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.experimental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>This is a test of the {@link DirectoryImportStrategy} class.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class DirectoryImportStrategyTest {
	protected AImportStrategy importStrategy;

	/*
	 * ##################################
	 * #
	 * # test preparation
	 * #
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
	 * ##################################
	 * #
	 * # tests
	 * #
	 * ##################################
	 */
	@Test
	/**
	 * Test of DirectoryImportStrategy#importFromFile(). 
	 */
	public void testImportFromFile() throws GeneralLoggingException {
		File testDir = new File( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_TESTFOLDER ) );
		
		Map< String, Board > resultingMap = this.importStrategy.importFromFile( testDir );
		
		assertTrue( null != resultingMap );
		
		// assert board/course names
		assertTrue ( resultingMap.keySet().contains( "Test course" ));
		
		// assert threads within a course
		assertTrue( null != resultingMap.get( "Test course" ));
		assertTrue ( null != resultingMap.get( "Test course" ).getBoardThreads());
		
		int i = 0;
		for (BoardThread boardThread : resultingMap.get( "Test course" ).getBoardThreads()) {
			if ( 0 == i ) {
				assertEquals( "Test thread", boardThread.getTitle() );
			}
		}
	}

}
