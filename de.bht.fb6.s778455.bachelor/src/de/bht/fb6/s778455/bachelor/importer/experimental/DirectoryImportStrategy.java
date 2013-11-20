/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.experimental;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * <p>
 * This class implements the functionality to import from a file system.
 * </p>
 * <p>
 * The structure of the file (a directory) must follow this structure:
 * 
 * <ul>
 * <li>COURSE/BOARD - DIRECTORY</li>
 * <li>
 * <ul>
 * <li>THREAD - DIRECTORY</li>
 * <li>
 * <ul>
 * <li>posting1.txt</li></li>posting2.txt</li>
 * </li>
 * </ul>
 * </li> </ul>
 * 
 * </p>
 * 
 * <p>
 * The name of the top level directory will be the name of the {@link Course}
 * instance.<br />
 * Also, the name of the thread directory will be the name of the resulting
 * {@link Thread} instance.
 * </p>
 * 
 * <p>
 * The posting files names' must follow this scheme:
 * posting[incrementalNumber].txt .<br />
 * The value for incrementalNumber starts at 1 and must be incremented by 1.
 * </p>
 * 
 * <p>
 * The content of a posting.txt file must follow this scheme: <br />
 * CREATION_DATETIME: e.g. Freitag, 28. Juni 2013, 18:37 (moodle view)<br />
 * CONTENT: (each following line will be interpreted to be content)
 * </p>
 * 
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class DirectoryImportStrategy extends AImportStrategy {

	@Override
	public Map< Course, Board > importFromStream( InputStream inputStream ) {
		// not supported
		throw new UnsupportedOperationException(
				"DirectoyImportStrategy:importFromStream() isn't supported." );
	}

	@Override
	public Map< Course, Board > importFromFile( File inputFile )
			throws GeneralLoggingException {
		// fully qualified name of this class + method to be printed in a log
		String fullyQualified = getClass() + ":importFromFile";

		if( !inputFile.exists() ) {
			throw new GeneralLoggingException(
					fullyQualified + " the given input file (input: "
							+ inputFile + ") doesn't exist.",
					"An internal error occured during the import process. Please read the log files." );
		}

		if( !inputFile.isDirectory() ) {
			throw new GeneralLoggingException(
					fullyQualified
							+ ": the given input file (input: "
							+ inputFile
							+ ") is not a directoy. See the docs to read how you need to structure it.",
					"An internal error occured during the import process. Please read the log files." );

		}
		
		Map<Course, Board> courseBoardMap = new HashMap< Course, Board >();

		// iterate through children directorys - a dir represents a course/board
		for( File childDir : inputFile.listFiles() ) {
			if( !childDir.isDirectory() ) { // append error log
				Application
						.log( fullyQualified
								+ ": the given subfolder of "
								+ inputFile
								+ " is not a directory. Read the docs so you learn about the correct structure.",
								LogType.ERROR );
				continue;
			}
			
			String courseName = childDir.getName();
			Course course = new Course(courseName);
			this.fillBoard(course, childDir, courseBoardMap);			
		}

		return null;
	}

	/**
	 * Fill the board for a given {@link Course}.
	 * @param course
	 * @param courseDir must consist of directories which represent a thread.
	 * @param courseBoardMap 
	 */
	private void fillBoard( Course course, File courseDir, Map< Course, Board > courseBoardMap ) {
		// fully qualified name of this class + method to be printed in a log
				String fullyQualified = getClass() + ":fillBoard";
		Board courseBoard = course.getBoard();
		
		List< File > threadDirs = Arrays.asList( courseDir.listFiles());
		// @TODO maybe sort the treads?
		
		for( File threadDir : threadDirs ) {
			if( !threadDir.isDirectory() ) { // append error log
				Application
						.log( fullyQualified
								+ ": the given subfolder of "
								+ courseDir
								+ " is not a directory. Read the docs so you learn about the correct structure.",
								LogType.ERROR );
				continue;
			}
			
			de.bht.fb6.s778455.bachelor.model.BoardThread boardThread = new de.bht.fb6.s778455.bachelor.model.BoardThread();
		}
		
	}

}
