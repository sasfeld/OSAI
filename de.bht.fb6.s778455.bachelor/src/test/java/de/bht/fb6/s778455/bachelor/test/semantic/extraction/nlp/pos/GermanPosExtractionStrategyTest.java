/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp.pos;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.APosExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.GermanPosExtractionStrategy;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class realizes tests of the {@link APosExtractionStrategy}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.01.2014
 * 
 */
public class GermanPosExtractionStrategyTest extends NoLoggingTest {
    protected static final String PATH_GERMAN_HGC_MODEL = "./conf/pos/models/german-hgc.tagger";
    protected static final String PATH_ENGLISH_MODEL = "./conf/pos/models/english-left3words-distsim.tagger";
    protected AExtractionStrategy strategy;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        final File model = new File( PATH_GERMAN_HGC_MODEL );
        this.strategy = new GermanPosExtractionStrategy( model );
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strategy = null;
    }

    @Test( expected = IllegalArgumentException.class )
    public void testForeignModel() {
        new GermanPosExtractionStrategy(
                new File( PATH_ENGLISH_MODEL ) );
    }

    @Test
    public void testExtractSemantics() throws GeneralLoggingException {
        // German Text and German model
        final Posting posting = new Posting();
        posting.setContent( "Hallo zusammen. Ich habe eine Frage zum Thema Variablen in Java. Wie deklariere ich eine lokale Variable? Vielen Dank!" );
        this.strategy.extractSemantics( posting );

        System.out.println( posting.getTags( TagType.POS_TAG ) + "\n\n" );
        System.out.println( "tagged content: " + posting.getTaggedContent() );

        assertTrue( null != posting.getTags( TagType.POS_TAG ) );
        assertTrue( 0 < posting.getTags( TagType.POS_TAG ).size() );
        assertTrue( null != posting.getTaggedContent() );
    }

}
