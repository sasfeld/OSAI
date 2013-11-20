/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import de.bht.fb6.s778455.bachelor.organization.Application.LogType;


/**
 * <p>InvalidConfigExceptions are raised when there are exceptions during the config reading.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class InvalidConfigException extends Exception {
	protected String presentationMessage;
	protected Exception causingException;
	
	/**
	 * Build a new InvalidConfigException. A log entry will be done immediatly.
	 * 
	 * @param errorMessage the error message to be logged. Should be much more specific than the presentationMessage.
	 * @param presentationMessage the error message to be presented to the user.
	 * @param causingException the causing {@link Exception} which raised the config exception.
	 */
	public InvalidConfigException( String errorMessage, String presentationMessage, Exception causingException ) {
		super( errorMessage );
		
		this.presentationMessage = presentationMessage;
		this.causingException = causingException;
		
		this.logException();
	}
	
	/**
	 * Log the exception.
	 */
	private void logException() {
		Application.log( super.getMessage(), LogType.CRITICAL );
	}

	/**
	 * Get the error message to be presented to the user.
	 * @return {@link String}
	 */
	public String getPresentationMessage() {
		return presentationMessage;
	}

	/**
	 * Get the orginal causing exception (when trying to read from the configuration).
	 * @return
	 */
	public Exception getCausingException() {
		return causingException;
	}

	/**
	 * Get the error message to be appended to a log file. <br />
	 * Usually, this error message is much more specific than the presentationMessage.
	 * @return
	 */
	public String getLogMessage() {
		return super.getMessage();
	}
	
}
