package de.bht.fb6.s778455.bachelor.organization;

import java.io.File;
/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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
    private static boolean loggerInitialized = false;
    private static Logger rootLogger;

    public enum LogType {
        INFO, WARNING, ERROR, CRITICAL;
        
        @SuppressWarnings( "deprecation" )
        public Priority getLog4jLevel() {
            switch( this ) {
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
    public static void log( final String logMessage, final LogType logType ) {
       if ( ! loggerInitialized ) {
           initializeLogger();
       }    
        
        // log in root logger
        rootLogger.log( logType.getLog4jLevel(), logMessage );
    }

    /**
     * Initialize the logging system.
     */
    public static void initializeLogger() {
        if( !loggerInitialized ) {
            if( null == System.getProperty( "log4j.configuration" ) ) {
                System.setProperty( "log4j.configuration",
                        new File( ".", "conf" + File.separatorChar
                                + "log4j.properties" ).getAbsolutePath() );
            } else {
                System.err
                        .println( "Log4j configuration file is already set..." );
            }

            BasicConfigurator.configure();
            loggerInitialized = true;
            rootLogger = Logger.getRootLogger();
        }
    }
}
