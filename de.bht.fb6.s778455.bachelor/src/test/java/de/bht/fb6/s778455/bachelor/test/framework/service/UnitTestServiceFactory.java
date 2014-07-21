/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.test.framework.service;

import de.bht.fb6.s778455.bachelor.organization.IService;
import de.bht.fb6.s778455.bachelor.organization.ServiceLocalisable;
import de.bht.fb6.s778455.bachelor.organization.ServicesCache;

/**
 * <p>
 * This service factory offers instances for unit tests.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.06.2014
 * 
 */
public class UnitTestServiceFactory implements ServiceLocalisable, IService {
    public final static String SERVICE_NAME = "unittest-service-factory";
    @SuppressWarnings("unchecked")
    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.organization.ServiceLocalisable#getService(java.lang.String)
     */
    public <T> T getService(String serviceName) {
        if (null == serviceName) {
            throw new NullPointerException("The value for the argument must not be null!");
        }
        if (!ServicesCache.hasService(serviceName)) {
            throw new IllegalArgumentException("No service for the given name was found.");
        }
        
        IService service = ServicesCache.getService(serviceName);
           
        return (T) service;
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.organization.IService#getServiceName()
     */
    public String getServiceName() {
        return SERVICE_NAME;
    }

    
}
