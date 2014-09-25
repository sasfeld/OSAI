/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.strategy.ner;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.ANerAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class realizes several tests of the
 * {@link GermanNerAnonymizationStrategy}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 * 
 */
public class GermanNerAnonymizationStrategyTest extends NoLoggingTest {
    protected static File DEWAC_CORPUS_FILE = new File(
            PATH_UNITTEST_DATA_FOLDER
                    + "/organization/ner/stanford-ner-2012-05-22-german/dewac_175m_600.crf.ser.gz");

    protected ANerAnonymizationStrategy strategy;

    /*
     * ################################## # test preparation #
     * ##################################
     */
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.strategy = new GermanNerAnonymizationStrategy(DEWAC_CORPUS_FILE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.strategy = null;
    }

    /*
     * ################################## # # tests #
     * ##################################
     */

    @Test
    /**
     * Use reflection to test the private method of ANerAnoymizationStrategy.
     */
    public void testPrepareText() throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            GeneralLoggingException {
        final Method method = GermanNerAnonymizationStrategy.class
                .getSuperclass().getSuperclass()
                .getDeclaredMethod("prepareText", String.class);
        method.setAccessible(true);

        /*
         * test replacement of ".[word]" by ".[whitespace][word]"
         */
        String inputText = "This text doesn't have whitespaces between sentences.As you see here.And here.";
        String expectedCleanedText = "This text doesn't have whitespaces between sentences. As you see here. And here.";

        String cleanedText = (String) method.invoke(this.strategy, inputText);

        assertEquals(expectedCleanedText, cleanedText);

        /*
         * test replacement of ".[word]" by ".[whitespace][word]"
         */
        inputText = "I don't like whitespaces,especially between commas. See that,amigo?";
        expectedCleanedText = "I don't like whitespaces, especially between commas. See that, amigo?";

        cleanedText = (String) method.invoke(this.strategy, inputText);

        assertEquals(expectedCleanedText, cleanedText);

        /*
         * ensure that no replacement of "http://www.test.[word]" is done
         */
        inputText = "Nice link. See that: http://www.test.de";
        expectedCleanedText = inputText;

        cleanedText = (String) method.invoke(this.strategy, inputText);

        assertEquals(expectedCleanedText, cleanedText);

        /*
         * test removement of empty lines
         */
        inputText = "Hello\n\n\n\nMany empty lines\nNo empty line\n\n";
        expectedCleanedText = "Hello\nMany empty lines\nNo empty line\n";

        cleanedText = (String) method.invoke(this.strategy, inputText);

        assertEquals(expectedCleanedText, cleanedText);

        // /*
        // * test replacement of moodle characters
        // */
        // inputText =
        // "<p>schaut für alle möglichen <strong>Typen</strong> Tb, ob Days::operator!=(Tb) existiert. Wenn auch das nicht geht, überprüft der Compiler auch <LEARNED_PERSON_NAME_REPLACEMENT> sämtliche Umwandlungen von a in <LEARNED_PERSON_NAME_REPLACEMENT> Typ Ta, <LEARNED_PERSON_NAME_REPLACEMENT> schaut welcher operator!=(Ta, Tb) existiert.</p>\r\n<p>Wenn Sie also <LEARNED_PERSON_NAME_REPLACEMENT> Days::operator unsigned int() implementiert haben, dann wird der Compiler fündig: der eingebaute operator!=(unsigned int, unsigned int) kann verwendet werden, weil sowohl a als auch b in unsigned int umwandelbar sind.</p>\r\n<p>Alles klar? Also, Sie müssen eigentlich nur die in der Aufgabenstellung gelisteten Operatoren umsetzen, dann sollte es gehen.</p>\r\n<p>Viel Erfolg";
        // expectedCleanedText =
        // "schaut für alle möglichen Typen Tb, ob Days::operator!=(Tb) existiert. Wenn auch das nicht geht, überprüft der Compiler auch <LEARNED_PERSON_NAME_REPLACEMENT> sämtliche Umwandlungen von a in <LEARNED_PERSON_NAME_REPLACEMENT> Typ Ta, <LEARNED_PERSON_NAME_REPLACEMENT> schaut welcher operator!=(Ta, Tb) existiert. Wenn Sie also <LEARNED_PERSON_NAME_REPLACEMENT> Days::operator unsigned int() implementiert haben, dann wird der Compiler fündig: der eingebaute operator!=(unsigned int, unsigned int) kann verwendet werden, weil sowohl a als auch b in unsigned int umwandelbar sind.</p>\r\n<p>Alles klar? Also, Sie müssen eigentlich nur die in der Aufgabenstellung gelisteten Operatoren umsetzen, dann sollte es gehen. Viel Erfolg";
        //
        // cleanedText = ( String ) method.invoke( this.strategy, inputText );
        //
        // assertEquals( expectedCleanedText, cleanedText );

        /*
         * test replacement of moodle characters
         */
        inputText = "<p id=\"yui_3_7_3_3_1386692797241_174\">test</p>";
        expectedCleanedText = "test";
        cleanedText = (String) method.invoke(this.strategy, inputText);

        assertEquals(expectedCleanedText, cleanedText);

        /*
         * assert that anonymization tags are not replaced
         */
        inputText = de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy.NE_PERSON_REPLACEMENT
                + " "
                + de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy.LEARNED_PERSON_NAME_REPLACEMENT
                + " "
                + de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy.NAME_CORPUS_REPLACEMENT
                + " "
                + AAnomyzationStrategy.PERSONAL_DATA_REPLACEMENT
                + " "
                + AAnomyzationStrategy.PERSONAL_GREETING_REPLACEMENT;
        expectedCleanedText = inputText;
        cleanedText = (String) method.invoke(this.strategy, inputText);

        assertEquals(expectedCleanedText, cleanedText);

        /*
         * test replacement of names
         */
        inputText = "Liebe Grüße, Sascha Feldmann";
        expectedCleanedText = "Liebe Grüße, "
                + AAnomyzationStrategy.NE_PERSON_REPLACEMENT;

        cleanedText = this.strategy.anonymizeText(inputText);
        assertEquals(expectedCleanedText, cleanedText);

        /*
         * test replacement of &quot;
         */
        inputText = "Er sagt: &quot;Was?&quot;";
        expectedCleanedText = "Er sagt: Was?";

        cleanedText = this.strategy.anonymizeText(inputText);
        assertEquals(expectedCleanedText, cleanedText);

        /*
         * ensure that common words are not replaced.
         */
        inputText = "Liebe Grüße und bis Montag";
        expectedCleanedText = inputText;

        cleanedText = this.strategy.anonymizeText(inputText);
        assertEquals(expectedCleanedText, cleanedText);
    }

    // @Test
    // /**
    // * Use reflection to test the private method of ANerAnoymizationStrategy.
    // */
    // public void testFilterPersonalData() throws NoSuchMethodException,
    // SecurityException, IllegalAccessException, IllegalArgumentException,
    // InvocationTargetException {
    // Method method =
    // GermanNerAnonymizationStrategy.class.getSuperclass().getSuperclass().getDeclaredMethod("filterPersonalData",
    // String.class );
    // method.setAccessible( true );
    //
    // /*
    // * test replacement of eMail addresses
    // */
    // String inputText =
    // "Hello, nice board here. Feel free to contact me at: max.mustermann@mustertld.td !";
    // String expectedCleanedText =
    // "Hello, nice board here. Feel free to contact me at: "+ANerAnonymizationStrategy.PERSONAL_DATA_REPLACEMENT+" !";
    //
    // String cleanedText = ( String ) method.invoke( this.strategy, inputText);
    //
    // assertEquals( expectedCleanedText, cleanedText );
    // }
    //
    // @Test
    // public void testSimpleTest() {
    // String inputString = "Hallo max.muster@tld.de!";
    // String expected = "Hallo !";
    //
    // String output = inputString.replaceAll(
    // "[a-zA-Z0-9][\\w\\.-]*@(?:[a-zA-Z0-9][a-zA-Z0-9_-]+\\.)+[A-Z,a-z]{2,5}",
    // "" );
    //
    // assertEquals( expected, output );
    // }
    //
    // @Test
    // public void testRemoveSpecialTags() {
    // String inputString =
    // "Hallo <I-PER>Max Mustermann</I-PER>, warst du bereits in <I-LOC>Berlin</I-LOC>?";
    // String expected = "Hallo Max Mustermann, warst du bereits in Berlin?";
    //
    // String output = this.strategy.removeSpecialTags( inputString );
    //
    // assertEquals( expected, output );
    // }
}
