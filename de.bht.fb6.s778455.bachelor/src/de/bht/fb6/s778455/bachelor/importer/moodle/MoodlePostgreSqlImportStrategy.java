/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>
 * This class implements the functionality to import raw dump files from moodle
 * our mini world.
 * </p>
 * <p>
 * This strategy is designed to work with PostgreSQL dumps. It was tested
 * against the following pg_dump command: <br />
 * pg_dump -c -h localhost -p 5432 -U postgres -t mdl_course moodle
 * </p>
 * <p>
 * You can configure the dumps' folder in the importer.properties configuration
 * file.
 * </p>
 * <p>
 * Make sure that the folder contains the following files:<br />
 * <ul>
 * <li>mdl_course.sql</li>
 * <li>mdl_forum.sql</li>
 * <li>mdl_forum_discussions.sql</li>
 * <li>mdl_forum_posts.sql</li>
 * <li>mdl_user.sql</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class MoodlePostgreSqlImportStrategy extends AImportStrategy {
	/**
	 * Full name (including extension) of the dump file containing the course
	 * table.
	 */
	public static final String NAME_COURSE_FILE = "mdl_course.sql";
	/**
	 * Full name (including extension) of the dump file containing the course
	 * table.
	 */
	public static final String NAME_FORUM_FILE = "mdl_forum.sql";
	/**
	 * Full name (including extension) of the dump file containing the course
	 * table.
	 */
	public static final String NAME_THREAD_FILE = "mdl_forum_discussions.sql";
	/**
	 * Full name (including extension) of the dump file containing the course
	 * table.
	 */
	public static final String NAME_POSTINGS_FILE = "mdl_forum_posts.sql";
	/**
	 * Full name (including extension) of the dump file containing the course
	 * table.
	 */
	public static final String NAME_USERS_FILE = "mdl_user.sql";

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream
	 * (java.io.InputStream)
	 */
	public Set< Course > importBoardFromStream( final InputStream inputStream ) {
		throw new UnsupportedOperationException( getClass()
				+ "importBoardFromStream() is not supported yet." );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile
	 * (java.io.File)
	 */
	public Collection< Course > importBoardFromFile( final File inputFile )
			throws GeneralLoggingException {
		// check file first
		if( !this.checkInputFile( inputFile ) ) {
			throw new GeneralLoggingException( getClass()
					+ "importBoardFromFile(): the given inputFile " + inputFile
					+ " is not valid!",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// import courses
		Map< Integer, Course > courses = this.importCourses( inputFile );
		Map< Integer, Board > boards = this.importBoards( inputFile, courses );
		Map< Integer, BoardThread > threads = this.importBoardThreads(
				inputFile, boards );

		return courses.values();
	}

	/**
	 * Import the board thread entities for the previously fetched {@link Board}
	 * instances.
	 * 
	 * @param inputFile
	 * @param boards
	 *            the map of id - {@link Board} instance
	 * @return the ID - boardthread map for internal use in further mappings
	 * @throws GeneralLoggingException
	 */
	private Map< Integer, BoardThread > importBoardThreads( File inputFile,
			Map< Integer, Board > boards ) throws GeneralLoggingException {
		Map< Integer, BoardThread > returnBoardThreads = new HashMap< Integer, BoardThread >();
		final File threadDump = new File( inputFile, NAME_THREAD_FILE );

		// check if thread dump exists
		if( !threadDump.exists() || !threadDump.isFile() ) {
			throw new GeneralLoggingException( getClass()
					+ "importBoardThreads(): the given inputFile " + threadDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse board table and its entities
		PostgreSqlDumpParser threadParser = new PostgreSqlDumpParser(
				threadDump );
		// map of column => value
		List< Map< String, String > > resultingEntities = threadParser
				.fetchEntities( "mdl_forum_discussions", "id", "forum", "name",
						"firstpost", "timemodified", "timestart", "timeend" );

		// map entities
		this.mapBoardThreads( boards, resultingEntities, returnBoardThreads );
		return returnBoardThreads;
	}

	/**
	 * Map the fecthed BoardThread entities to {@link BoardThread} instances
	 * which are child of a previously filled {@link Board} instance.
	 * 
	 * @param boards
	 * @param resultingEntities
	 * @param returnBoardThreads
	 */
	private void mapBoardThreads( Map< Integer, Board > boards,
			List< Map< String, String >> resultingEntities,
			Map< Integer, BoardThread > returnBoardThreads ) {
		for( Map< String, String > entity : resultingEntities ) {
			// fetch associated forum id and id first, log on fail
			int threadId = 0;
			int boardId = 0;
			try {
				threadId = Integer.parseInt( entity.get( "id" ) );
				boardId = Integer.parseInt( entity.get( "forum" ) );
			} catch( NumberFormatException e ) {
				// handled below
			}

			// log if the thread is not associated to a board or doesn't have an
			// id
			if( 0 == boardId || 0 == threadId ) {
				Application
						.log( getClass()
								+ "mapBoardThreads: the board entity doesn't have an id or course id. Given dump file: "
								+ NAME_THREAD_FILE, LogType.ERROR );
			} else { // otherwise, get belonging board instance and fill the
						// thread
				// fetch board instance
				Board belongingBoard = boards.get( boardId );
				if( null == belongingBoard ) {
					Application
							.log( getClass()
									+ "mapBoardThreads: the board instance to the given board thread doesn't exist. Is the dump corrupted or aren't the dumps asynchronous?"
									+ NAME_FORUM_FILE, LogType.ERROR );
				} else {
					// "id", "forum", "name", "firstpost","timemodified",
					// "timestart", "timeend"
					BoardThread newThread = new BoardThread( belongingBoard );
					newThread.setId( threadId );
					
					// add to belonging board
					belongingBoard.addThread( newThread );
					
					// add to id - board thread map for internal use
					returnBoardThreads.put( threadId, newThread );

					// get name
					String name = entity.get( "name" );
					if( null != name ) {
						newThread.setTitle( name );
					}

					// first posting id
					try {
						int postingId = Integer.parseInt( entity
								.get( "firstpost" ) );
						newThread.setFirstPostingId( postingId );

					} catch( NumberFormatException e ) {
						Application.log( getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}

					// modification date
					try {
						long modifcationTime = Long.parseLong( entity
								.get( "timemodified" ) );
						if( 0 != modifcationTime ) {
							newThread.setModificationDate( new Date(
									modifcationTime ) );
						}
					} catch( NumberFormatException e ) {
						Application.log( getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}
					
					// time start date
					try {
						long startTime = Long.parseLong( entity
								.get( "timestart" ) );
						if( 0 != startTime ) {
							newThread.setCreationDate( new Date(
									startTime ) );
						}
					} catch( NumberFormatException e ) {
						Application.log( getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}
					
					// time end date
					try {
						long endTime = Long.parseLong( entity
								.get( "timeend" ) );
						if( 0 != endTime ) {
							newThread.setEndDate( new Date(
									endTime ) );
						}
					} catch( NumberFormatException e ) {
						Application.log( getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}
				}
			}
		}
	}

	/**
	 * Import the boards for the previously fetched courses.
	 * 
	 * @param inputFile
	 * @param courses
	 * @throws GeneralLoggingException
	 * @return the ID - board map for internal use in further mappings
	 */
	private Map< Integer, Board > importBoards( File inputFile,
			Map< Integer, Course > courses ) throws GeneralLoggingException {
		Map< Integer, Board > returnBoards = new HashMap< Integer, Board >();
		final File boardDump = new File( inputFile, NAME_FORUM_FILE );

		// check if board dump exists
		if( !boardDump.exists() || !boardDump.isFile() ) {
			throw new GeneralLoggingException( getClass()
					+ "importBoards(): the given inputFile " + boardDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse board table and its entities
		PostgreSqlDumpParser boardParser = new PostgreSqlDumpParser( boardDump );
		// map of column => value
		List< Map< String, String > > resultingEntities = boardParser
				.fetchEntities( "mdl_forum", "id", "course", "name", "type",
						"intro", "timemodified" );

		// map entities
		this.mapBoards( courses, resultingEntities, returnBoards );

		return returnBoards;
	}

	/**
	 * Map the resulting Board entities to our Board instances. If the board is
	 * not associated to any course or doesn't have an ID, it will be ignored.
	 * 
	 * @param courses
	 * @param resultingEntities
	 * @param returnBoards
	 *            the ID - board map for internal use in further mappings
	 */
	private void mapBoards( Map< Integer, Course > courses,
			List< Map< String, String >> resultingEntities,
			Map< Integer, Board > returnBoards ) {
		for( Map< String, String > entity : resultingEntities ) {
			int courseId = 0;
			int boardId = 0;
			try {
				courseId = Integer.parseInt( entity.get( "course" ) );
				boardId = Integer.parseInt( entity.get( "id" ) );
			} catch( NumberFormatException e ) {
				// handled below
			}

			// log if the board is not associated to a course or doesn't have an
			// id
			if( 0 == courseId || 0 == boardId ) {
				Application
						.log( getClass()
								+ "mapBoards: the board entity doesn't have an id or course id. Given dump file: "
								+ NAME_FORUM_FILE, LogType.ERROR );
			} else { // otherwise, get belonging course instance and fill the
						// board
				Course course = courses.get( courseId );
				if( null == course ) { // no course instance -> some internal
										// error
					Application
							.log( getClass()
									+ "mapBoards: the course instance to the given board doesn't exist. Is the dump corrupted or aren't the dumps asynchronous?"
									+ NAME_FORUM_FILE, LogType.ERROR );
				} else {
					Board newBoard = new Board( course );
					newBoard.setId( boardId );
					
					course.addBoard( newBoard );
					returnBoards.put( boardId, newBoard );

					// fill board
					// "id", "course", "name", "type", "intro", "timemodified"
					// );

					// board title
					String name = entity.get( "name" );
					if( null != name ) {
						newBoard.setTitle( name );
					}

					// board type
					String type = entity.get( "type" );
					if( null != type ) {
						newBoard.setType( type );
					}

					// intro text
					String intro = entity.get( "intro" );
					if( null != intro ) {
						newBoard.setIntro( intro );
					}

					// set modification date
					try {
						long modifcationTime = Long.parseLong( entity
								.get( "timemodified" ) );
						if( 0 != modifcationTime ) {
							newBoard.setModificationDate( new Date(
									modifcationTime ) );
						}
					} catch( NumberFormatException e ) {
						Application.log( getClass() + "mapBoards: " + e,
								LogType.ERROR );
					}
				}

			}
		}
	}

	/**
	 * Import the courses from the sql dump.
	 * 
	 * @param inputFile
	 * @return a {@link Set} of {@link Course} instance.
	 * @throws GeneralLoggingException
	 *             if the dump is invalid
	 */
	private Map< Integer, Course > importCourses( final File inputFile )
			throws GeneralLoggingException {
		Map< Integer, Course > returnCourses = new HashMap< Integer, Course >();
		final File courseDump = new File( inputFile, NAME_COURSE_FILE );

		// check if course dump exists
		if( !courseDump.exists() || !courseDump.isFile() ) {
			throw new GeneralLoggingException( getClass()
					+ "importCourses(): the given inputFile " + courseDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse table and its entities
		PostgreSqlDumpParser courseParser = new PostgreSqlDumpParser(
				courseDump );
		// map of column => value
		List< Map< String, String > > resultingEntities = courseParser
				.fetchEntities( "mdl_course", "id", "fullname", "shortname",
						"summary", "lang", "timecreated", "timemodified" );

		// map entities
		this.mapCourse( returnCourses, resultingEntities );

		return returnCourses;
	}

	/**
	 * Map the resulting entities to the given set of {@link Course} instances.
	 * 
	 * @param returnCourses
	 * @param resultingEntities
	 */
	private void mapCourse( Map< Integer, Course > returnCourses,
			List< Map< String, String >> resultingEntities ) {

		for( Map< String, String > entity : resultingEntities ) {
			String fullname = entity.get( "fullname" );

			int id = 0;
			// fetch id
			try {
				id = Integer.parseInt( entity.get( "id" ) );
			} catch( NumberFormatException e ) {
				// will be handled below
			}

			if( null == fullname || 0 == id ) {
				Application
						.log( getClass()
								+ "mapCourse: the course entity doesn't have a fullname or id. Given dump file: "
								+ NAME_COURSE_FILE, LogType.ERROR );
			} else { // course name given so the course has an identity
				Course course = new Course( fullname );
				course.setTitle( fullname );
				course.setId( id );

				// set shortname
				String shortname = entity.get( "shortname" );
				if( null != shortname ) {
					course.setShortName( shortname );
				}

				// set summary
				String summary = entity.get( "summary" );
				if( null != summary ) {
					course.setSummary( summary );
				}

				// set lang
				String lang = entity.get( "lang" );
				if( null != lang ) {
					course.setLang( lang );
				}

				// set creation date
				try {
					long creationTime = Long.parseLong( entity
							.get( "timecreated" ) );
					course.setCreationDate( new Date( creationTime ) );
				} catch( NumberFormatException e ) {
					Application.log( getClass() + "mapCourse: " + e,
							LogType.ERROR );
				}

				// set modification date
				try {
					long modifcationTime = Long.parseLong( entity
							.get( "timemodified" ) );
					if( 0 != modifcationTime ) {
						course.setModificationDate( new Date( modifcationTime ) );
					}
				} catch( NumberFormatException e ) {
					Application.log( getClass() + "mapCourse: " + e,
							LogType.ERROR );
				}

				// finally add the course to be returned
				returnCourses.put( id, course );
			}
		}
	}

	/**
	 * Check the validity of the given inputFile.
	 * 
	 * @param inputFile
	 * @return
	 */
	private boolean checkInputFile( File inputFile ) {
		if( !inputFile.exists() || !inputFile.isDirectory() ) {
			return false;
		}
		return true;
	}

	@Override
	public PersonNameCorpus fillFromFile( File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}
}
