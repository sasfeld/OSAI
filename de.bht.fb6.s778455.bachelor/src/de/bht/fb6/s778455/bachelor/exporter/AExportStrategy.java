/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class defines the API for export strategies.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 *
 */
public abstract class AExportStrategy {

	/**
	 * Export the given map of courses and boards to a {@link File} instance.
	 * @param anonymizedCourses
	 * @param outputFile
	 * @return true on success
	 * @throws GeneralLoggingException 
	 */
	public abstract boolean exportToFile(Collection< Course > anonymizedCourses, File outputFile) throws GeneralLoggingException;
}
