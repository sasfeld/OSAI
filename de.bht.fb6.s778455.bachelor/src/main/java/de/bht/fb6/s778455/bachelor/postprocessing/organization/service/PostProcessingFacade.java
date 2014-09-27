/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.organization.service;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;

/**
 * <p>The central processing facade for accesing this module's main logic from outside.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class PostProcessingFacade
{

    /**
     * Trigger an {@link PostProcessEvents}. This should be done by a controller after a special action was performed.
     * 
     * This will engage the whole logic of getting all configured {@link IPostprocessMethod} instances and calling the correct 
     * event methods.
     * 
     * This action will change the given {@link LmsCourseSet} (e.g. filter {@link Posting} contents).
     * 
     * @param postProcessEvents
     * @param lmsCourseSet
     */
    public static void triggerEvent(PostProcessEvents postProcessEvents, LmsCourseSet lmsCourseSet)
    {
        PostprocessEvent newEvent = ServiceFactory.newPostProcessEvent(postProcessEvents, lmsCourseSet);
        ServiceFactory.getEventManager().triggerEvent(newEvent);
    }
}
