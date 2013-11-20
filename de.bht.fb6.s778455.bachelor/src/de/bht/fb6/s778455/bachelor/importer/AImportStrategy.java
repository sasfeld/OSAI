/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.importer;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>This abstract class is the API for the import strategy (strategy pattern).</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public abstract class AImportStrategy {
	
	/**
	 * <p>Import raw data from an {@link InputStream} and construct a {@link Map} with {@link Course} instances as keys and {@link Board} instances as values.</p>
	 * <p>This will grant a fast access to a learning course's (e.g. a "Moodle course") board.
	 * @param inputStream any {@link InputStream} (e.g.: from an HTTP resource, a file resource,...)
	 * @return a {@link Map} of key value pairs.
	 */
	abstract public Map<Course, Board> importFromStream(InputStream inputStream);
	
	/**
	 * <p>Import raw data from a {@link File} and construct a {@link Map} with {@link Course} instances as keys and {@link Board} instances as values.</p>
	 * <p>This will grant a fast access to a learning course's (e.g. a "Moodle course") board.
	 * @param input File any {@link File} 
	 * @return a {@link Map} of key value pairs.
	 * @throws GeneralLoggingException 
	 */
	abstract public Map<Course, Board> importFromFile(File inputFile) throws GeneralLoggingException;
}
