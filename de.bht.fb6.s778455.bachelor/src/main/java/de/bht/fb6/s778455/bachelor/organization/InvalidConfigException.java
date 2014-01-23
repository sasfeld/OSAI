/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;



/**
 * <p>InvalidConfigExceptions are raised when there are exceptions during the config reading.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class InvalidConfigException extends GeneralLoggingException {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8734395541678502362L;

	/**
	 * Build a new InvalidConfigException. A log entry will be done immediatly.
	 * 
	 * @param errorMessage the error message to be logged. Should be much more specific than the presentationMessage.
	 * @param presentationMessage the error message to be presented to the user.
	 * @param causingException the causing {@link Exception} which raised the config exception.
	 */
	public InvalidConfigException( String errorMessage, String presentationMessage, Exception causingException ) {
		super( errorMessage, presentationMessage );
	
		this.causingException = causingException;
	}
	
	/**
	 * Get the orginal causing exception (when trying to read from the configuration).
	 * @return
	 */
	public Exception getCausingException() {
		return causingException;
	}


	
}
