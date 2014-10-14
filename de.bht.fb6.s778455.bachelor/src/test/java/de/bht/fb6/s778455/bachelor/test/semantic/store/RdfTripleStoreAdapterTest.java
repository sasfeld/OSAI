/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;
import de.bht.fb6.s778455.bachelor.test.framework.JenaFrameworkTest;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link RdfTripleStoreAdapter}.
 * </p>
 * 
 * <p>
 * Important functionalities to be tested:
 * <ul>
 * <li>The Jena {@link OntModel}</li>
 * <li>SPARQL functionalities - does the Insert and Select work?</li>
 * <li>Manipulation of Jena {@link Model}</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 30.01.2014
 * 
 */
public class RdfTripleStoreAdapterTest extends NoLoggingTest {
    protected RdfTripleStoreAdapter adapter;

    /**
     * As a logging aware test, call the super constructor.
     */
    public RdfTripleStoreAdapterTest() {
        super();
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.adapter = JenaFrameworkTest.getRdfTripleStoreAdapter();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.adapter = null;
    }

    @Test
    public void testIncrementVersion() throws NoSuchMethodException,
            SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        // get reflection method
        final Method method = RdfTripleStoreAdapter.class.getDeclaredMethod(
                "incrementVersion", Boolean.class );
        method.setAccessible( true );

        // inject some value
        final Field f = RdfTripleStoreAdapter.class
                .getDeclaredField( "currentVersion" );
        f.setAccessible( true );
        f.set( this.adapter, "V_30_01_2014_1" );

        // Check values
        String result = ( String ) method.invoke( this.adapter, false );
        String expected = "V_"
                + new SimpleDateFormat( "dd_MM_yyyy" ).format( new Date() )
                + "_2";

        assertEquals( expected, result );

        result = ( String ) method.invoke( this.adapter, false );
        expected = "V_"
                + new SimpleDateFormat( "dd_MM_yyyy" ).format( new Date() )
                + "_3";

        assertEquals( expected, result );
    }

    @Test
    public void testReleaseModel() throws Exception {
        final OntModel ontModel = this.adapter.getPureOntologyModel();
        // final Model ontModel = ModelFactory.createDefaultModel();

        // final OntModel ontModel = ModelFactory.createOntologyModel();

        assertTrue( null != ontModel );

        final Resource subject = ResourceFactory
                .createResource( "http://example.org/test" );
        final Property predicate = ResourceFactory.createProperty(
                "http://example.org/", "hasAge" );
        final RDFNode object = ResourceFactory.createTypedLiteral( 10 );

        // add some triples
        final Statement s = new StatementImpl( subject, predicate, object );
        ontModel.add( s );

        // release model
        final String oldVersion = this.adapter.getCurrentVersion();
        this.adapter.releaseModel( ontModel, false );

        // assert another version now
        assertTrue( !oldVersion.equals( this.adapter.getCurrentVersion() ) );

        // assert triple
        this.adapter.setWorkingVersion( this.adapter.getCurrentVersion() );
        final Set< Statement > statements = this.adapter.getStatements();

        boolean statementContained = false;
        for( final Statement statement : statements ) {
            if( statement.getSubject().equals( s.getSubject() )
                    && statement.getPredicate().equals( s.getPredicate() )  ) {
                statementContained = true;
                break;
            }
        }

        assertTrue( statementContained );
    }

    @Test
    public void testSome() {
        System.out.println( "Versions: " );
        System.out.println( this.adapter.getAvailableVersions() );
    }

    /**
     * Some tests of the {@link OntModel}
     */
    @Test
    public void testOntologyModel() {
        assertEquals( 4, this.adapter.getImportedOntologies().size() );
        assertEquals( 11, this.adapter.getOntologieClasses().size() );
        assertEquals( 6, this.adapter.getOntologieDatatypeProperties().size() );
//        assertEquals( 90, this.adapter.getOntologieIndividuals().size() );
    }

}
