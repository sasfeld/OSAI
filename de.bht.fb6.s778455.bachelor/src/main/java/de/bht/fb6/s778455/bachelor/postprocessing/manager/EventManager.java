/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.manager;

import java.util.HashSet;
import java.util.Observer;
import java.util.Observable;
import java.util.Set;

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
    protected Set<Observer> observers;
    
    public EventManager()
    {
        this.initializeObservers();
    }
    
    protected void initializeObservers()
    {
        this.observers = new HashSet<>();
    }
    
    /**
     * Add an observer.
     * @param observer
     */
    public void addObserver( Observer observer ) 
    {
       this.observers.add(observer);
    }
    
    /**
     * Remove an observer.
     * @param observer
     */
    public void removeObserver( Observer observer ) 
    {
      this.observers.remove(observer);
    }

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
     * Notify observers.
     * @param event
     */
    public void notifyObservers(PostprocessEvent event )
    {
        for (Observer observer : this.observers) {
            observer.update(this, event);
        }
    }
 
    /**
     * Trigger an event.
     * @param event
     */
    public void triggerEvent( PostprocessEvent event ) 
    {
        this.notifyObservers(event);
    }

}
