package de.bht.fb6.s778455.bachelor.exporter;

import java.util.Arrays;

import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>
 * This enum contains the allowed export methods that can be used.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.12.2013
 * 
 */
public enum ExportMethod {
	FILESYSTEM;
	
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
      if (null != allowedInputParameters) {
        // fetch all enum values
        ExportMethod[] exportMethods = values();
        String[] allowedParameters = new String[exportMethods.length];
        
        // put the String representation of each enum value into an array
        for (int i = 0; i < allowedParameters.length; i++) {
        allowedParameters[i] = exportMethods[i].name().toLowerCase();
      }
        
        // convert the enum value String array into a String representation
        allowedInputParameters = Arrays.toString(allowedParameters);
      }
      
      return allowedInputParameters;
    }
    
    /**
     * Map the given inputParameter (handed in by user) to the available export methods.
     * @param inputParameter
     * @return
     * @throws GeneralLoggingException if the input parameter could not be mapped.
     */
    public static ExportMethod mapInputParameter(String inputParameter) throws GeneralLoggingException
    {
      try {
        return ExportMethod.valueOf( inputParameter.toUpperCase().trim() );
      } catch (IllegalArgumentException e) {
        throw new GeneralLoggingException(":mapInputParameter(): could not map given export method " + inputParameter,
            "The given export method does not exist. Please choose one of " + getAllowedInputParameters());
      }
    }
}