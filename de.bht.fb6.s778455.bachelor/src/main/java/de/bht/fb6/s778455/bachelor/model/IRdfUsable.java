/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;

/**
 * <p>This interface defines methods which are used in the semantic creation.</p>
 * <p>Implementing classes must make sure that they offer an identity URI.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 *
 */
public interface IRdfUsable {
    /**
     * Get the RDF uri identifying the object as an OWL individual or RDF node in general.
     * @return
     */
    URI getRdfUri();
}
