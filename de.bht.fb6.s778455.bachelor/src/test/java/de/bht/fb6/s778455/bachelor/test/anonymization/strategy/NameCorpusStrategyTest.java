/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.NameCorpusStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class realizes tests of {@link NameCorpusStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 * 
 */
public class NameCorpusStrategyTest extends NoLoggingTest {
    private static final File PERSON_CORPUS_FILE = new File(
            PATH_UNITTEST_DATA_FOLDER
                    + "/anonymization/namecorpus/testprenames.txt");
    
    protected AAnomyzationStrategy strategy;
    private PersonNameCorpus personNameCorpus;

    /*
     * ################################## # test preparation #
     * ##################################
     */
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.strategy = new NameCorpusStrategy();
        final DirectoryImportStrategy importStrategy = new DirectoryImportStrategy();

        this.personNameCorpus = importStrategy.fillFromFile(PERSON_CORPUS_FILE,
                new PersonNameCorpus(), PersonNameType.LASTNAME);
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
        final Board testBoard = new Board(new Course("unit test course",
                new LmsCourseSet("unit test course set")));
        testBoard.getBelongingCourse().setPersonNameCorpus(
                this.personNameCorpus);

        System.out.println(this.personNameCorpus);

        String input = "Hallo Farshad! Fr. Schmiedecke? Richtige Einstellung! Das sagt auch Mustafa.";
        String expectedOutput = "Hallo "
                + NameCorpusStrategy.NAME_CORPUS_REPLACEMENT + "! Fr. "
                + NameCorpusStrategy.NAME_CORPUS_REPLACEMENT
                + "? Richtige Einstellung! Das sagt auch "
                + NameCorpusStrategy.NAME_CORPUS_REPLACEMENT + ".";
        String result = this.strategy.anonymizeText(input, testBoard);

        assertEquals(expectedOutput, result);

        input = "ich finde, der Dis-Kurs sollte in das offizielle Modulhandbuch. Vielleicht können Sie sich ja darum bem�hen, Fr. Schmiedecke ? ;)";
        expectedOutput = "ich finde, der Dis-Kurs sollte in das offizielle Modulhandbuch. Vielleicht können Sie sich ja darum bem�hen, Fr. "
                + NameCorpusStrategy.NAME_CORPUS_REPLACEMENT + " ? ;)";

        result = this.strategy.anonymizeText(input, testBoard);

        assertEquals(expectedOutput, result);

        // "hauke[xawgas] shall not be replaced
        input = "haukeineinemWort";
        expectedOutput = input;

        result = this.strategy.anonymizeText(input, testBoard);

        assertEquals(expectedOutput, result);

        // lower cased
        input = "gruß schmiedecke";
        expectedOutput = "gruß " + NameCorpusStrategy.NAME_CORPUS_REPLACEMENT;

        result = this.strategy.anonymizeText(input, testBoard);

        assertEquals(expectedOutput, result);

        // common names
        input = "da elsta";
        expectedOutput = input;

        result = this.strategy.anonymizeText(input, testBoard);

        assertEquals(expectedOutput, result);
    }

}
