/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.manager;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.AbstractPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.service.ServiceFactory;

/**
 * <p>The method delegator is an registered {@link Observer} of {@link EventManager} and 
 * delegates the given {@link PostprocessEvent} to the configured methods of {@link IPostprocessMethod}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class MethodDelegator implements Observer
{
    
    /**
     * Try to cast the given eventObject. Otherwise throw an {@link IllegalArgumentException}.
     * @param eventObject
     * @return
     * @throws IllegalArgumentException
     */
    protected PostprocessEvent castObject(Object eventObject)
    {
        if (eventObject instanceof PostprocessEvent) {
            return (PostprocessEvent) eventObject;
        }
        
        throw new IllegalArgumentException("The given argument must be of type PostprocessEvent");
    }
    
    @Override
    /*
     * (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable observable, Object eventObject)
    {
       PostprocessEvent event = this.castObject(eventObject);       
       PostProcessEvents eventName = event.getEvent();
       
       this.triggerTemplateMethods(event, eventName);               
    }
    
    /**
     * Finally call the methods defined by {@link IPostprocessMethod} according to the given eventName.
     * @param event
     * @param eventName
     */
    private void triggerTemplateMethods(PostprocessEvent event, PostProcessEvents eventName)
    {
        Set<IPostprocessMethod> methods = this.getMethodsForEvent(eventName);
        
        for (IPostprocessMethod iPostprocessMethod : methods) {
            // set the LmsCourseSetInstance on the postprocess event attached to this event
            ((AbstractPostprocessMethod) iPostprocessMethod).setLmsCourseSet(event.getLmsCourseSet());
            
            // call afterAnonymization() method on event after_anonymization
            if (PostProcessEvents.AFTER_ANONYMIZATION.equals(eventName)) {
                iPostprocessMethod.afterAnonymization();
                continue;
            }
            
            // call afterImport() method on event after_import
            if (PostProcessEvents.AFTER_ANONYMIZATION.equals(eventName)) {
                iPostprocessMethod.afterAnonymization();
                continue;
            }
            
            // call afterSemanticExtraction() method on event after_semantic_extraction
            if (PostProcessEvents.AFTER_ANONYMIZATION.equals(eventName)) {
                iPostprocessMethod.afterAnonymization();
                continue;
            }            
        }
        
    }

    protected Set<IPostprocessMethod> getMethodsForEvent(PostProcessEvents eventName)
    {
        return ((ConfigReader) ServiceFactory.getConfigReader()).getPostprocessesForEvent(eventName);
    }
}
