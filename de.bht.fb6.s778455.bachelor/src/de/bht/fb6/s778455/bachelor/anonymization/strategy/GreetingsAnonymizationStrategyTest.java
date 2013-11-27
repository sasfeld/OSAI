/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>This class contains tests of the {@link GreetingsAnonymizationStrategy}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.11.2013
 *
 */
public class GreetingsAnonymizationStrategyTest {
	protected AAnomyzationStrategy strategy;

	/*
	 * ##################################
	 * # test preparation #
	 * ##################################
	 */
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.strategy = new GreetingsAnonymizationStrategy();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.strategy = null;
	}

	/*
	 * ##################################
	 * # tests #
	 * ##################################
	 */
	@Test
	public void testAnonymizeText() {
		/*
		 * "Gruﬂ XY"
		 */
		String input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ SF";		
		String expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		String result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nSF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "! XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir! SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
	}

}
