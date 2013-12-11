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
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>This class ....</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 *
 */
public class MoodlePostgreSqlImportStrategyTest {
	protected AImportStrategy importStrategy;
	protected File testFolder;

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
		this.importStrategy = new MoodlePostgreSqlImportStrategy();
		this.testFolder = new File( ServiceFactory.getConfigReader().fetchValue( IConfigKeys.IMPORT_STRATEGY_POSTGRESQL_TESTFOLDER ) );
		
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
	public void testImportFromFile() throws GeneralLoggingException {
		Collection< Course > courses = this.importStrategy.importBoardFromFile( this.testFolder );
		
		assertTrue( null != courses);
		
		System.out.println(courses);
	}

}
