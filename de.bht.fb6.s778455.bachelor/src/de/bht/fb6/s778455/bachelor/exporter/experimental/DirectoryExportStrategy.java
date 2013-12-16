/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.experimental;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

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

	private String encoding;

	public DirectoryExportStrategy() {
		this.encoding = "UTF-8";
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.exporter.AExportStrategy#exportToFile(java
	 * .util.Map, java.io.File)
	 */
	public boolean exportToFile( Collection< Course > anonymizedCourses,
			File outputFile ) throws GeneralLoggingException {
		if( !outputFile.exists() ) {
			throw new GeneralLoggingException(
					getClass()
							+ ":exportToFile: the given outputFile (a directory) (value: "
							+ outputFile.getAbsolutePath() + ") doesn't exist.",
					"An internal error occured in the exporter module. Please check the logs." );
		}
		if( !outputFile.isDirectory() ) {
			throw new GeneralLoggingException( getClass()
					+ ":exportToFile: the given outputFile (value: "
					+ outputFile.getAbsolutePath()
					+ " ) isn't a valid directoy.",
					"An internal error occured in the exporter module. Please check the logs." );
		}

		for( Course course : anonymizedCourses ) {
			this.createCourseDir( outputFile, course );
		}
		return false;
	}

	/**
	 * Create the directory which represents a single {@link Course}.
	 * 
	 * @param outputDir
	 * @param course
	 * @throws GeneralLoggingException
	 */
	private void createCourseDir( File outputDir, Course course )
			throws GeneralLoggingException {
		File newCourseDir = new File( outputDir,
				this.removeIllegalChars( course.getTitle() ) );
		newCourseDir.mkdir(); // create new dir immediatly.

		// create course.txt file
		File courseFile = new File( newCourseDir, "course.txt" );
		this.createTxtFile( courseFile, course.exportToTxt() );
		
		this.createBoardDirs( newCourseDir, course.getBoards() );
	}

	/**
	 * Remove illegal characters in the file name and replace them by '_'
	 * 
	 * @param title
	 * @return
	 */
	private String removeIllegalChars( String filename ) {
		return filename.replaceAll( "[^a-zA-Z0-9.-]", "_" );
	}

	/**
	 * Create the directories for each {@link Board} instance within the list of
	 * {@link Board}.
	 * 
	 * @param courseDir
	 * @param boards
	 * @throws GeneralLoggingException
	 */
	private void createBoardDirs( File courseDir, List< Board > boards )
			throws GeneralLoggingException {

		for( Board board : boards ) {
			File newBoardDir = new File( courseDir,
					this.removeIllegalChars( board.getTitle() ) );
			newBoardDir.mkdir(); // create new dir immediatly.
			
			// create board.txt file
			File boardTxt = new File( newBoardDir, "board.txt" );
			this.createTxtFile( boardTxt, board.exportToTxt() );

			for( BoardThread boardThread : board.getBoardThreads() ) {
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
	private void createBoardThreadDir( File newBoardDir, BoardThread boardThread )
			throws GeneralLoggingException {
		File newBoardThreadDir = new File( newBoardDir,
				this.removeIllegalChars( boardThread.getTitle() ) );
		newBoardThreadDir.mkdir(); // create new dir immediatly.

		File newBoardThreadFile = new File( newBoardThreadDir, "boardthread.txt" );
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
		for( Posting posting : boardThread.getPostings() ) {
			File newPostingFile = new File( newBoardThreadDir, "posting"
					+ i + ".txt" );
			this.createTxtFile( newPostingFile, posting.exportToTxt() );
			i++;
		}
	}	
	
	private void createTxtFile( File newFile, String txtContent ) throws GeneralLoggingException {
		try {
			newFile.createNewFile();
			PrintWriter writer = new PrintWriter( new OutputStreamWriter( new FileOutputStream( newFile ), this.encoding ) );
			String[] lines = txtContent.split( "\n" );
			
			for( String line : lines ) {
				writer.println(line);
			}	

			writer.flush();

			writer.close();
		} catch( IOException e ) {
			throw new GeneralLoggingException( getClass()
					+ ":createPostingFile: the posting file ("
					+ newFile.getAbsolutePath()
					+ ")couldn't be created. Original exception: \n"
					+ e.getLocalizedMessage(),
					"An internal error occured in the exporter module. Please check the logs." );

		}
	}

}
