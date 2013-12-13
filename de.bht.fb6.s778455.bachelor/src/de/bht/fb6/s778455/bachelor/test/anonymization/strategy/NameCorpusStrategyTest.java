/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.NameCorpusStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class realizes tests of {@link NameCorpusStrategy}</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 *
 */
public class NameCorpusStrategyTest {
	protected AAnomyzationStrategy strategy;
	private PersonNameCorpus personNameCorpus;
 
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
		this.strategy = new NameCorpusStrategy();
	    DirectoryImportStrategy importStrategy = new DirectoryImportStrategy();
		
		this.personNameCorpus = importStrategy.fillFromFile( new File( "./data/anonymization/personnames/testprenames.txt" ), new PersonNameCorpus(), PersonNameType.LASTNAME );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.strategy = null;
	}

	@Test
	public void testAnonymizeText() throws GeneralLoggingException {
		Board testBoard = new Board( new Course( "unit test course" ));
		testBoard.getBelongingCourse().setPersonNameCorpus( personNameCorpus );
//		
//		System.out.println(this.personNameCorpus);
//		
//		String input = "Hallo Farshad! Fr. Schmiedecke? Richtige Einstellung! Das sagt auch Mustafa. Gruß Özbey";
//		String expectedOutput = "Hallo "+NameCorpusStrategy.NAME_CORPUS_REPLACEMENT+"! Fr. "+NameCorpusStrategy.NAME_CORPUS_REPLACEMENT+"? Richtige Einstellung! Das sagt auch "+NameCorpusStrategy.NAME_CORPUS_REPLACEMENT+". Gruß "+NameCorpusStrategy.NAME_CORPUS_REPLACEMENT;
//		
//		String result = this.strategy.anonymizeText( input, testBoard );
		
//		assertEquals( expectedOutput, result );		
		
		String input = "ich finde, der Dis-Kurs sollte in das offizielle Modulhandbuch. Vielleicht können Sie sich ja darum bem�hen, Fr. Schmiedecke ? ;)";
		String expectedOutput = "ich finde, der Dis-Kurs sollte in das offizielle Modulhandbuch. Vielleicht können Sie sich ja darum bem�hen, "+NameCorpusStrategy.NAME_CORPUS_REPLACEMENT+"? ;)";
		
		String result = this.strategy.anonymizeText( input, testBoard );
		
		assertEquals( expectedOutput, result );
	}

}
