/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.nlp.pos;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos.PosTagMapper;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * 
 * <p>This class contains tests of the {@link PosTagMapper}</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 *
 */
public class PosTagMapperTest extends NoLoggingTest {
    protected PosTagMapper mapper;

    @Before
    public void setUp() throws Exception {
        final Set< String > posTags = new HashSet<String>();
        posTags.add( "NN" );
        
        this.mapper = new PosTagMapper( posTags  );
    }

    @After
    public void tearDown() throws Exception {
        this.mapper = null;
    }

    @Test
    public void testMapToContribution() {
       // sample posting
       final Posting sampleP = new Posting();
      
       this.mapper.mapToContribution( "Hallo_ITJ zusammen_PTKVZ ._$. Ich_PPER habe_VAFIN eine_ART Frage_NN zum_APPRART Thema_NN Variablen_NN in_APPR Java_NE ._$. Wie_PWAV deklariere_VVFIN ich_PPER eine_ART lokale_ADJA Variable_NN ?_$. Vielen_PIDAT Dank_NN !_$.", sampleP );
       
       assertTrue( null != sampleP.getTags( TagType.POS_TAG ));
       assertTrue( 5 == sampleP.getTags( TagType.POS_TAG ).size());       
    }

}
