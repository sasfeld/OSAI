/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.postprocessing.organization.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.test.framework.LoggingAwareTest;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class ConfigReaderTest extends LoggingAwareTest
{
    protected static File UNITTEST_CONFIG_FILE = new File(PATH_UNITTEST_CONFIG_FOLDER + "/configtest/postprocessing.properties");
    protected ConfigReader configReader;

    /*
     * ##################################
     * #
     * # test preparation
     * #
     * ##################################
     */
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.configReader = new ConfigReader(UNITTEST_CONFIG_FILE);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.configReader = null;
    }


    /*
     * ##################################
     * #
     * # tests
     * #
     * ##################################
     */
    @Test
    /**
     * Test of the method:
     * @see de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader#fetchValues()
     */
    public void testFetchValues() {
        final Map< String, String > configValues = this.configReader.fetchValues();
        
        // assert size -> force the devloper to check this test before he manipulates the configuration
        assertEquals( 3, configValues.size() );
        
        // assert properties' keys
        assertTrue( configValues.containsKey( IConfigKeys.POSTPROCESSING_EVENT_AFTER_ANONYMIZATION) );
        assertTrue( configValues.containsKey( IConfigKeys.POSTPROCESSING_EVENT_AFTER_IMPORT) );
        assertTrue( configValues.containsKey( IConfigKeys.POSTPROCESSING_EVENT_AFTER_SEMANTIC_EXTRACTION) );
    }   
    
    @Test
    /**
     * Test of the method:
     * @see de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader#fetchValue()
     */
    public void testFetchValue() {
        // assert properties' keys
        assertTrue ( 0 <= this.configReader.fetchValue( IConfigKeys.POSTPROCESSING_EVENT_AFTER_ANONYMIZATION ).length() );
    }
    
    @Test
    public void testGetEventPostprocessMap()
    {
        Map<PostProcessEvents, Set<IPostprocessMethod>> eventMethodMap = this.configReader.getEventPostprocessMap();
        assertEquals(3, eventMethodMap.size());
        
        for (Set<IPostprocessMethod> methods : eventMethodMap.values()) {
            assertEquals(1, methods.size());
        }       
        
    }

}
