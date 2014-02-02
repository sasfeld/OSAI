/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;

import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IOwlIndividuals;

/**
 * <p>
 * This enumeration keeps language information.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 * 
 */
public enum Language implements IRdfUsable {
    GERMAN, ENGLISH, UNKNOWN;

    /**
     * Get the enum type from a string
     * 
     * @param lang
     * @return
     */
    public static Language getFromString( final String lang ) {
        final String lowered = lang.trim().toLowerCase();

        switch( lowered ) {
        case "german":
            return GERMAN;
        case "english":
            return ENGLISH;
        default:
            return UNKNOWN;
        }
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    public URI getRdfUri() throws URISyntaxException {
        switch( this ) {
            case ENGLISH:
                return new URI( IOwlIndividuals.INSTANCE_LANG_EN );
            case GERMAN:
                return new URI( IOwlIndividuals.INSTANCE_LANG_DE );
            default:
                throw new IllegalArgumentException( "Can't map UNKNOWN to an RDF id!" );
        }
    }

}
