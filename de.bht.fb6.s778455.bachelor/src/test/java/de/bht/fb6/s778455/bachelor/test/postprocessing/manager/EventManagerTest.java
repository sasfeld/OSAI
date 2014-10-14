/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.postprocessing.manager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.EventManager;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>Test of the {@link EventManager}</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class EventManagerTest extends NoLoggingTest
{
    /*
     * ##################################
     * #
     * # test setup
     * #
     * ##################################
     */
    protected boolean wasUpdateCalled = false;
    
    protected class DummyObserver implements Observer {
        @Override
        public void update(Observable arg0, Object arg1)
        {
            wasUpdateCalled = true;            
        }
        
    }
    
    protected EventManager eventManager;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.wasUpdateCalled = false;
        this.eventManager = ServiceFactory.getEventManager();
        this.eventManager.deleteObservers();
    }
    
    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.eventManager = null;
    }
    
    protected Observer getDummyObserver()
    {
        return new DummyObserver();
    }
    
    private PostprocessEvent getDummyEvent()
    {
        return ServiceFactory.newPostProcessEvent(PostProcessEvents.AFTER_ANONYMIZATION, new LmsCourseSet("unittest"));
    }
    

    /*
     * ##################################
     * #
     * # tests
     * #
     * ##################################
     */
    
    @Test
    public void testAddObserver()
    {
        this.eventManager.addObserver(getDummyObserver());
        assertEquals(1, this.eventManager.countObservers());              
    }
    
    @Test
    public void testDeleteObserver()
    {
        Observer dummyObserver = getDummyObserver();
        this.eventManager.addObserver(dummyObserver);
     
        this.eventManager.deleteObserver(dummyObserver);
        
        assertEquals(0, this.eventManager.countObservers());
    }
    
    @Test
    public void testTriggerEvent()
    {
        Observer dummyObserver = getDummyObserver();
        this.eventManager.addObserver(dummyObserver);
        
        // make sure that dummy observer's update() method was called
        assertFalse(this.wasUpdateCalled);
        this.eventManager.triggerEvent(getDummyEvent());
        assertTrue(this.wasUpdateCalled);
    }

  

}
