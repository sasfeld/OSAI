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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.IDirectoryPortable;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.FileUtil;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * This class implements the functionality to import from a file system.
 * </p>
 * <p>
 * The structure of the file (a directory) must follow this structure:
 * 
 * <ul>
 * <li>COURSE - DIRECTORY</li>
 * <li>
 * <ul>
 * <li>BOARD - DIRECTORY</li>
 * <ul>
 * <li>THREAD - DIRECTORY</li>
 * <li>
 * <ul>
 * <li>posting1.txt</li></li>posting2.txt</li>
 * </li>
 * </ul>
 * </li> </ul> </ul> </li> </ul>>
 * 
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
	private static final String PERSON_CORPUS_DIR = "namecorpora";
	private static final String PERSON_CORPUS_PRENAME_FILE = "prenames.txt";
	private static final String PERSON_CORPUS_LASTNAME_FILE = "lastnames.txt";
	protected String boardSpecificImport;

	/**
	 * Create and prepare a new DirectoryImportStrategy instance.
	 */
	public DirectoryImportStrategy() {
		this.boardSpecificImport = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importFromStream
	 * (java.io.InputStream)
	 */
	public Set< Course > importBoardFromStream( InputStream inputStream ) {
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
	public Set< Course > importBoardFromFile( File inputFile )
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

		Set< Course > courseSet = new HashSet< Course >();

		// iterate through children directorys - a dir represents a course/board
		for( File childDir : inputFile.listFiles() ) {
			if( childDir.isDirectory() ) { // append error log

				String courseName = childDir.getName();
				Course course = new Course( courseName );

				File courseTxtFile = new File( childDir, "course.txt" );
				// find course.txt, parse it
				this.importTxtFile( courseTxtFile, course );

				this.fillBoards( course, childDir );

				// add board to resulting map
				courseSet.add( course );
			}
		}

		return courseSet;
	}

	/**
	 * Fill all boards for the given {@link Course}.
	 * 
	 * @param course
	 * @param childDir
	 * @param courseBoardMap
	 */
	private void fillBoards( Course course, File courseDir ) {
		// fully qualified name of this class + method to be printed in a log
		String fullyQualified = getClass() + ":fillBoards";

		// bare course board list to be filled
		List< Board > courseBoards = course.getBoards();

		boolean specificCorpusIncluded = false;
		for( File boardDir : courseDir.listFiles() ) {
			// check if dir is a person corpus dir
			if( boardDir.getName().equals( PERSON_CORPUS_DIR ) ) {
				if( this.boardSpecificImport.equals( "true" )
						|| this.boardSpecificImport.equals( "fallback" ) ) {
					try {
						PersonNameCorpus bareCorpus = new PersonNameCorpus();
						File prenameFile = new File( boardDir,
								PERSON_CORPUS_PRENAME_FILE );
						this.fillFromFile( prenameFile, bareCorpus,
								PersonNameType.PRENAME );
						File lastnameFile = new File( boardDir,
								PERSON_CORPUS_LASTNAME_FILE );
						this.fillFromFile( lastnameFile, bareCorpus,
								PersonNameType.LASTNAME );
						// set specific corpus instance
						course.setPersonNameCorpus( bareCorpus );
						specificCorpusIncluded = true;
					} catch( GeneralLoggingException e ) {
						// log was written, just continue
					}
				}
			} else if( boardDir.isDirectory() ) { // append error log
				// create new Board instance
				Board newBoard = new Board( course );
				newBoard.setTitle( boardDir.getName() );

				// fill from board.txt
				File boardTxt = new File( boardDir, "board.txt" );
				this.importTxtFile( boardTxt, newBoard );

				this.fillBoard( newBoard, boardDir );

				courseBoards.add( newBoard );
			} 
		}

		// course specific corpus was not found
		if( !specificCorpusIncluded
				&& this.boardSpecificImport.equals( "fallback" ) ) {
			Application.log( getClass()
					+ ":fillBoards: fallback to global person corpus.",
					LogType.INFO );
			// set singleton corpus
			course.setPersonNameCorpus( ServiceFactory
					.getPersonNameCorpusSingleton() );
		}
	}

	private void fillBoard( Board newBoard, File boardDir ) {
		// fully qualified name of this class + method to be printed in a log
		String fullyQualified = getClass() + ":fillBoard";

		for( File threadDir : boardDir.listFiles() ) {
			if( threadDir.isDirectory() ) {
				// create new thread
				BoardThread boardThread = new BoardThread( newBoard );
				String threadName = threadDir.getName();
				boardThread.setTitle( threadName );

				// import boardthread.txt
				File boardThreadFile = new File( threadDir, "boardthread.txt" );
				this.importTxtFile( boardThreadFile, boardThread );

				this.fillThread( boardThread, threadDir );

				newBoard.addThread( boardThread );
			}
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
			Posting p = new Posting( boardThread );

			// import from posting file
			this.importTxtFile( postingFile, p );
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
	 * Import any model which implements {@link IDirectoryPortable}
	 * 
	 * @param importFile
	 * @param portableModel
	 */
	private void importTxtFile( File importFile,
			IDirectoryPortable portableModel ) {
		// fully qualified name of this class + method to be printed in a log
		String fullyQualified = getClass() + ":parseTxtFile";

		try {
			BufferedReader reader = new BufferedReader( new FileReader(
					importFile ) );

			String line;
			boolean contentMatched = false;
			boolean taggedContentMatched = false;
			StringBuilder contentBuilder = new StringBuilder();
			StringBuilder taggedContentBuilder = new StringBuilder();

			while( null != ( line = reader.readLine() ) ) {
				if( !contentMatched && !taggedContentMatched ) {
					if( line.startsWith( "CONTENT:" ) ) {
						contentMatched = true;
					} else { // match key value pairs
						Pattern pKeyValue = Pattern
								.compile( "^([A-Z]+):\\s(.*?)$" );
						Matcher matcher = pKeyValue.matcher( line );
						while( matcher.find() ) {
							String key = matcher.group( 1 );
							String value = matcher.group( 2 );

							portableModel.importFromTxt( key, value );
						}
					}
				} else if( contentMatched && !taggedContentMatched ) {
					if( line.startsWith( "TAGGED_CONTENT:" ) ) {
						taggedContentMatched = true;
					} else {
						contentBuilder.append( line + "\n" );
					}
				} else { // !contentMatched && taggedContentMatched
					taggedContentBuilder.append( line + "\n" );
				}
			}
			reader.close();

			portableModel.importFromTxt( "CONTENT", contentBuilder.toString() );
			portableModel.importFromTxt( "TAGGED_CONTENT",
					taggedContentBuilder.toString() );
		} catch( IOException | IllegalArgumentException e ) {
			Application.log( fullyQualified + ": exception occured: " + e,
					LogType.ERROR );
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#fillFromFile(java
	 * .io.File, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus,
	 * de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType)
	 */
	public PersonNameCorpus fillFromFile( File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType )
			throws GeneralLoggingException {
		if( !personCorpus.exists() ) {
			throw new GeneralLoggingException( getClass()
					+ ":fillFromFile: the given file doesn't exist. File was: "
					+ personCorpus.getAbsolutePath(),
					"Internal error in the import module. Please read the logs." );
		}

		if( !personCorpus.isFile() ) {
			throw new GeneralLoggingException(
					getClass()
							+ ":fillFromFile: the given file doesn't appear to be a file. File was: "
							+ personCorpus.getAbsolutePath(),
					"Internal error in the import module. Please read the logs." );

		}

		List< String > lines = FileUtil.readFileLineBased( personCorpus );

		int lineNumber = 0;
		for( String line : lines ) {
			lineNumber++;
			// line must only consist of letters
			if( line.matches( "^[^\\s]+$" ) ) {
				corpusInstance.fillName( nameType, line, false ); // case-insensitive
			} else {
				Application
						.log( getClass()
								+ "fillFromFile(): illegal line in person name corpus file ("
								+ personCorpus.getAbsolutePath()
								+ "). Line number: " + lineNumber,
								LogType.ERROR );
			}
		}

		return corpusInstance;
	}

}
