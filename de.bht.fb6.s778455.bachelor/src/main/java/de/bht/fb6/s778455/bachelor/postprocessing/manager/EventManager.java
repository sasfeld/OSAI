/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.manager;

import java.util.Observable;

import de.bht.fb6.s778455.bachelor.organization.IService;



/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class EventManager extends Observable implements IService
{
    public static String SERVICE_NAME = "postprocess_event_manager";    
    

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.organization.IService#getServiceName()
     */
    public String getServiceName() 
    {
        return SERVICE_NAME;
    }
 
    /**
     * Trigger an event.
     * @param event
     */
    public void triggerEvent( PostprocessEvent event ) 
    {
        this.setChanged();
        this.notifyObservers(event);
    }

}
