/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

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
		assertEquals( 131, configValues.size() );
		
		// assert properties' keys
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_DEWAC_FILE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_ENGLISH_3CLASS_FILE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_ENGLISH_4CLASS_FILE) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_ENGLISH_7CLASS_FILE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_ENGLISH_PRIMARY ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_PRIMARY ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_CASCADE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_ENGLISH_CASCADE ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_PRIMARY ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_GERMAN_STRATEGY_CLASS ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_NER_ENGLISH_STRATEGY_CLASS ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_STRATEGY_CHAIN ) );
		assertTrue( configValues.containsKey( IConfigKeys.ANONYM_GREETINGS_NUMBEROFLINES ) );
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
	
	@Test
	public void testFetchMultipleValues() throws InvalidConfigException {
		// test german corpus config keys (format: a.b.c.1 = property and a.b.c.2 = property2)
		List< String > germanCorpora = this.configReader.fetchMultipleValues( IConfigKeys.ANONYM_NER_GERMAN_CORPORA );
		
		assertTrue( 2 == germanCorpora.size() );	
		
		// test config keys pointing to a property which is comma-separated
		List< String > germanCorporaCascade = this.configReader.fetchMultipleValues( IConfigKeys.ANONYM_NER_GERMAN_CASCADE );
		assertTrue( germanCorporaCascade.size() >= 1 );
		
		// german greetings
		List< String > germanGreetings = this.configReader.fetchMultipleValues( IConfigKeys.ANONYM_GREETINGS_GERMAN );
		
		assertTrue( 1 < germanGreetings.size() );	
	}
	
	@Test(expected = InvalidConfigException.class)
	public void testFetchMultipleValuesException() throws InvalidConfigException {
		this.configReader.fetchMultipleValues( "doesntexist" );
	}
	
	@Test
	public void testFetchMultipleValuesSingleKey() throws InvalidConfigException {		
		// test single property key -> shouldn't point to a list of values
		List< String > singleValueList = this.configReader.fetchMultipleValues( IConfigKeys.ANONYM_NER_GERMAN_DEWAC_FILE );
		
		assertTrue( 1 == singleValueList.size());
	}
	
	@Test
	public void testGetConfiguredClass() throws InvalidConfigException {
		// test for GermanNerAnonymizationStrategy
		ConfigReader configReader = (ConfigReader) this.configReader;
		AAnomyzationStrategy strategy =  configReader.<AAnomyzationStrategy>getConfiguredClass( IConfigKeys.ANONYM_NER_GERMAN_STRATEGY_CLASS, new File( this.configReader.fetchValue( IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE )));
		
		assertTrue( null != strategy );
	}
}
