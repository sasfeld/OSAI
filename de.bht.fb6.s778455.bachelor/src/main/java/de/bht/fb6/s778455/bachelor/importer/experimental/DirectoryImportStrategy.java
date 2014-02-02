/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.experimental;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
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
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
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
	protected String encoding;
	protected PersonNameCorpus personCorpus = null;

	/**
	 * Create and prepare a new DirectoryImportStrategy instance.
	 */
	public DirectoryImportStrategy() {
		this.boardSpecificImport = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC );
		this.encoding = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_ENCODING );

		if( ServiceFactory
				.getConfigReader()
				.fetchValue(
						IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC )
				.equals( "false" ) ) {
			this.personCorpus = ServiceFactory.getPersonNameCorpusSingleton();
			final String prenamesFile = ServiceFactory
					.getConfigReader()
					.fetchValue(
							IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_NAMECORPUS_PRENAMES );
			final String lastnamesFile = ServiceFactory
					.getConfigReader()
					.fetchValue(
							IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_NAMECORPUS_LASTNAMES );
			this.fillSingletonPersonCorpus( this.personCorpus, prenamesFile,
					lastnamesFile );
		}
		// else: personCorpus = null
	}

	/**
	 * Fill the given personCorpus singleton by two *.txt files.
	 * 
	 * @param personCorpus
	 * @param prenamesFile
	 * @param lastnamesFile
	 */
	private void fillSingletonPersonCorpus( final PersonNameCorpus personCorpus,
			final String prenamesFile, final String lastnamesFile ) {
		final File fPrenamesFile = new File( prenamesFile );
		final File fLastnamesFile = new File( lastnamesFile );

		if( !fPrenamesFile.exists() ) {
			Application
					.log( this.getClass()
							+ ":fillSingletonPersonCorpus: the configured corpus files "
							+ fPrenamesFile.getAbsolutePath()
							+ " doesn't exist.", LogType.CRITICAL );
		} else {
			try {
				this.fillFromFile( fPrenamesFile, personCorpus,
						PersonNameType.PRENAME );
			} catch( final GeneralLoggingException e ) {
				// exception makes a log
			}
		}

		if( !fLastnamesFile.exists() ) {
			Application.log( this.getClass()
					+ ":fillSingletonPersonCorpus: the configured corpus file "
					+ fLastnamesFile.getAbsolutePath() + " doesn't .",
					LogType.CRITICAL );
		} else {
			try {
				this.fillFromFile( fLastnamesFile, personCorpus,
						PersonNameType.LASTNAME );
			} catch( final GeneralLoggingException e ) {
				// exception makes a log
			}
		}

	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importFromStream
	 * (java.io.InputStream)
	 */
	public Set< Course > importBoardFromStream( final InputStream inputStream ) {
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
	public LmsCourseSet importBoardFromFile( final File inputFile )
			throws GeneralLoggingException {
		// fully qualified name of this class + method to be printed in a log
		final String fullyQualified = this.getClass() + ":importFromFile";

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

		final LmsCourseSet courseSet = new LmsCourseSet();
		// set the name of the CourseSet from the upper directory name
		courseSet.setName( inputFile.getName() );

		// iterate through children directorys - a dir represents a course/board
		for( final File childDir : inputFile.listFiles() ) {
			if( childDir.isDirectory() ) { // append error log

				final String courseName = childDir.getName();
				final Course course = new Course( courseName );

				final File courseTxtFile = new File( childDir, "course.txt" );
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
	private void fillBoards( final Course course, final File courseDir ) {
		// bare course board list to be filled
		final List< Board > courseBoards = course.getBoards();

		boolean specificCorpusIncluded = false;
		for( final File boardDir : courseDir.listFiles() ) {
			// check if dir is a person corpus dir
			if( boardDir.getName().equals( PERSON_CORPUS_DIR ) ) {
				if( this.boardSpecificImport.equals( "true" )
						|| this.boardSpecificImport.equals( "fallback" ) ) {
					try {
						final PersonNameCorpus bareCorpus = new PersonNameCorpus();
						final File prenameFile = new File( boardDir,
								PERSON_CORPUS_PRENAME_FILE );
						this.fillFromFile( prenameFile, bareCorpus,
								PersonNameType.PRENAME );
						final File lastnameFile = new File( boardDir,
								PERSON_CORPUS_LASTNAME_FILE );
						this.fillFromFile( lastnameFile, bareCorpus,
								PersonNameType.LASTNAME );
						// set specific corpus instance
						course.setPersonNameCorpus( bareCorpus );
						specificCorpusIncluded = true;
					} catch( final GeneralLoggingException e ) {
						// log was written, just continue
					}
				}
			} else if( boardDir.isDirectory() ) { // append error log
				// create new Board instance
				final Board newBoard = new Board( course );
				newBoard.setTitle( boardDir.getName() );

				// fill from board.txt
				final File boardTxt = new File( boardDir, "board.txt" );
				this.importTxtFile( boardTxt, newBoard );

				this.fillBoard( newBoard, boardDir );

				courseBoards.add( newBoard );
			}
		}

		// course specific corpus was not found or it is turned off in
		// configuration (value: false)
		if( ( !specificCorpusIncluded && this.boardSpecificImport
				.equals( "fallback" ) )
				|| this.boardSpecificImport.equals( "false" ) ) {
			Application.log( this.getClass()
					+ ":fillBoards: fallback to global person corpus.",
					LogType.INFO );
			// set singleton corpus
			course.setPersonNameCorpus( ServiceFactory
					.getPersonNameCorpusSingleton() );
		}
	}

	private void fillBoard( final Board newBoard, final File boardDir ) {
		for( final File threadDir : boardDir.listFiles() ) {
			if( threadDir.isDirectory() ) {
				// create new thread
				final BoardThread boardThread = new BoardThread( newBoard );
				final String threadName = threadDir.getName();
				boardThread.setTitle( threadName );

				// import boardthread.txt
				final File boardThreadFile = new File( threadDir, "boardthread.txt" );
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
	private void fillThread( final BoardThread boardThread, final File threadDir ) {
		final FilenameFilter txtFileFilter = new FilenameFilter() {
			/**
			 * Filter the posting files for posting *.txt files.
			 */
			@Override
			public boolean accept( final File dir, final String fileName ) {
				return fileName.startsWith( "posting" )
						&& fileName.endsWith( ".txt" );
			}
		};
		for( final File postingFile : threadDir.listFiles( txtFileFilter ) ) {
			final Posting p = new Posting( boardThread );

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
	private void importTxtFile( final File importFile,
			final IDirectoryPortable portableModel ) {
		// fully qualified name of this class + method to be printed in a log
		final String fullyQualified = this.getClass() + ":importTxtFile";

		List< String > lines;
		try {
			lines = FileUtil.readFileLineBased( importFile, this.encoding );

			boolean contentMatched = false;
			boolean taggedContentMatched = false;
			final StringBuilder contentBuilder = new StringBuilder();
			final StringBuilder taggedContentBuilder = new StringBuilder();

			for( final String line : lines ) {
				if( !contentMatched && !taggedContentMatched ) {
					if( line.trim().toLowerCase().contains( "content:" ) ) {
						contentMatched = true;
					} else { // match key value pairs
						final Pattern pKeyValue = Pattern.compile(
								"^([A-Z_]+):\\s(.*?)$", Pattern.MULTILINE );
						final Matcher matcher = pKeyValue.matcher( line );
						while( matcher.find() ) {
							final String key = matcher.group( 1 );
							final String value = matcher.group( 2 );

							try {
								portableModel.importFromTxt( key, value );
							} catch( final IllegalArgumentException e ) {
								Application.log( fullyQualified
										+ ": exception occured, file: "
										+ importFile + ": " + e, LogType.ERROR );
							}
						}
					}
				} else if( contentMatched && !taggedContentMatched ) {
					if( line.trim().toLowerCase().contains( "tagged_content:" ) ) {
						taggedContentMatched = true;
					} else {
						contentBuilder.append( line + "\n" );
					}
				} else { // contentMatched && taggedContentMatched
					taggedContentBuilder.append( line + "\n" );
				}
			}

			if( contentMatched ) {
				try {
					portableModel.importFromTxt( "CONTENT",
							contentBuilder.toString() );
				} catch( final IllegalArgumentException e ) {
					Application.log( fullyQualified
							+ ": exception occured, file: " + importFile + ": "
							+ e, LogType.ERROR );
				}
			} else if( !importFile.getName().equals( "boardthread.txt" )
					&& !importFile.getName().equals( "course.txt" )
					&& !importFile.getName().equals( "board.txt" ) ) {
				Application.log( "no posting content found, file: "
						+ importFile, LogType.WARNING );
			}
			if( taggedContentMatched ) {
				try {
					portableModel.importFromTxt( "TAGGED_CONTENT",
							taggedContentBuilder.toString() );
				} catch( final IllegalArgumentException e ) {
					Application.log( fullyQualified
							+ ": exception occured, file: " + importFile + ": "
							+ e, LogType.ERROR );
				}
			} else if( !importFile.getName().equals( "boardthread.txt" )
					&& !importFile.getName().equals( "course.txt" )
					&& !importFile.getName().equals( "board.txt" ) ) {
				Application.log( "no posting tagged content found, file: "
						+ importFile, LogType.WARNING );
			}
		} catch( final GeneralLoggingException e1 ) {
			// is logged already
		} catch( final IllegalArgumentException e ) {
			Application.log( fullyQualified + ": exception occured, file: "
					+ importFile + ": " + e, LogType.ERROR );
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
	public PersonNameCorpus fillFromFile( final File personCorpus,
			final PersonNameCorpus corpusInstance, final PersonNameType nameType )
			throws GeneralLoggingException {
		if( !personCorpus.exists() ) {
			throw new GeneralLoggingException( this.getClass()
					+ ":fillFromFile: the given file doesn't exist. File was: "
					+ personCorpus.getAbsolutePath(),
					"Internal error in the import module. Please read the logs." );
		}

		if( !personCorpus.isFile() ) {
			throw new GeneralLoggingException(
					this.getClass()
							+ ":fillFromFile: the given file doesn't appear to be a file. File was: "
							+ personCorpus.getAbsolutePath(),
					"Internal error in the import module. Please read the logs." );

		}

		final List< String > lines = FileUtil.readFileLineBased( personCorpus,
				this.encoding );

		int lineNumber = 0;
		for( final String line : lines ) {
			lineNumber++;
			// line must only consist of letters
			if( line.matches( "^[^\\s]+$" ) ) {
				corpusInstance.fillName( nameType, line ); // case-insensitive
			} else {
				Application
						.log( this.getClass()
								+ "fillFromFile(): illegal line in person name corpus file ("
								+ personCorpus.getAbsolutePath()
								+ "). Line number: " + lineNumber,
								LogType.ERROR );
			}
		}

		return corpusInstance;
	}

}
