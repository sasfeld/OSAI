/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.GreetingsAnonymizationStrategy;

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
		 * "Gru� XY"
		 */
		String input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru� SF";		
		String expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru� "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		String result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�, XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�, SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�! XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�! Hier folgt kein Name."
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! Hier folgt kein Name.";		
		expectedOutput = input;
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg, XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg, XYZ"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, XYZ";		
		expectedOutput = input;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nSF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		
		/*
		 * "XYZ" shouldn't be replaced!
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nXYZ";		
		expectedOutput = input;
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
		
		/*	
		 * "! XY\nNew line."
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir! SF\nAnd a new line";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT + "\nAnd a new line";
		
		result = this.strategy.anonymizeText( input );
		assertEquals( expectedOutput, result );		
	}

}
