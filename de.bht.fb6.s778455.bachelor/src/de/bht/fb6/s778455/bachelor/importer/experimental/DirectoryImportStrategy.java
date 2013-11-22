/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.experimental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

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
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importFromStream
	 * (java.io.InputStream)
	 */
	public Map< String, Board > importFromStream( InputStream inputStream ) {
		// not supported
		throw new UnsupportedOperationException(
				"DirectoyImportStrategy:importFromStream() isn't supported." );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importFromFile(java
	 * .io.File)
	 */
	public Map< String, Board > importFromFile( File inputFile )
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

		Map< String, Board > courseBoardMap = new HashMap< String, Board >();

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
			Course course = new Course( courseName );
			this.fillBoard( course, childDir, courseBoardMap );

			// add board to resulting map
			courseBoardMap.put( course.getTitle(), course.getBoard() );
		}

		return courseBoardMap;
	}

	/**
	 * Fill the board for a given {@link Course}.
	 * 
	 * @param course
	 * @param courseDir
	 *            must consist of directories which represent a thread.
	 * @param courseBoardMap
	 */
	private void fillBoard( Course course, File courseDir,
			Map< String, Board > courseBoardMap ) {
		// fully qualified name of this class + method to be printed in a log
		String fullyQualified = getClass() + ":fillBoard";
		Board courseBoard = course.getBoard();

		List< File > threadDirs = Arrays.asList( courseDir.listFiles() );

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

			BoardThread boardThread = new BoardThread();
			String threadName = threadDir.getName();
			boardThread.setTitle( threadName );
			this.fillThread( boardThread, threadDir );

			courseBoard.addThread( boardThread );
		}
	}

	/**
	 * Fill the given thread with postings included in the given threadDir.
	 * 
	 * @param boardThread
	 * @param threadDir
	 */
	private void fillThread( BoardThread boardThread, File threadDir ) {
		FilenameFilter txtFileFilter = new FilenameFilter() {
			/**
			 * Filter the posting files for *.txt files.
			 */
			@Override
			public boolean accept( File dir, String fileName ) {
				return fileName.endsWith( ".txt" );
			}
		};
		for( File postingFile : threadDir.listFiles( txtFileFilter ) ) {
			Posting p = this.parseTxtFile( postingFile );
			if( null != p ) {
				boardThread.addPosting( p );

				// use the first posting to enrich the thread's metadata
				if( postingFile.getName().contains( "posting1" ) ) {
					boardThread.setCreationDate( p.getCreationDate() );
				}
			}
		}
	}

	/**
	 * Parse an *.txt file and create a {@link Posting} instance.
	 * 
	 * @param postingFile
	 * @return
	 */
	private Posting parseTxtFile( File postingFile ) {
		// fully qualified name of this class + method to be printed in a log
		String fullyQualified = getClass() + ":parseTxtFile";

		try {
			Posting posting = new Posting();
			BufferedReader reader = new BufferedReader( new FileReader(
					postingFile ) );

			String line;
			boolean creationDateTimeMatched = false;
			boolean contentMatched = false;
			StringBuilder contentBuilder = new StringBuilder();

			while( null != ( line = reader.readLine() ) ) {
				if( !creationDateTimeMatched ) {
					Pattern pCreationDatetime = Pattern
							.compile( "CREATION_DATETIME:\\s?(.*)" );
					Matcher m = pCreationDatetime.matcher( line );
					while( m.find() ) {
						String creationDateTime = m.group( 1 );
						try {
							long timeStamp = Long.parseLong( creationDateTime );
							posting.setCreationDate( new Date( timeStamp ) );
						} catch( NumberFormatException e ) { // timestamp was
																// invalid and
																// couldn't get
																// parsed
							Application
									.log( getClass()
											+ ":parseTxtFile(): wrong value for CREATION_DATETIME in file "
											+ postingFile.getAbsolutePath()
											+ " ! The date time was ignored. Please check the file.",
											LogType.ERROR );
						}
					}
				}

				if( !contentMatched ) {
					if( line.contains( "CONTENT:" ) ) {
						contentMatched = true;
					}
				} else {
					contentBuilder.append( line );
				}
			}
			reader.close();

			posting.setContent( contentBuilder.toString() );
			return posting;
		} catch( IOException e ) {
			Application
					.log( fullyQualified
							+ ": the given posting file doesn't exist (given:  "
							+ postingFile
							+ "). Read the docs so you learn about the correct structure.",
							LogType.ERROR );
		}

		return null;
	}

}
