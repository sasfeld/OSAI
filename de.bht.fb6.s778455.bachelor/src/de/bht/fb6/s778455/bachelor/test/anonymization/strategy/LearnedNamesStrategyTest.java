/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.LearnedNamesStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Board.LearnedWordTypes;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * This class realizes tests of the {@link LearnedNamesStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.12.2013
 * 
 */
public class LearnedNamesStrategyTest {
	protected AAnomyzationStrategy anonymizationStrategy;

	/*
	 * ################################## # test preparation #
	 * ##################################
	 */
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.anonymizationStrategy = new LearnedNamesStrategy();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.anonymizationStrategy = null;
	}

	@Test
	/**
	 * Test if simple "learned names" are replaced.
	 * This is very simpe but might be extended in the future.
	 * @throws GeneralLoggingException
	 */
	public void testAnonymizeText() throws GeneralLoggingException {
		Board testBoard = new Board( new Course( "testCourse" ) );
		testBoard.addLearnedWord( "max", LearnedWordTypes.PERSON_NAME );
		testBoard.addLearnedWord( "Mustermann", LearnedWordTypes.PERSON_NAME );
		testBoard.addLearnedWord( "xyz", LearnedWordTypes.PERSON_NAME );

		String input = "Hallo Max, wie geht es der Mutter mustermann? Gruﬂ XYZ";
		String expectedOutput = "Hallo "
				+ LearnedNamesStrategy.LEARNED_PERSON_NAME_REPLACEMENT
				+ ", wie geht es der Mutter "
				+ LearnedNamesStrategy.LEARNED_PERSON_NAME_REPLACEMENT + "? Gruﬂ "
				+ LearnedNamesStrategy.LEARNED_PERSON_NAME_REPLACEMENT;
		assertEquals( expectedOutput, this.anonymizationStrategy.anonymizeText( input, testBoard ) );
		
		input = "Hallo das darf nicht ersetzt werden: abcxyzstadt";
		expectedOutput = input;
		assertEquals( expectedOutput, this.anonymizationStrategy.anonymizeText( input, testBoard ) );
	}

}
