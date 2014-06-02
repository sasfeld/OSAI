/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.framework;

import de.bht.fb6.s778455.bachelor.organization.Application;

/**
 * <p>General class for tests which use or test logging functionalities using the production configuration.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 30.01.2014
 *
 */
public abstract class LoggingAwareTest {
    
    public LoggingAwareTest() {
        Application.initializeLogger(false);
    }
    
    
    
}
