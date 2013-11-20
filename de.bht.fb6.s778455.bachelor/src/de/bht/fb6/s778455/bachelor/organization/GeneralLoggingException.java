/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * <p>This class defines an exception which realizes a general logging using the {@link Application}.</p>
 * <p>A general logging exception is meant to separate a message to be logged an a message to be presented, e.g. to an user.</p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class GeneralLoggingException extends Exception{
	private static final long serialVersionUID = 8888400943685005429L;
	protected String presentationMessage;
	protected Exception causingException;
	
	/**
	 * Build a new InvalidConfigException. A log entry will be done immediatly.
	 * 
	 * @param errorMessage the error message to be logged. Should be much more specific than the presentationMessage.
	 * @param presentationMessage the error message to be presented to the user.
	 */
	public GeneralLoggingException(String errorMessage, String presentationMessage) {
		super(errorMessage);
		
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
	 * Get the error message to be appended to a log file. <br />
	 * Usually, this error message is much more specific than the presentationMessage.
	 * @return
	 */
	public String getLogMessage() {
		return super.getMessage();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ( ( causingException == null ) ? 0 : causingException
						.hashCode() );
		result = prime
				* result
				+ ( ( presentationMessage == null ) ? 0 : presentationMessage
						.hashCode() );
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		GeneralLoggingException other = ( GeneralLoggingException ) obj;
		if( causingException == null ) {
			if( other.causingException != null )
				return false;
		} else if( !causingException.equals( other.causingException ) )
			return false;
		if( presentationMessage == null ) {
			if( other.presentationMessage != null )
				return false;
		} else if( !presentationMessage.equals( other.presentationMessage ) )
			return false;
		return true;
	}
}
