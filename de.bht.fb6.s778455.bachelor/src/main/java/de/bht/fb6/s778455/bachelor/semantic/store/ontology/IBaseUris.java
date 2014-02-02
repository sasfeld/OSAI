/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store.ontology;

/**
 * <p>This interface holds base URIS to be used in our RDF network.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 *
 */
public interface IBaseUris {
    /**
     * Base URI for the usage of 'rdf:about'
     */
    String BASE_URI = "http://saschafeldmann.de/bachelor/ontology/";
    /**
     * Base URI for the usage of 'rdf:ID'
     */
    String BASE_URI_ID = "http://saschafeldmann.de/bachelor/ontology#";
    
    String INDIVIDUAL_BASE_URI = BASE_URI + "individuals/";
}
