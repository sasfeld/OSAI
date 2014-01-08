/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.topiczoom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Posting.TagType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom.TopicZoomExtractionStrategy;

/**
 * <p>This class contains tests of the {@link TopicZoomExtractionStrategy}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class TopicZoomExtractionStrategyTest {
	private TopicZoomExtractionStrategy strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.strategy = new TopicZoomExtractionStrategy();
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
		samplePosting.setContent( "Chardonnay ist ein Wein\nAlbert Einstein ist eine Person" );
		
		this.strategy.extractSemantics( samplePosting );
		
		System.out.println(samplePosting.getTags( TagType.TOPIC_ZOOM ));
	}

}
