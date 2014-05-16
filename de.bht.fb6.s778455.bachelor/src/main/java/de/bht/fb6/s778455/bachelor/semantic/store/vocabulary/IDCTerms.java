/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store.vocabulary;

import com.hp.hpl.jena.vocabulary.DCTerms;

/**
 * <p>This interface holds vocabulary from DCTerms.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 *
 */
public interface IDCTerms {
    String BASE_URI = DCTerms.getURI();
    
    /**
     * @see http://purl.org/dc/terms/URI
     */
    String DCTERMS_URI = BASE_URI + "/" + BASE_URI;
}
