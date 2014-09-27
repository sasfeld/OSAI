/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.organization.service;

import java.util.Observer;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.EventManager;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.MethodDelegator;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.method.JavaCodeFilter;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.ConfigReader;

/**
 * <p>ServiceFactory for the postprocessing module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class ServiceFactory
{
    protected static ConfigReader configReader;
    protected static EventManager eventManager;
    
    /**
     * Get the {@link IConfigReader} for this module.
     * @return
     */
    public static IConfigReader getConfigReader()
    {
        if (null == configReader) {
            configReader = new ConfigReader();
        }
        
        return configReader;
    }
    
    /**
     * Get the {@link EventManager}.
     * 
     * If not done yet, set the {@link Observer} instances for this module.
     *  
     * 
     * @return
     */
    public static EventManager getEventManager()
    {
        if (null == eventManager) {
            eventManager = newEventManager();
        }
        
        return eventManager;
    }

    /**
     * Create a new EventManager and set the module-based observers.
     * 
     * Don't make this method public since a singleton instance should be fetched using getEventManager().
     * @return
     */
    private static EventManager newEventManager()
    {
        EventManager manager = new EventManager();     
        manager.addObserver(new MethodDelegator());
        
        return manager;
    }

    /**
     * Generate a new {@link JavaCodeFilter} instance.
     * 
     * Set the dependencies and return the fresh instance.
     * 
     * @return
     */
    public static IPostprocessMethod newJavaCodeFilter()
    {
        JavaCodeFilter codeFilter = new JavaCodeFilter();
        return codeFilter;
    }

    /**
     * Generate a new {@link PostprocessEvent} and set dependencies.
     * @param postProcessEvents
     * @param lmsCourseSet
     * @return
     */
    public static PostprocessEvent newPostProcessEvent(PostProcessEvents postProcessEvents, LmsCourseSet lmsCourseSet)
    {
        PostprocessEvent newEvent = new PostprocessEvent();
        newEvent.setLmsCourseSet(lmsCourseSet);
        newEvent.setEvent(postProcessEvents);
        
        return newEvent;
    } 
}
