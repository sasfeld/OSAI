/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * This simple export strategy takes a course and saves the serialization of it.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 * 
 */
public class SerializedExportStrategy extends AExportStrategy {

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.exporter.AExportStrategy#exportToFile(java
	 * .util.Collection, java.io.File)
	 */
	public boolean exportToFile( final LmsCourseSet anonymizedCourses,
			final File outputFile ) throws GeneralLoggingException {
		if( !outputFile.getParentFile().exists() ) {
			throw new GeneralLoggingException( this.getClass()
					+ ":exportToFile(): the given outputFile ("
					+ outputFile.getAbsolutePath()
					+ ") doesn't exist or is not a directoy.",
					"Internal error while trying to export the serialization. Please read the logs" );
		}
		
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream( new FileOutputStream( outputFile ) );
			os.writeObject( anonymizedCourses );			
		} catch (final IOException e) {
			throw new GeneralLoggingException( this.getClass()
					+ ":exportToFile(): exception\n" + e, "Internal error in the export module. Please read the logs"); 
		} finally {
			if ( null != os) {
				try {
					os.flush();
					os.close();
				} catch( final IOException e ) {
					// already caught
				}			
			}
		}
		return false;
	}

}
