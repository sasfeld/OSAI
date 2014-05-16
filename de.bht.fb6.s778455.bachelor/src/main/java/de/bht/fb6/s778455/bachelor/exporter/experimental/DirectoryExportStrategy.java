/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.experimental;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.FileUtil;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * This class realizes an export on a local file system (directory structure).
 * </p>
 * <p>
 * It's mostly meant to be able to analyze moodle courses, e.g. after
 * anonymization.
 * 
 * </p>
 * 
 * @TODO move generation of txt contents to the model!
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 * 
 */
public class DirectoryExportStrategy extends AExportStrategy {

	private final String encoding;

	public DirectoryExportStrategy() {
		this.encoding = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_ENCODING );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.exporter.AExportStrategy#exportToFile(java
	 * .util.Map, java.io.File)
	 */
	public boolean exportToFile( final LmsCourseSet anonymizedCourses,
			final File outputFile ) throws GeneralLoggingException {
		if( !outputFile.exists() ) {
			throw new GeneralLoggingException(
					this.getClass()
							+ ":exportToFile: the given outputFile (a directory) (value: "
							+ outputFile.getAbsolutePath() + ") doesn't exist.",
					"An internal error occured in the exporter module. Please check the logs." );
		}
		if( !outputFile.isDirectory() ) {
			throw new GeneralLoggingException( this.getClass()
					+ ":exportToFile: the given outputFile (value: "
					+ outputFile.getAbsolutePath()
					+ " ) isn't a valid directoy.",
					"An internal error occured in the exporter module. Please check the logs." );
		}

		for( final Course course : anonymizedCourses ) {
			this.createCourseDir( outputFile, course );
		}
		return true;
	}

	/**
	 * Create the directory which represents a single {@link Course}.
	 * 
	 * @param outputDir
	 * @param course
	 * @throws GeneralLoggingException
	 */
	private void createCourseDir( final File outputDir, final Course course )
			throws GeneralLoggingException {
		final File newCourseDir = new File( outputDir,
				this.removeIllegalChars( course.getTitle() ) );
		newCourseDir.mkdir(); // create new dir immediatly.

		// create course.txt file
		final File courseFile = new File( newCourseDir, "course.txt" );
		this.createTxtFile( courseFile, course.exportToTxt() );

		this.createBoardDirs( newCourseDir, course.getBoards() );
	}

	/**
	 * Remove illegal characters in the file name and replace them by '_'
	 * 
	 * @param title
	 * @return
	 */
	private String removeIllegalChars( final String filename ) {
		return FileUtil.removeIllegalChars( filename );
	}

	/**
	 * Create the directories for each {@link Board} instance within the list of
	 * {@link Board}.
	 * 
	 * @param courseDir
	 * @param boards
	 * @throws GeneralLoggingException
	 */
	private void createBoardDirs( final File courseDir, final List< Board > boards )
			throws GeneralLoggingException {

		for( final Board board : boards ) {
			final File newBoardDir = new File( courseDir,
					this.removeIllegalChars( board.getTitle() ) );
			newBoardDir.mkdir(); // create new dir immediatly.

			// create board.txt file
			final File boardTxt = new File( newBoardDir, "board.txt" );
			this.createTxtFile( boardTxt, board.exportToTxt() );

			for( final BoardThread boardThread : board.getBoardThreads() ) {
				this.createBoardThreadDir( newBoardDir, boardThread );
			}
		}

	}

	/**
	 * Create a new directoy for the given {@link BoardThread}
	 * 
	 * @param newBoardDir
	 * @param boardThread
	 * @throws GeneralLoggingException
	 */
	private void createBoardThreadDir( final File newBoardDir, final BoardThread boardThread )
			throws GeneralLoggingException {
		final File newBoardThreadDir = new File( newBoardDir,
				this.removeIllegalChars( boardThread.getTitle() ) );
		newBoardThreadDir.mkdir(); // create new dir immediatly.

		final File newBoardThreadFile = new File( newBoardThreadDir,
				"boardthread.txt" );
		this.createTxtFile( newBoardThreadFile, boardThread.exportToTxt() );
		// if (!successCreation) {
		// throw new GeneralLoggingException(
		// getClass()
		// +
		// ":createBoardThreadDir: the directory couldn't be created (value: "+newBoardThreadDir.getAbsolutePath()+" ).",
		// "An internal error occured in the exporter module. Please check the logs."
		// );
		//
		// }

		int i = 1;
		for( final Posting posting : boardThread.getPostings() ) {
			final File newPostingFile = new File( newBoardThreadDir, "posting" + i
					+ ".txt" );
			this.createTxtFile( newPostingFile, posting.exportToTxt() );
			i++;
		}
	}

	private void createTxtFile( final File newFile, final String txtContent )
			throws GeneralLoggingException {
		try {
			newFile.createNewFile();
			PrintWriter writer = null;
			
			if( this.encoding.equals( "false" ) ) {
				writer = new PrintWriter( newFile );
			} else {
				writer = new PrintWriter( new OutputStreamWriter(
						new FileOutputStream( newFile ), this.encoding ) );
			}
			final String[] lines = txtContent.split( "\n" );

			for( final String line : lines ) {
				writer.println( line );
			}

			writer.flush();

			writer.close();
		} catch( final IOException e ) {
			throw new GeneralLoggingException( this.getClass()
					+ ":createTxtFile: the file ("
					+ newFile.getAbsolutePath()
					+ ")couldn't be created. Original exception: \n"
					+ e.getLocalizedMessage(),
					"An internal error occured in the exporter module. Please check the logs." );

		}
	}

}
