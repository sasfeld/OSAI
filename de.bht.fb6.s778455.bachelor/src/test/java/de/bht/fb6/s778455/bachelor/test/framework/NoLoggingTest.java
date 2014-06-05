/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.test.framework;

import de.bht.fb6.s778455.bachelor.organization.Application;

/**
 * <p>
 * General class for unit tests for which the logging to STDOUT of LogType:INFO is supressed.<br />
 * Those tests use the unittest log4 configuration.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.06.2014
 * 
 */
public abstract class NoLoggingTest {
    protected static final String PATH_UNITTEST_DATA_FOLDER = "./data/unittest";

    public NoLoggingTest() 
    {
        Application.initializeLogger(true);
    }
}
