/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy.ner;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.EnglishNerAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link EnglishNerAnonymizationStrategyTest}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.12.2013
 * 
 */
public class EnglishNerAnonymizationStrategyTest extends NoLoggingTest {
    protected static final File ENGLISH_CORPUS_FILE = new File(
            PATH_UNITTEST_DATA_FOLDER
                    + "/organization/ner/stanford-ner-2013-06-20-english/english.all.3class.distsim.crf.ser.gz");

    protected AAnomyzationStrategy strategy;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.strategy = new EnglishNerAnonymizationStrategy(ENGLISH_CORPUS_FILE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strategy = null;
    }

    @Test
    public void testAnonymization() throws GeneralLoggingException {
        String input = "Hello Mr. Feldmann, how are you doing? Kecia is all ok. Best wishes, Mr. Jackson";
        String expectedOutput = "Hello Mr. "
                + AAnomyzationStrategy.NE_PERSON_REPLACEMENT
                + ", how are you doing? "
                + AAnomyzationStrategy.NE_PERSON_REPLACEMENT
                + " is all ok. Best wishes, Mr. "
                + AAnomyzationStrategy.NE_PERSON_REPLACEMENT;

        String anonymized = this.strategy.anonymizeText(input);
        assertEquals(expectedOutput, anonymized);

        /*
         * make sure that common names are not replaced.
         */
        input = "Hello Montag, how are you doing?";
        expectedOutput = "Hello <PERSON>Montag</PERSON>, how are you doing?";

        anonymized = this.strategy.anonymizeText(input);
        assertEquals(expectedOutput, anonymized);

    }

}
