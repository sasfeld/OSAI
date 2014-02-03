/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.organization.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;

import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.semantic.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>This class is a static ServiceFactory for the semantics module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class ServiceFactory {	
	private static IConfigReader configReader;
	private static Map< String, String > namespacesMap;
	private static Dataset jenaStore;
	private static RdfTripleStoreAdapter jenaAdapter;
    private static File ontologyFile;
    private static String ontologyBaseUri;
    private static Boolean forceOntUpdate;

	/**
	 * Return the config Reader for the semantics module.
	 * @return
	 */
	public static IConfigReader getConfigReader() {
		if ( null == configReader ) {
			configReader = new ConfigReader( );
		}
		
		return configReader;
	}

	/**
	 * Get the general namespaces used in Topic Zoom's XML documents.
	 * @return
	 */
	public static Map< String, String > getTZNamespaces() {
		if ( null == namespacesMap ) {
			namespacesMap = new HashMap<String, String>();
			namespacesMap.put( "soap", "http://www.w3.org/2003/05/soap-envelope" );
			namespacesMap.put( "tzns", "http://www.topiczoom.de:2208/TZNS" );
		}
		return namespacesMap;
	}
	
	/**
	 * Get the Jena adapter from the TDB storage layer. 
	 * This means the RDF triples are stored in a so-called "Dataset" folder on the disk.
	 * @return
	 */
	public static RdfTripleStoreAdapter getJenaStoreAdapter() {
	    if ( null == jenaStore ) {
	        // the factory will either create a bare dataset if the configured folder is empty or join the existing triples
	        jenaStore = TDBFactory.createDataset( getConfigReader().fetchValue( IConfigKeys.SEMANTICS_STORE_DATASET ) );
	    }	    
	    
	    if ( null == jenaAdapter ) {
	        jenaAdapter = new RdfTripleStoreAdapter(jenaStore, getOntologyFile(), getOntologyBaseUri(), getForceOntUpdate());
	    }	    
	    
	    
	    return jenaAdapter;
	}

	/**
	 * Get the forceUpdateOnt flag.
	 * @return
	 */
    public static boolean getForceOntUpdate() {
        if ( null == forceOntUpdate) {
            forceOntUpdate = Boolean.parseBoolean( getConfigReader().fetchValue( IConfigKeys.SEMANTICS_STORE_ONTOLOGY_FORCEUPDATE ) );
        }
        
        return forceOntUpdate;
    }

    /**
     * Get the ontology (.owl) file.
     * @return {@link File}
     */
    public static File getOntologyFile() {
        if ( null == ontologyFile ) {
	        ontologyFile = new File( getConfigReader().fetchValue( IConfigKeys.SEMANTICS_STORE_ONTOLOGY_FILE ) );
	    }
        
        return ontologyFile;
    }
    
    /**
     * Get the base uri of the own ontology (.owl) file.
     * @return {@link String}
     */
    public static String getOntologyBaseUri() {
        if ( null == ontologyBaseUri ) {
            ontologyBaseUri = getConfigReader().fetchValue( IConfigKeys.SEMANTICS_STORE_ONTOLOGY_BASEURI );
        }
        
        return ontologyBaseUri;
    }
}
