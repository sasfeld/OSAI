/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp.pos;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.PosExtractionStrategy;

/**
 * <p>This class realizes tests of the {@link PosExtractionStrategy}</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.01.2014
 *
 */
public class PosExtractionStrategyTest {
	protected static final String PATH_GERMAN_HGC_MODEL = "./conf/pos/models/german-hgc.tagger";
	protected AExtractionStrategy strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File model = new File( PATH_GERMAN_HGC_MODEL );
		this.strategy = new PosExtractionStrategy( model );
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
		// German Text and German model
		Posting posting = new Posting();
		posting.setContent( "Hallo zusammen. Ich habe eine Frage zum Thema Variablen in Java. Wie deklariere ich eine lokale Variable? Vielen Dank!" );
		this.strategy.extractSemantics( posting );
		
		// English text on German Model
		posting.setContent( "Hello together. I have a question to the topic variables in Java. How can I declare a local variable? Thanks alot!" );
		this.strategy.extractSemantics( posting );
	}

}
