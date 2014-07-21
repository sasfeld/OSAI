package de.bht.fb6.s778455.bachelor.organization;

import java.io.File;
/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
     * Directory (without trailing '/'!) of the loggin configuration.
     * This directory needs two files: log4j.properties (production) and log4-unittest.properties (test)
     */
    private static final String PATH_LOG4J_CONFIG = "./conf/logging";    
    /**
     * File where the sysout and syserr get logged.
     */
    private static final File LOG_FILE_UNITTEST = new File("./logs/unittests.log");

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
                String confFile = new File(PATH_LOG4J_CONFIG
                        + File.separatorChar + "log4j-unittest.properties")
                        .getAbsolutePath();
                unittestLoggerInitialized = true;
                productionLoggerInitialized = false;
                _initRootLogger(confFile);
                _enableUnitTestLog();
            }
            return;
        }

        // otherwise set production configuration file
        if (!productionLoggerInitialized) {
            String confFile = new File(PATH_LOG4J_CONFIG + File.separatorChar
                    + "log4j.properties").getAbsolutePath();

            productionLoggerInitialized = true;
            unittestLoggerInitialized = false;
            _initRootLogger(confFile);
            _enableRegularLog();
        }
    }

    private static void _enableRegularLog() {
       System.setOut(System.out);
       System.setErr(System.err);
    }

    private static void _enableUnitTestLog() {
        try {
            System.setOut(new PrintStream(new FileOutputStream(
                    LOG_FILE_UNITTEST)));
            System.setErr(new PrintStream(new FileOutputStream(
                    LOG_FILE_UNITTEST)));
        } catch (FileNotFoundException e) {
            try {
                LOG_FILE_UNITTEST.createNewFile();
            } catch (IOException | SecurityException e1) {
                System.err
                        .println("Couldn't create the unit test log file with name: "
                                + LOG_FILE_UNITTEST.getName()
                                + ". Please check security settings on the target platform.");
            }
        }
    }

    protected static void _initRootLogger(String confFile) {
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(confFile);
        rootLogger = LogManager.getRootLogger();
    }

}
