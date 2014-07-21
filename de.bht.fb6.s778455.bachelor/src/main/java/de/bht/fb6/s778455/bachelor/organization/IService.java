/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.organization;

/**
 * <p>
 * A service is located by a {@link ServiceLocalisable} implementation.<br />
 * It can be looked up by it's unique name.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.06.2014
 * 
 */
public interface IService {

    /**
     * Get the name of this service.
     * @return
     */
    public String getServiceName();
}
