/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.organization;

/**
 * <p>
 * This interface is the API for all ServiceFactory classes that are service locators.
 * 
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.06.2014
 * 
 */
public interface ServiceLocalisable {

    /**
     * <p>
     * Get a service class by the given serviceName.
     * The service will be located in a registry and returned immediatly.
     * </p>
     * 
     * <p>
     * If the given generic type and the located one don't match and T is not superclass of it, an {@link IllegalArgumentException} will be thrown.
     * </p>
     * 
     * <p>
     * If the service wasn't found an {@link IllegalArgumentException} will be thrown, too.
     * </p>
     *  
     * @param serviceName
     * @return
     * @throws IllegalArgumentException if no service was found or the matched one doesn't extend or equal T
     * @throws NullPointerException if serviceName is null
     */
    public <T> T getService(final String serviceName);
}
