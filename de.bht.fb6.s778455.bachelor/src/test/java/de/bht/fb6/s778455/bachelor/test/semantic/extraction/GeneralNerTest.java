/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * 
 * <p>This class ....</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.02.2014
 *
 */
public class GeneralNerTest extends NoLoggingTest {
    protected static File HGC_CORPUS_FILE = new File(
            PATH_UNITTEST_DATA_FOLDER
                    + "/organization/ner/stanford-ner-2012-05-22-german/hgc_175m_600.crf.ser.gz");
    
    protected AbstractSequenceClassifier< CoreLabel > classifier;

    @Before
    public void setUp() throws Exception {
        this.classifier = CRFClassifier.getClassifier( HGC_CORPUS_FILE );
    }

    @After
    public void tearDown() throws Exception {
        this.classifier = null;
    }

    @Test
    public void test() {
       final String input = "Axel Springer ist eine Person.";
       
       System.out.println("\n classify with List < List <CoreLabel >> \n");
       final List< List< CoreLabel >> output1 = this.classifier.classify( input );
       System.out.println(output1);
       
       System.out.println("\n classify with inline \n");
       final String output2 = this.classifier.classifyToString( input );
       System.out.println(output2);
    }

}
