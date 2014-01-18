/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>This class contains tests of the {@link LanguageDetectionStrategy}</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.01.2014
 *
 */
public class LanguageDetectionStrategyTest {
    protected AExtractionStrategy strategy;
    
    @Before
    public void setUp() throws Exception {
        this.strategy = new LanguageDetectionStrategy();
    }

    @After
    public void tearDown() throws Exception {
        this.strategy = null;
    }

    @Test
    public void testExtractSemantics() throws GeneralLoggingException {
        // test for German posting
        final Posting p = new Posting();
        
        assertEquals(Language.UNKNOWN, p.getLang());
        
        final String content = "Wenn du folgende Aufgabe meinst:Bestimmen Sie die Konstante c so, dass die Gleichung 2x^2 + 3x = cgenau eine (doppelte) Lösung besitzt. Ich habe die Aufgabe folgendermaßen gelöst:Ausgangsüberlegung:Eine quadratische Gleichung hat genau eine (doppelte) Lösung, wenn die Diskriminante = 0 ist. Um die Diskriminante zu ermitteln muss die Gleichung zunächst so umgeformt werden, dass sie der pq-Formel entspricht. 2x^2 + 3x =c &lt;=&gt; x^2 + 3/2x - c/2 = 0Die Diskriminante entspricht (p/2)^2 - q . Also: (3/4)^2 - (-c/2) = 0 =&gt; 9/16 + c/2 = 0Nun musst du die Gleichung noch nach c auflösen. c = -9/8Somit muss c = -9/8 sein, damit die Gleichung nur eine Lösung besitzt. Sollte ich mich irren und nur zufällig auf das richtige Ergebnis gekommen sein, korregiert mich bitte! ";
        p.setContent( content );
        
        // let strategy work
        this.strategy.extractSemantics( p );
        
        assertEquals( Language.GERMAN, p.getLang() );
        
        
        
    }

}
