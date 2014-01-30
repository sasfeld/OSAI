/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.store;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>This class contains tests of the {@link RdfTripleStoreAdapter}.</p>
 * 
 * <p>Important functionalities to be tested:
 *  <ul>
 *      <li>The Jena {@link OntModel}</li>
 *      <li>SPARQL functionalities - does the Insert and Select work?</li>
 *      <li>Manipulation of Jena {@link Model}</li>
 *  </ul>
 * </p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 30.01.2014
 *
 */
public class RdfTripleStoreAdapterTest {
    /**
     * Path to the dataset for unit testing.
     */
    private static final String LOCATION_UNIT_TEST_DS = "./data/semantics/unittest/testdataset";
    protected RdfTripleStoreAdapter adapter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        final Dataset jenaStore = TDBFactory.createDataset( LOCATION_UNIT_TEST_DS );
        final File ontologyFile = ServiceFactory.getOntologyFile();
        
        this.adapter = new RdfTripleStoreAdapter( jenaStore, ontologyFile );
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.adapter = null;
    }

    @Test
    /**
     * Some tests of the {@link OntModel}
     */
    public void testOntologyModel() {
       System.out.println(this.adapter.showOntologyTriples());
    }

}
