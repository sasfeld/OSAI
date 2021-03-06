/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.experimental;

import java.util.List;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * <p>
 * This class offers the base for the testing of the stanford NLP library.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 * 
 */
public class GermanExctractionTest {
	private static final String EXAMPLE_INPUT = "Hallo Sascha Feldmann, ein toller Beitrag von dir.";
	protected static AbstractSequenceClassifier< CoreLabel > classifier;

	/**
	 * Initialize the classifier.
	 * The classifier contains the text corpus for the NLP process.
	 */
	protected static void initClassifier() {
		classifier = CRFClassifier.getClassifierNoExceptions( ServiceFactory
				.getConfigReader().fetchValue(
						IConfigKeys.ANONYM_NER_GERMAN_DEWAC_FILE ) );
	}
	
	protected static void testClassifier() {
		List<List<CoreLabel>> labeledOut = classifier.classify( EXAMPLE_INPUT );
		for (List<CoreLabel> sentence : labeledOut) {
			System.out.println(sentence);
		}
	}

	public static void main( String[] args ) {
		initClassifier();
		testClassifier();
		testClassifyToString();
		testClassifiyXml();
	}

	private static void testClassifiyXml() {
		System.out.println(classifier.classifyWithInlineXML( EXAMPLE_INPUT ));		
	}

	private static void testClassifyToString() {
		System.out.println(classifier.classifyToString( EXAMPLE_INPUT ));
		
	}

	
}
