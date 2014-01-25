/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;

/**
 * <p>This class realizes the adapter to access the RdfTripleStore from the Jena library.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 *
 */
public class RdfTripleStoreAdapter {
    protected Dataset jenaStore;

    /**
     * Create the adapter which access the given {@link Dataset} from the Jena TDB layer.
     * @param jenaStore
     */
    public RdfTripleStoreAdapter( final Dataset jenaStore ) {
        this.jenaStore = jenaStore;       
    }
    
    public void executeInsert( final String sparqlComannd ) {
        this.beginTransaction( ReadWrite.WRITE );
        
        try {
            
        } finally {
            this.endTransaction();
        }
    }
    
    protected void beginTransaction( final ReadWrite mode) {
        this.jenaStore.begin( mode );
    }
    
    protected void endTransaction() {
        this.jenaStore.end();
    }
}
