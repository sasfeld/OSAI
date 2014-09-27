/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.organization.service;

import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
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
}
