/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;

/**
 * <p>This class contains tests of the {@link ConfigReader} in the anonymization module.</p>
 * <p>It's an integration test of the conf/anonymization.properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.11.2013
 *
 */
public class ConfigReaderTest {
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
		this.configReader = new ConfigReader();
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
	 * @see de.bht.fb6.s778455.bachelor.organization.anonymization.ConfigReader#fetchValues()
	 */
	public void testFetchValues() {
		Map< String, String > configValues = this.configReader.fetchValues();
		
		// assert size -> force the devloper to check this test before he manipulates the configuration
		assertEquals( 2, configValues.size() );
		
		// assert properties' keys
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_DEWAC_FILE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE ) );
	}
	
	@Test
	/**
	 * Test of the method:
	 * @see de.bht.fb6.s778455.bachelor.organization.anonymization.ConfigReader#fetchValue()
	 */
	public void testFetchValue() {
		// assert properties' keys
		assertTrue ( 0 < this.configReader.fetchValue( IConfigKeys.ANONYM_NER_GERMAN_DEWAC_FILE ).length() );
		assertTrue ( 0 < this.configReader.fetchValue( IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE ).length() );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFetchValueException() {
		this.configReader.fetchValue( "this property doesn't exist" );
	}
}