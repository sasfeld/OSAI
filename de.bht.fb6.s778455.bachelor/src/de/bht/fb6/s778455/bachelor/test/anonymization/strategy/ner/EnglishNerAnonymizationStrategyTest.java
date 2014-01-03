/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy.ner;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.EnglishNerAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * This class contains tests of the {@link EnglishNerAnonymizationStrategyTest}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.12.2013
 * 
 */
public class EnglishNerAnonymizationStrategyTest {
	protected AAnomyzationStrategy strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.strategy = new EnglishNerAnonymizationStrategy( new File(
				ServiceFactory.getConfigReader().fetchValue(
						IConfigKeys.ANONYM_NER_ENGLISH_3CLASS_FILE ) ) );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.strategy = null;
	}

	@Test
	public void testAnonymization() throws GeneralLoggingException {
		String input = "Hello Mr. Feldmann, how are you doing? Kecia is all ok. Best wishes, Mr. Jackson";
		String expectedOutput = "Hello Mr. " + AAnomyzationStrategy.NE_PERSON_REPLACEMENT + ", how are you doing? " + AAnomyzationStrategy.NE_PERSON_REPLACEMENT + " is all ok. Best wishes, Mr. " + AAnomyzationStrategy.NE_PERSON_REPLACEMENT;
		
		String anonymized = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, anonymized );
	}

}