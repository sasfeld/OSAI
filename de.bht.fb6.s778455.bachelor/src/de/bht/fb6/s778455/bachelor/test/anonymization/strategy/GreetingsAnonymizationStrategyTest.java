/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.GreetingsAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Board.LearnedWordTypes;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

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
	public void testAnonymizeText() throws GeneralLoggingException {
		Board testBoard = new Board( new Course( "unit test course" ) );
		
		/*
		 * "Gru� XY"
		 */
		String input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru� SF";		
		String expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru� "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		String result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viele Gr��e
		 * Vorname Nachname"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViele Gr��e\nMax Mustermann";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nViele Gr��e\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "viele gr��e
		 * Vorname Nachname"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nviele gr��e\nMax Mustermann";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nviele gr��e\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gr��le XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGr��le SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGr��le "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�, XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�, SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�! XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�! XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\ngru�, max mustermann";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\ngru�, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gru�! Hier folgt kein Name." - not in the last line
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! Hier folgt kein Name.\n\nThis is another line\nanother line\nanother line";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nGru�! Hier folgt kein Name.\nThis is another line\nanother line\nanother line";
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg, XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg, xyz"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, xyz";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\nViel Erfolg, " + GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nSF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir!\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		
		/*
		 * "XYZ" shouldn't be replaced!
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir!\nXYZ";		
		expectedOutput = input;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*	
		 * "! XY\nNew line."
		 */
		input = "Hallo!\nGuter Beitrag, gef�llt mir! SF\nAnd a new line";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef�llt mir! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT + "\nAnd a new line";
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * assert "learned words"
		 */
		Set< String > learnedWords = testBoard.getLearnedWords( LearnedWordTypes.PERSON_NAME );
		System.out.println(learnedWords);
		
		assertTrue ( null != learnedWords);		
		
		assertTrue( learnedWords.contains( "SF" ) );
		assertTrue( learnedWords.contains( "xyz" ) );
		
		assertEquals( 4, learnedWords.size() );
		
	}

}
