/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner.NerExtractionStrategy;

/**
 * <p>
 * This class contains tests of the {@link NerExtractionStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class NerExtractionStrategyTest {
	protected NerExtractionStrategy strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File corpusFile = new File( ServiceFactory.getConfigReader()
				.fetchValue( IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE ) );
		this.strategy = new NerExtractionStrategy( corpusFile );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.strategy = null;
	}

	@Test
	public void testExtractSemantics() throws GeneralLoggingException {
		// create sample posting
		Posting samplePosting = new Posting();
		samplePosting
				.setContent( "Warst du schonmal in Berlin?\nAlbert Einstein war in der Brandenburgischen Akademie der Wissenschaften." );

		this.strategy.extractSemantics( samplePosting );

		System.out.println( samplePosting.getTags( TagType.NER_TAGS ) );

		// create sample course
		Course newCourse = new Course( "Albert Einstein - Kurs" );
		newCourse.setSummary( "Chardonnay ist ein Wein." );

		this.strategy.extractSemantics( newCourse );

		System.out.println( newCourse.getTags( TagType.NER_TAGS ) );

	}

}
