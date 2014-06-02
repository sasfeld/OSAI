package de.bht.fb6.s778455.bachelor.organization;

import java.io.File;
/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * This is the central (static) Application class.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 14.11.2013
 * 
 */
public class Application {
    /**
     * Flag whether the logger was initialized.
     */
    private static boolean productionLoggerInitialized = false;
    private static Logger rootLogger;
    private static boolean unittestLoggerInitialized;

    public enum LogType {
        INFO, WARNING, ERROR, CRITICAL;

        @SuppressWarnings("deprecation")
        /**
         * Mapping from our to log4's log type.
         * @return
         */
        public Priority getLog4jLevel() {
            switch (this) {
            case INFO:
                return Priority.INFO;
            case WARNING:
                return Priority.WARN;
            case ERROR:
                return Priority.ERROR;
            case CRITICAL:
                return Priority.FATAL;
            default:
                return Priority.DEBUG;
            }
        }
    }

    /**
     * Log a given (error, warning, info,...) message.
     * 
     * @param logMessage
     * @param logType
     */
    public static void log(final String logMessage, final LogType logType) {
        if (!productionLoggerInitialized && !unittestLoggerInitialized) {
            initializeLogger(false);
        }

        // log in root logger
        rootLogger.log(logType.getLog4jLevel(), logMessage);
    }

    /**
     * Log a message for a given class.
     * 
     * @param logMessage
     * @param logType
     * @param clazz
     */
    public static void log(final String logMessage, final LogType logType,
            final Class<?> clazz) {
        // log in root logger
        Logger.getLogger(clazz).log(logType.getLog4jLevel(), logMessage);

    }

    /**
     * Initialize the logging system.
     * 
     * @param unitTest
     *            indicidates whether the unittest configuration should be read.
     */
    public static void initializeLogger(boolean unitTest) {
        // set unit test configuration
        if (unitTest) {
            if (!unittestLoggerInitialized) {
                String confFile = new File(".", "conf" + File.separatorChar
                        + "log4j-unittest.properties").getAbsolutePath();
                unittestLoggerInitialized = true;
                productionLoggerInitialized = false;
                _initRootLogger(confFile);
            }
            return;
        }

        // otherwise set production configuration file
        if (!productionLoggerInitialized) {
            String confFile = new File(".", "conf" + File.separatorChar
                    + "log4j.properties").getAbsolutePath();

            productionLoggerInitialized = true;
            unittestLoggerInitialized = false;
            _initRootLogger(confFile);
        }
    }

    protected static void _initRootLogger(String confFile) {
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(confFile);
        rootLogger = LogManager.getRootLogger();
    }

}
