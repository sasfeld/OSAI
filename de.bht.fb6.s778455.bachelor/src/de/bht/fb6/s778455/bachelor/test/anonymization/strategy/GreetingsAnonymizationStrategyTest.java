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
		 * "Gruﬂ XY"
		 */
		String input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ SF";		
		String expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		String result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viele Gr¸ﬂe
		 * Vorname Nachname"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViele Gr¸ﬂe\nMax Mustermann";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViele Gr¸ﬂe\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "viele gr¸ﬂe
		 * Vorname Nachname"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nviele gr¸ﬂe\nMax Mustermann";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nviele gr¸ﬂe\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gr¸ﬂle XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGr¸ﬂle SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGr¸ﬂle "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gruﬂ, XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ, SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gruﬂ! XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ! SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gruﬂ! XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\ngruﬂ, max mustermann";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\ngruﬂ, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Gruﬂ! Hier folgt kein Name." - not in the last line
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ! Hier folgt kein Name.\n\nThis is another line\nanother line\nanother line";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nGruﬂ! Hier folgt kein Name.\nThis is another line\nanother line\nanother line";
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg, XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg, SF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg, "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "Viel Erfolg, xyz"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg, xyz";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\nViel Erfolg, " + GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*
		 * "XY"
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nSF";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir!\n"+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		
		/*
		 * "XYZ" shouldn't be replaced!
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir!\nXYZ";		
		expectedOutput = input;
		
		result = this.strategy.anonymizeText( input, testBoard );
		assertEquals( expectedOutput, result );		
		
		/*	
		 * "! XY\nNew line."
		 */
		input = "Hallo!\nGuter Beitrag, gef‰llt mir! SF\nAnd a new line";		
		expectedOutput = "Hallo!\nGuter Beitrag, gef‰llt mir! "+GreetingsAnonymizationStrategy.PERSONAL_GREETING_REPLACEMENT + "\nAnd a new line";
		
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
