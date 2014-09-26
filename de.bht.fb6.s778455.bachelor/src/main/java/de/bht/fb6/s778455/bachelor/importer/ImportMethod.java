/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.importer;

import java.util.Arrays;

import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>
 * This enum contains the allowed import methods to be used.
 * 
 * They are usually hand in as parameter (e.g. in CLI controllers).
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.12.2013 
 * 
 */
public enum ImportMethod
{  
	POSTGREDUMP, FILESYSTEM, LUEBECK_XML, AUDITORIUM_DB, OLIVER_LUEBECK_XML;
	
	/**
	 * String containing all allowed input parameters.
	 */
	protected static String allowedInputParameters;
	
	/**
	 * Get a string showing all allowed values for the parameter importMethod.
	 * 
	 * The user should hand in one of these parameters, they are getting mapped to an import method in here.
	 * 
	 * @return
	 */
	public static String getAllowedInputParameters()
	{
	  // be lazy and generate the parameters string only once
	  if (null == allowedInputParameters) {
	    // fetch all enum values
	    ImportMethod[] importMethods = values();
	    String[] allowedParameters = new String[importMethods.length];
	    
	    // put the String representation of each enum value into an array
	    for (int i = 0; i < allowedParameters.length; i++) {
        allowedParameters[i] = importMethods[i].name().toLowerCase();
      }
	    
	    // convert the enum value String array into a String representation
	    allowedInputParameters = Arrays.toString(allowedParameters);
	  }
	  
	  return allowedInputParameters;
	}
	
	/**
	 * Map the given inputParameter (handed in by user) to the available import methods.
	 * @param inputParameter
	 * @return
	 * @throws GeneralLoggingException if the input parameter could not be mapped.
	 */
	public static ImportMethod mapInputParameter(String inputParameter) throws GeneralLoggingException
	{
	  try {
	    return ImportMethod.valueOf( inputParameter.toUpperCase().trim() );
	  } catch (IllegalArgumentException e) {
	    throw new GeneralLoggingException(":mapInputParameter(): could not map given import method " + inputParameter,
	        "The given import method does not exist. Please choose one of " + getAllowedInputParameters());
	  }
	}
}
