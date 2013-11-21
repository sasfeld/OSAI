/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.strategy;

/**
 * 
 * <p>This class describes the API for an anonymization strategy.</p>
 * <p>An anomization strategy offers an implementation to anonymize text.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 21.11.2013
 *
 */
public abstract class AAnomyzationStrategy {
	/**
	 * Anonymize a given {@link String}.
	 * Anonymization means removing all named entities recording to a person.
	 * 
	 * @param inputText
	 * @return a new {@link String} with anonymized data
	 */
	public abstract String anonymizeText(String inputText);
}
