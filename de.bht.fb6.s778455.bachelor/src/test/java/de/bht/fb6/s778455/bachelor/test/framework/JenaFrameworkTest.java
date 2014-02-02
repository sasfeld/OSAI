/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.framework;

import java.io.File;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;

import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>
 * This combines general methods to test the Jena framework.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 * 
 */
public class JenaFrameworkTest {
    /**
     * Path to the dataset for unit testing.
     */
    private static final String LOCATION_UNIT_TEST_DS = "C:/jena/unittest";
    protected static RdfTripleStoreAdapter adapter; 
    /**
     * Get the RdfTripleStoreAdapter for unit testing.
     * @return
     */
    public static RdfTripleStoreAdapter getRdfTripleStoreAdapter() {
        if( null == adapter ) {
            final Dataset jenaStore = TDBFactory.createDataset( LOCATION_UNIT_TEST_DS );
            final File ontologyFile = ServiceFactory.getOntologyFile();
            final String ontologyBaseUri = ServiceFactory.getOntologyBaseUri();
            adapter = new RdfTripleStoreAdapter( jenaStore, ontologyFile,
                    ontologyBaseUri );                      
        }
        
        return adapter;
    }
    
   
}
