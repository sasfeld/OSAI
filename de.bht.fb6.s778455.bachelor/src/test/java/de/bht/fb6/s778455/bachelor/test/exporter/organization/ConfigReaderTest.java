/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.exporter.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>This class contains tests of the {@link de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader} in the importer module.</p>
 * <p>It's an integration test of the conf/exporter.properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 24.11.2013
 *
 */
public class ConfigReaderTest extends NoLoggingTest {
	protected IConfigReader configReader;

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
		this.configReader = new de.bht.fb6.s778455.bachelor.exporter.organization.ConfigReader();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.configReader = null;
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
	 * Test of the method:
	 * @see de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader#fetchValues()
	 */
	public void testFetchValues() {
		Map< String, String > configValues = this.configReader.fetchValues();
		
		// assert size -> force the devloper to check this test before he manipulates the configuration
		assertEquals( 5, configValues.size() );
		
		// assert properties' keys
		assertTrue( configValues.containsKey( IConfigKeys.EXPORT_STRATEGY_CLASS) );
		assertTrue( configValues.containsKey( IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_DATAFOLDER) );
		assertTrue( configValues.containsKey( IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_TESTFOLDER) );
		assertTrue( configValues.containsKey( IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_ENCODING) );
	
	}	
	
	@Test
	/**
	 * Test of the method:
	 * @see de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader#fetchValue()
	 */
	public void testFetchValue() {
		// assert properties' keys
		assertTrue ( 0 < this.configReader.fetchValue( IConfigKeys.EXPORT_STRATEGY_CLASS ).length() );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFetchValueException() {
		this.configReader.fetchValue( "this property doesn't exist" );
	}
	
	@Test
	/**
	 * Test the configured classes.
	 * Ensure that they exist and extend special abstract classes or implement interfaces.
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void testConfiguredClasses() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// importer strategy
		String exporterStrategy = this.configReader.fetchValue( IConfigKeys.EXPORT_STRATEGY_CLASS );
		Class< ? > className = Class.forName( exporterStrategy );
		Constructor< ? > constructor = className.getConstructor( );
		Object strategyObject = constructor.newInstance(  );
		
		assertTrue( null != exporterStrategy );
		assertTrue ( strategyObject instanceof AExportStrategy );
	}
}
