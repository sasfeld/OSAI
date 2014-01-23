package de.bht.fb6.s778455.bachelor.organization;
/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

/**
 * <p>This is the central (static) Application class.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 14.11.2013
 *
 */
public class Application {
	public enum LogType {
		INFO,
		WARNING,
		ERROR,
		CRITICAL,
	}
	
	/**
	 * Log a given (error, warning, info,...) message.
	 * @param logMessage
	 * @param logType
	 */
	public static void log(String logMessage, LogType logType) {
		System.out.println(logType + " : " + logMessage);
		// @TODO implement file logging
	}
}
