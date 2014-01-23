/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.experimental;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 <p>This test is designed to test the Stanford NER library with German text corpus files.
 * Here, the HGC corpus gets tested.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.11.2013
 *
 */
@RunWith(value = Parameterized.class)
public class GermanExtractionHgcTest {
	/**
	 * Stanford NER classifier.
	 */
	protected AbstractSequenceClassifier< CoreLabel > classifier;
	private String inputValue;
	private String expectedValue;
	
	/*
	 * ##################################
	 * #
	 * # test preparation
	 * #
	 * ##################################
	 */
	public GermanExtractionHgcTest(String inputValue, String expectedValue) {
		this.inputValue = inputValue;
		this.expectedValue = expectedValue;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass() throws Exception {
		this.initClassifier();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		classifier = null;
	}
	
	/**
	 * Initialize the classifier.
	 * The classifier contains the text corpus for the NLP process.
	 */
	protected void initClassifier() {
		classifier = CRFClassifier.getClassifierNoExceptions( ServiceFactory
				.getConfigReader().fetchValue(
						IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE ) );
	}
	
	/*
	 * ##################################
	 * #
	 * # data providers
	 * #
	 * ##################################
	 */
	@Parameters
	public static Collection< Object[] > data() {
		Object[][] dataProvider = new Object[][] { 
				// [inputValue] , [expectedValue]
				{ "Hallo Max Mustermann, ein toller Beitrag von dir!" 
					, "Hallo <I-PER>Max Mustermann</I-PER>, ein toller Beitrag von dir!" } 
								};
		return Arrays.asList( dataProvider );
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
	 * Test the German HGC classifier. 
	 */
	public void testHgcClassifier() {
		String nerResult = this.classifier.classifyWithInlineXML( this.inputValue );
		assertEquals( this.expectedValue, nerResult );
	}

}
