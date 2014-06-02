/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp.ner;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner.ANerExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner.GermanNerExtractionStrategy;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link ANerExtractionStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class GermanNerExtractionStrategyTest extends NoLoggingTest {
	protected ANerExtractionStrategy strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		final File corpusFile = new File( ServiceFactory.getConfigReader()
				.fetchValue( IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE ) );
		this.strategy = new GermanNerExtractionStrategy( corpusFile );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.strategy = null;
	}

	@Test( expected = IllegalArgumentException.class )
	public void testWrongClassifier() {
		final File englishCorpusFile = new File( ServiceFactory.getConfigReader()
				.fetchValue( IConfigKeys.ANONYM_NER_ENGLISH_4CLASS_FILE ) );
		new GermanNerExtractionStrategy( englishCorpusFile );
	}

	@Test
	public void testExtractSemantics() throws GeneralLoggingException {
		// create sample posting
		final Posting samplePosting = new Posting();
		samplePosting
				.setContent( "Warst du schonmal in Berlin?\nAlbert Einstein war in der Brandenburgischen Akademie der Wissenschaften." );

		this.strategy.extractSemantics( samplePosting );

		assertTrue( null != samplePosting.getTags( TagType.NER_TAG ) );
		assertTrue( samplePosting.getTags( TagType.NER_TAG ).size() > 0 );

		System.out.println( samplePosting.getTags( TagType.NER_TAG ) );

		// create sample course
		final Course newCourse = new Course( "Albert Einstein - Kurs", new LmsCourseSet( "unit test course set" )  );
		newCourse.setSummary( "Chardonnay ist ein Wein." );

		this.strategy.extractSemantics( newCourse );

		System.out.println( newCourse.getTags( TagType.NER_TAG ) );

	}

}
