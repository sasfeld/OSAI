/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp.ner;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner.EnglishNerExtractionStrategy;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link EnglishNerExtractionStrategyTest}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.01.2014
 * 
 */
public class EnglishNerExtractionStrategyTest extends NoLoggingTest {
    protected static final File ENGLISH_CORPUS_FILE = new File(
            PATH_UNITTEST_DATA_FOLDER
                    + "/organization/ner/stanford-ner-2013-06-20-english/english.muc.7class.distsim.crf.ser.gz");

    protected AExtractionStrategy strategy;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.strategy = new EnglishNerExtractionStrategy(ENGLISH_CORPUS_FILE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strategy = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongClassifier() {
        File englishCorpusFile = new File(ServiceFactory.getConfigReader()
                .fetchValue(IConfigKeys.ANONYM_NER_GERMAN_HGC_FILE));
        new EnglishNerExtractionStrategy(englishCorpusFile);
    }

    @Test
    public void testExtractSemantics() throws GeneralLoggingException {
        // create sample posting
        Posting samplePosting = new Posting();
        samplePosting
                .setContent("The fate of Lehman Brothers, the beleaguered investment bank, hung in the balance on Sunday as Federal Reserve officials and the leaders of major financial institutions continued to gather in emergency meetings trying to complete a plan to rescue the stricken bank.  Several possible plans emerged from the talks, held at the Federal Reserve Bank of New York and led by Timothy R. Geithner, the president of the New York Fed, and Treasury Secretary Henry M. Paulson Jr.");

        this.strategy.extractSemantics(samplePosting);

        System.out.println(samplePosting.getTags(TagType.NER_TAG));

        assertTrue(null != samplePosting.getTags(TagType.NER_TAG));
        assertTrue(samplePosting.getTags(TagType.NER_TAG).size() > 0);

    }

}
