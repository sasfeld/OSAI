/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class defines the API for all concrete strategies which aim to extract semantic information from {@link Posting} instances.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public abstract class AExtractionStrategy {	
	/**
	 * Extract semantic information from a given {@link Posting}.
	 * @param p
	 * @throws GeneralLoggingException 
	 */
	public abstract void extractSemantics(final Posting p) throws GeneralLoggingException;

}
