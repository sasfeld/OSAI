/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.experimental;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class realizes an export on a local file system (directory structure).</p>
 * <p>It's mostly meant to be able to analyze moodle courses, e.g. after anonymization.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 *
 */
public class DirectoryExportStrategy extends AExportStrategy {

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.exporter.AExportStrategy#exportToFile(java.util.Map, java.io.File)
	 */
	@Override
	public boolean exportToFile( Map< String, Board > courseMap, File outputFile ) throws GeneralLoggingException {
		if (outputFile.exists()) {
			throw new GeneralLoggingException(
					getClass()
							+ ":exportToFile: the given outputFile (a directory) (value: "+outputFile.getAbsolutePath()+" ) doesn't exist.",
					"An internal error occured in the exporter module. Please check the logs." );
		}
		if (!outputFile.isDirectory()) {
			throw new GeneralLoggingException(
					getClass()
							+ ":exportToFile: the given outputFile (value: "+outputFile.getAbsolutePath()+" ) isn't a valid directoy.",
					"An internal error occured in the exporter module. Please check the logs." );
		}
		
		for (String course: courseMap.keySet()) {
			this.createBoardDir(outputFile, course, courseMap.get( course ));
		}
		return false;
	}

	private void createBoardDir( File outputDir, String course, Board board ) throws GeneralLoggingException {
		File newBoardDir = new File( outputDir, course);
		boolean successCreation = newBoardDir.mkdir(); // create new dir immediatly.
		
		if (!successCreation) {
			throw new GeneralLoggingException(
					getClass()
							+ ":createBoardDir: the directory couldn't be created (value: "+newBoardDir.getAbsolutePath()+" ).",
					"An internal error occured in the exporter module. Please check the logs." );
		
		}
		
		for( BoardThread boardThread : board.getBoardThreads() ) {
			this.createBoardThreadDir( newBoardDir, boardThread);
		}		
	}

	private void createBoardThreadDir( File newBoardDir, BoardThread boardThread ) throws GeneralLoggingException {
		File newBoardThreadDir = new File( newBoardDir, boardThread.getTitle() );
		boolean successCreation = newBoardThreadDir.mkdir(); // create new dir immediatly.
		
		if (!successCreation) {
			throw new GeneralLoggingException(
					getClass()
							+ ":createBoardThreadDir: the directory couldn't be created (value: "+newBoardThreadDir.getAbsolutePath()+" ).",
					"An internal error occured in the exporter module. Please check the logs." );
		
		}
		
		int i = 1;
		for( Posting posting : boardThread.getPostings()) {
			this.createPostingFile( newBoardThreadDir, posting, i );
			i++;
		}		
	}

	private void createPostingFile( File newBoardThreadDir, Posting posting, int numberIncrement ) throws GeneralLoggingException {
		File newPostingFile = new File( newBoardThreadDir, "posting" + numberIncrement + ".txt" );
		try {
			newPostingFile.createNewFile();
			BufferedWriter writer = new BufferedWriter( new FileWriter( newPostingFile ) );
			
			writer.write( "" );
		} catch( IOException e ) {
			throw new GeneralLoggingException(
					getClass()
							+ ":createPostingFile: the posting file couldn't be created. Original exception: \n" + e.getLocalizedMessage(),
					"An internal error occured in the exporter module. Please check the logs." );
		
		}
	}

}
