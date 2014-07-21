/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.LearnedNamesStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Course.LearnedWordTypes;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class realizes tests of the {@link LearnedNamesStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.12.2013
 * 
 */
public class LearnedNamesStrategyTest extends NoLoggingTest {
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
		final Board testBoard = new Board( new Course( "testCourse", new LmsCourseSet( "unit test course set" )  ) );
		testBoard.getBelongingCourse().addLearnedWord( "max", LearnedWordTypes.PERSON_NAME );
		testBoard.getBelongingCourse().addLearnedWord( "Mustermann", LearnedWordTypes.PERSON_NAME );
		testBoard.getBelongingCourse().addLearnedWord( "xyz", LearnedWordTypes.PERSON_NAME );

		String input = "Hallo Max, wie geht es der Mutter mustermann? Gru� XYZ";
		String expectedOutput = "Hallo "
				+ LearnedNamesStrategy.LEARNED_PERSON_NAME_REPLACEMENT
				+ ", wie geht es der Mutter "
				+ LearnedNamesStrategy.LEARNED_PERSON_NAME_REPLACEMENT + "? Gru� "
				+ LearnedNamesStrategy.LEARNED_PERSON_NAME_REPLACEMENT;
		assertEquals( expectedOutput, this.anonymizationStrategy.anonymizeText( input, testBoard ) );
		
		input = "Hallo das darf nicht ersetzt werden: abcxyzstadt";
		expectedOutput = input;
		assertEquals( expectedOutput, this.anonymizationStrategy.anonymizeText( input, testBoard ) );
	}
	
	@Test
	public void testCommonWords() throws GeneralLoggingException {
		final Board testBoard = new Board( new Course( "testCourse", new LmsCourseSet( "unit test course set" )  ) );
		
		// german common words
		testBoard.getBelongingCourse().addLearnedWord( "im", LearnedWordTypes.PERSON_NAME );
		testBoard.getBelongingCourse().addLearnedWord( "und", LearnedWordTypes.PERSON_NAME );
		testBoard.getBelongingCourse().addLearnedWord( "zwischen", LearnedWordTypes.PERSON_NAME );
		
		String input = "Er steht zwischen den Autos auf der Straße im Regen und raucht.";
		String expectedOutput = input;
		assertEquals( expectedOutput, this.anonymizationStrategy.anonymizeText( input, testBoard ) );

		// english common words
		testBoard.getBelongingCourse().addLearnedWord( "he", LearnedWordTypes.PERSON_NAME );
		testBoard.getBelongingCourse().addLearnedWord( "and", LearnedWordTypes.PERSON_NAME );
		testBoard.getBelongingCourse().addLearnedWord( "small", LearnedWordTypes.PERSON_NAME );
		
		input = "He is small and thick.";
		expectedOutput = input;
		assertEquals( expectedOutput, this.anonymizationStrategy.anonymizeText( input, testBoard ) );

	}

}
