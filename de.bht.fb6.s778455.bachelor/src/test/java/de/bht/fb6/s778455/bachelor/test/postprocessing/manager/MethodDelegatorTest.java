/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.postprocessing.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.MethodDelegator;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.AbstractPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class MethodDelegatorTest extends NoLoggingTest
{
    /*
     * ##################################
     * #
     * # test preparation
     * #
     * ##################################
     */
    protected static File UNITTEST_CONFIG_FILE = new File(PATH_UNITTEST_CONFIG_FOLDER + "/configtest/postprocessing.properties");
    protected static ConfigReader CONFIG_READER = new ConfigReader(UNITTEST_CONFIG_FILE);

    protected MethodDelegator methodDelegator;
    
    protected boolean afterImportCalled = false;
    protected boolean afterAnonymizationCalled = false;
    protected boolean afterSemanticExtractionCalled = false;
    
    /**
     * 
     * <p>This mock makes sure that the unittest properties file is used.</p>
     *
     * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
     * @since 27.09.2014
     *
     */
    protected class MethodDelegatorMock extends MethodDelegator
    {
        @Override
        protected Set<IPostprocessMethod> getMethodsForEvent(PostProcessEvents eventName)
        {
            HashSet<IPostprocessMethod> hashSet = new HashSet<>();
            // AFTER_IMPORT only
            if (PostProcessEvents.AFTER_IMPORT.equals(eventName)) {
                hashSet.add(new DummyTemplateMethod());
            }
            
            return hashSet;
        }        
    }
    
    protected class DummyObservable extends Observable
    {
        
    }
    
    /**
     * 
     * <p>Dummy template method for unit testing.</p>
     *
     * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
     * @since 27.09.2014
     *
     */
    public class DummyTemplateMethod extends AbstractPostprocessMethod
    {

        @Override
        public void afterImport()
        {
            afterImportCalled = true;            
        }

        @Override
        public void afterAnonymization()
        {
            afterAnonymizationCalled = true;            
        }

        @Override
        public void afterSemanticExtraction()
        {
            afterSemanticExtractionCalled = true;            
        }

        @Override
        protected void applyMethod()
        {
                       
        }
        
    }
    
    private PostprocessEvent getDummyEvent(PostProcessEvents event)
    {
        return ServiceFactory.newPostProcessEvent(event, new LmsCourseSet("unittest"));
    }
    
    private Observable getDummyObservable()
    {
        return new DummyObservable();
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.methodDelegator = new MethodDelegatorMock();
        this.afterAnonymizationCalled = false;
        this.afterImportCalled = false;
        this.afterSemanticExtractionCalled = false;
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        this.methodDelegator = null;
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
     * Make sure that the mapping between the triggered event and the called template method is correct.
     */
    public void testUpdate()
    {        
        // make sure that the afterImport() method of the TemplateMethodDummy is called on event AFTER_IMPORT
        PostprocessEvent dummyEvent = this.getDummyEvent(PostProcessEvents.AFTER_IMPORT);        
        assertFalse(this.afterImportCalled);
        this.methodDelegator.update(getDummyObservable(), dummyEvent);        
        assertTrue(this.afterImportCalled);
        
        // make sure that all others were NOT called
        assertFalse(this.afterAnonymizationCalled);
        assertFalse(this.afterSemanticExtractionCalled);        
    }

  

}
