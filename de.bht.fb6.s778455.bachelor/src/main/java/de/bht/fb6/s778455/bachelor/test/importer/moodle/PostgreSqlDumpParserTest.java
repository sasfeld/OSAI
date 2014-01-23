/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.moodle;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.moodle.PostgreSqlDumpParser;

/**
 * <p>This class contains tests of the {@link PostgreSqlDumpParser}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 *
 */
public class PostgreSqlDumpParserTest {
	/**
	 * Determine the path to the test postgre sql dump here.
	 */
	private static final String PATH_TO_TESTDUMP = "./data/importer/moodle-dumps/unittest/mdl_course.sql";
	protected File testFile;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.testFile = new File( PATH_TO_TESTDUMP );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.testFile = null;
	}

	@Test
	public void testFetchEntities() {
		PostgreSqlDumpParser parser = new PostgreSqlDumpParser( this.testFile );
		
		List< Map < String, String > > entities = parser.fetchEntities( "mdl_course", "completionnotify", "id", "fullname", "shortname", "summary" );
	
		System.out.println("resulting:\n" + entities);
		assertEquals( 2, entities.size() );
		
		// assert that each fetched entity contains the required columns
		for( Map< String, String > entity : entities ) {
			assertTrue( entity.containsKey( "id" ) );
			assertTrue( entity.containsKey( "fullname" ) );
			assertTrue( entity.containsKey( "shortname" ) );
			assertTrue( entity.containsKey( "summary" ) );
		}
	}

}
