/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp.pos;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.PosParser;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>This class contains tests of the {@link PosParser}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 *
 */
public class PosParserTest extends NoLoggingTest {
    protected PosParser parser;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.parser = new PosParser();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.parser = null;
    }

    @Test
    public void testGetWords() {
       final String inputPosTagged = "Hallo_ITJ zusammen_PTKVZ ._$. Ich_PPER habe_VAFIN eine_ART Frage_NN zum_APPRART Thema_NN Variablen_NN in_APPR Java_NE ._$. Wie_PWAV deklariere_VVFIN ich_PPER eine_ART lokale_ADJA Variable_NN ?_$. Vielen_PIDAT Dank_NN !_$.";
       final Set< String > words = this.parser.getWords( "NN", inputPosTagged );
       
       assertEquals(5, words.size());       
    }

}
