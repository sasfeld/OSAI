/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.organization;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * The services cache should be used by all {@link ServiceLocalisable} to cache service instances.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.06.2014
 * 
 */
public class ServicesCache {
    protected static Map<String, IService> services = new HashMap<>();
    
    
    public static boolean hasService(String serviceName)
    {
        return services.containsKey(serviceName);
    }
    
    public static void addService(IService service) 
    {
        if (!hasService(service.getServiceName())) {
            services.put(service.getServiceName(), service);
        }
    }
    
    public static IService getService(final String serviceName)
    {
        return services.get(serviceName);
    }
}
