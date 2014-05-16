/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

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
	private static final String NAME_USER_ENROLE_FILE = "mdl_user_enrolments.sql";
	private static final String NAME_ENROLE_FILE = "mdl_enrol.sql";
	/**
	 * Configuration of person corpus import true | false | fallback
	 */
	private final String personOption;
	/**
	 * Saved courses of the last import. A map of id => course instance.
	 */
	private Map< Integer, Course > savedCourses;

	/**
	 * Create and prepare a new instance.
	 */
	public MoodlePostgreSqlImportStrategy() {
		this.personOption = ServiceFactory
				.getConfigReader()
				.fetchValue(
						IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC )
				.trim().toLowerCase();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream
	 * (java.io.InputStream)
	 */
	public Set< Course > importBoardFromStream( final InputStream inputStream ) {
		throw new UnsupportedOperationException( this.getClass()
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
	public LmsCourseSet importBoardFromFile( final File inputFile )
			throws GeneralLoggingException {
		// check file first
		if( !this.checkInputFile( inputFile ) ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importBoardFromFile(): the given inputFile " + inputFile
					+ " is not valid!",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// set course set's name from input file
		final LmsCourseSet dumpCourseSet = new LmsCourseSet( super.removeIllegalChars( inputFile.getName() ));
		
		// import courses
		final Map< Integer, Course > courses = this.importCourses( inputFile, dumpCourseSet );
		final Map< Integer, Board > boards = this.importBoards( inputFile, courses );
		final Map< Integer, BoardThread > threads = this.importBoardThreads(
				inputFile, boards );
		this.importPostings( inputFile, threads );

		this.savedCourses = courses;
		// import person
		this.fillFromFile( inputFile, null, null );
		
		dumpCourseSet.addAll( courses.values() );
		return dumpCourseSet;
	}

	/**
	 * Import the {@link Posting} entities for the previously fetched
	 * {@link BoardThread} instances.
	 * 
	 * @param inputFile
	 * @param threads
	 * @throws GeneralLoggingException
	 */
	private void importPostings( final File inputFile,
			final Map< Integer, BoardThread > threads )
			throws GeneralLoggingException {

		final File postingsDump = new File( inputFile, NAME_POSTINGS_FILE );

		// check if thread dump exists
		if( !postingsDump.exists() || !postingsDump.isFile() ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importPostings(): the given inputFile " + postingsDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse postings and its entities
		final PostgreSqlDumpParser postingsParser = new PostgreSqlDumpParser(
				postingsDump );
		// map of column => value
		final List< Map< String, String > > resultingEntities = postingsParser
				.fetchEntities( "mdl_forum_posts", "id", "discussion",
						"parent", "created", "modified", "subject", "message" );

		// map entities
		this.mapPostings( threads, resultingEntities );
	}

	/**
	 * Map the fetched entities to {@link Posting} instances
	 * 
	 * @param threads
	 * @param resultingEntities
	 */
	private void mapPostings( final Map< Integer, BoardThread > threads,
			final List< Map< String, String >> resultingEntities ) {
		for( final Map< String, String > entity : resultingEntities ) {
			int postingId = 0;
			int threadId = 0;
			try {
				postingId = Integer.parseInt( entity.get( "id" ) );
				threadId = Integer.parseInt( entity.get( "discussion" ) );
			} catch( final NumberFormatException e ) {
				// handled below
			}

			// log if the thread is not associated to a board or doesn't have an
			// id
			if( 0 == postingId || 0 == threadId ) {
				Application
						.log( this.getClass()
								+ "mapPostings: the posting entity doesn't have an id or thread id. Given dump file: "
								+ NAME_POSTINGS_FILE, LogType.ERROR );
			} else { // otherwise, get belonging thread instance and fill
				final BoardThread thread = threads.get( threadId );
				if( null == thread ) {
					Application
							.log( this.getClass()
									+ "mapBoardThreads: the BoardThread instance to the given posting doesn't exist. Is the dump corrupted or aren't the dumps asynchronous?"
									+ NAME_POSTINGS_FILE, LogType.ERROR );
				} else {
					final Posting newPosting = new Posting( thread );
					thread.addPosting( newPosting );

					// parent posting id
					try {
						final int parentPostingId = Integer.parseInt( entity
								.get( "parent" ) );
						newPosting.setParentPostingId( parentPostingId );
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + "mapPostings: " + e,
								LogType.ERROR );
					}

					// title
					final String title = entity.get( "subject" );
					if( null != title ) {
						newPosting.setTitle( title );
					}

					// message
					final String message = entity.get( "message" );
					if( null != message ) {
						newPosting.setContent( message );
					} else {
						Application
								.log( this.getClass()
										+ ":mapPostings(): Posting with empty content created! Posting: "
										+ newPosting, LogType.ERROR );
					}

					// creation date
					try {
						final long creationTime = Long.parseLong( entity
								.get( "created" ) );
						if( 0 != creationTime ) {
							newPosting
									.setCreationDate( new Date( creationTime ) );
						}
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + ":mapPostings(): " + e,
								LogType.ERROR );
					}

					// modification date
					try {
						final long modifcationTime = Long.parseLong( entity
								.get( "modified" ) );
						if( 0 != modifcationTime ) {
							newPosting.setModificationDate( new Date(
									modifcationTime ) );
						}
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + ":mapPostings(): " + e,
								LogType.ERROR );
					}
				}
			}
		}
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
	private Map< Integer, BoardThread > importBoardThreads( final File inputFile,
			final Map< Integer, Board > boards ) throws GeneralLoggingException {
		final Map< Integer, BoardThread > returnBoardThreads = new HashMap< Integer, BoardThread >();
		final File threadDump = new File( inputFile, NAME_THREAD_FILE );

		// check if thread dump exists
		if( !threadDump.exists() || !threadDump.isFile() ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importBoardThreads(): the given inputFile " + threadDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse board table and its entities
		final PostgreSqlDumpParser threadParser = new PostgreSqlDumpParser(
				threadDump );
		// map of column => value
		final List< Map< String, String > > resultingEntities = threadParser
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
	private void mapBoardThreads( final Map< Integer, Board > boards,
			final List< Map< String, String >> resultingEntities,
			final Map< Integer, BoardThread > returnBoardThreads ) {
		for( final Map< String, String > entity : resultingEntities ) {
			// fetch associated forum id and id first, log on fail
			int threadId = 0;
			int boardId = 0;
			try {
				threadId = Integer.parseInt( entity.get( "id" ) );
				boardId = Integer.parseInt( entity.get( "forum" ) );
			} catch( final NumberFormatException e ) {
				// handled below
			}

			// log if the thread is not associated to a board or doesn't have an
			// id
			if( 0 == boardId || 0 == threadId ) {
				Application
						.log( this.getClass()
								+ "mapBoardThreads: the board entity doesn't have an id or course id. Given dump file: "
								+ NAME_THREAD_FILE, LogType.ERROR );
			} else { // otherwise, get belonging board instance and fill the
						// thread
				// fetch board instance
				final Board belongingBoard = boards.get( boardId );
				if( null == belongingBoard ) {
					Application
							.log( this.getClass()
									+ "mapBoardThreads: the board instance to the given board thread doesn't exist. Is the dump corrupted or aren't the dumps asynchronous?"
									+ NAME_FORUM_FILE, LogType.ERROR );
				} else {
					// "id", "forum", "name", "firstpost","timemodified",
					// "timestart", "timeend"
					final BoardThread newThread = new BoardThread( belongingBoard );
					newThread.setId( threadId );

					// add to belonging board
					belongingBoard.addThread( newThread );

					// add to id - board thread map for internal use
					returnBoardThreads.put( threadId, newThread );

					// get name
					final String name = entity.get( "name" );
					if( null != name ) {
						newThread.setTitle( name );
					}

					// first posting id
					try {
						final int postingId = Integer.parseInt( entity
								.get( "firstpost" ) );
						newThread.setFirstPostingId( postingId );

					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}

					// modification date
					try {
						final long modifcationTime = Long.parseLong( entity
								.get( "timemodified" ) );
						if( 0 != modifcationTime ) {
							newThread.setModificationDate( new Date(
									modifcationTime ) );
						}
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}

					// time start date
					try {
						final long startTime = Long.parseLong( entity
								.get( "timestart" ) );
						if( 0 != startTime ) {
							newThread.setCreationDate( new Date( startTime ) );
						}
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + "mapBoardThreads: " + e,
								LogType.ERROR );
					}

					// time end date
					try {
						final long endTime = Long.parseLong( entity.get( "timeend" ) );
						if( 0 != endTime ) {
							newThread.setEndDate( new Date( endTime ) );
						}
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + "mapBoardThreads: " + e,
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
	private Map< Integer, Board > importBoards( final File inputFile,
			final Map< Integer, Course > courses ) throws GeneralLoggingException {
		final Map< Integer, Board > returnBoards = new HashMap< Integer, Board >();
		final File boardDump = new File( inputFile, NAME_FORUM_FILE );

		// check if board dump exists
		if( !boardDump.exists() || !boardDump.isFile() ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importBoards(): the given inputFile " + boardDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse board table and its entities
		final PostgreSqlDumpParser boardParser = new PostgreSqlDumpParser( boardDump );
		// map of column => value
		final List< Map< String, String > > resultingEntities = boardParser
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
	private void mapBoards( final Map< Integer, Course > courses,
			final List< Map< String, String >> resultingEntities,
			final Map< Integer, Board > returnBoards ) {
		for( final Map< String, String > entity : resultingEntities ) {
			int courseId = 0;
			int boardId = 0;
			try {
				courseId = Integer.parseInt( entity.get( "course" ) );
				boardId = Integer.parseInt( entity.get( "id" ) );
			} catch( final NumberFormatException e ) {
				// handled below
			}

			// log if the board is not associated to a course or doesn't have an
			// id
			if( 0 == courseId || 0 == boardId ) {
				Application
						.log( this.getClass()
								+ "mapBoards: the board entity doesn't have an id or course id. Given dump file: "
								+ NAME_FORUM_FILE, LogType.ERROR );
			} else { // otherwise, get belonging course instance and fill the
						// board
				final Course course = courses.get( courseId );
				if( null == course ) { // no course instance -> some internal
										// error
					Application
							.log( this.getClass()
									+ "mapBoards: the course instance to the given board doesn't exist. Is the dump corrupted or aren't the dumps asynchronous?"
									+ NAME_FORUM_FILE, LogType.ERROR );
				} else {
					final Board newBoard = new Board( course );
					newBoard.setId( boardId );

					course.addBoard( newBoard );
					returnBoards.put( boardId, newBoard );

					// fill board
					// "id", "course", "name", "type", "intro", "timemodified"
					// );

					// board title
					final String name = entity.get( "name" );
					if( null != name ) {
						newBoard.setTitle( name );
					}

					// board type
					final String type = entity.get( "type" );
					if( null != type ) {
						newBoard.setType( type );
					}

					// intro text
					final String intro = entity.get( "intro" );
					if( null != intro ) {
						newBoard.setIntro( intro );
					}

					// set modification date
					try {
						final long modifcationTime = Long.parseLong( entity
								.get( "timemodified" ) );
						if( 0 != modifcationTime ) {
							newBoard.setModificationDate( new Date(
									modifcationTime ) );
						}
					} catch( final NumberFormatException e ) {
						Application.log( this.getClass() + "mapBoards: " + e,
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
	 * @param dumpCourseSet 
	 * @return a {@link Set} of {@link Course} instance.
	 * @throws GeneralLoggingException
	 *             if the dump is invalid
	 */
	private Map< Integer, Course > importCourses( final File inputFile, final LmsCourseSet dumpCourseSet )
			throws GeneralLoggingException {
		final Map< Integer, Course > returnCourses = new HashMap< Integer, Course >();
		final File courseDump = new File( inputFile, NAME_COURSE_FILE );

		// check if course dump exists
		if( !courseDump.exists() || !courseDump.isFile() ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importCourses(): the given inputFile " + courseDump
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}

		// parse table and its entities
		final PostgreSqlDumpParser courseParser = new PostgreSqlDumpParser(
				courseDump );
		// map of column => value
		final List< Map< String, String > > resultingEntities = courseParser
				.fetchEntities( "mdl_course", "id", "fullname", "shortname",
						"summary", "lang", "timecreated", "timemodified" );

		// map entities
		this.mapCourse( returnCourses, resultingEntities, dumpCourseSet );

		return returnCourses;
	}

	/**
	 * Map the resulting entities to the given set of {@link Course} instances.
	 * 
	 * @param returnCourses
	 * @param resultingEntities
	 * @param dumpCourseSet 
	 */
	private void mapCourse( final Map< Integer, Course > returnCourses,
			final List< Map< String, String >> resultingEntities, final LmsCourseSet dumpCourseSet ) {

		for( final Map< String, String > entity : resultingEntities ) {
			final String fullname = entity.get( "fullname" );

			int id = 0;
			// fetch id
			try {
				id = Integer.parseInt( entity.get( "id" ) );
			} catch( final NumberFormatException e ) {
				// will be handled below
			}

			if( null == fullname || 0 == id ) {
				Application
						.log( this.getClass()
								+ "mapCourse: the course entity doesn't have a fullname or id. Given dump file: "
								+ NAME_COURSE_FILE, LogType.ERROR );
			} else { // course name given so the course has an identity
				final Course course = new Course( fullname, dumpCourseSet );
				course.setTitle( fullname );
				course.setId( id );

				// set shortname
				final String shortname = entity.get( "shortname" );
				if( null != shortname ) {
					course.setShortName( shortname );
				}

				// set summary
				final String summary = entity.get( "summary" );
				if( null != summary ) {
					course.setSummary( summary );
				}

				// set lang
				final String lang = entity.get( "lang" );
				if( null != lang ) {
					course.setLang( lang );
				}

				// set creation date
				try {
					final long creationTime = Long.parseLong( entity
							.get( "timecreated" ) );
					course.setCreationDate( new Date( creationTime ) );
				} catch( final NumberFormatException e ) {
					Application.log( this.getClass() + "mapCourse: " + e,
							LogType.ERROR );
				}

				// set modification date
				try {
					final long modifcationTime = Long.parseLong( entity
							.get( "timemodified" ) );
					if( 0 != modifcationTime ) {
						course.setModificationDate( new Date( modifcationTime ) );
					}
				} catch( final NumberFormatException e ) {
					Application.log( this.getClass() + "mapCourse: " + e,
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
	private boolean checkInputFile( final File inputFile ) {
		if( !inputFile.exists() || !inputFile.isDirectory() ) {
			return false;
		}
		return true;
	}

	@Override
	public PersonNameCorpus fillFromFile( final File personCorpus,
			final PersonNameCorpus corpusInstance, final PersonNameType nameType )
			throws GeneralLoggingException {
		// fallback or true -> only read the course names course-based
		if( this.personOption.equals( "fallback" )
				|| this.personOption.equals( "true" ) ) {
			// parse mdl_enrol.sql
			final File userEnrolFile = new File( personCorpus, NAME_ENROLE_FILE );
			if( !userEnrolFile.exists() || !userEnrolFile.isFile() ) {
				Application
						.log( this.getClass()
								+ "fillFromFile(): the given file doesn't exist or is not a file: "
								+ userEnrolFile, LogType.ERROR );
				return null;
			}
			final PostgreSqlDumpParser userEnrolParser = new PostgreSqlDumpParser(
					userEnrolFile );
			final List< Map< String, String > > resultingEnrolEntities = userEnrolParser
					.fetchEntities( "mdl_enrol", "id", "courseid" );

			// filter courses
			this.filterCourses( resultingEnrolEntities );

			// parse mdl_user_enrolments.sql
			final File userEnrolmentFile = new File( personCorpus,
					NAME_USER_ENROLE_FILE );
			if( !userEnrolmentFile.exists() || !userEnrolmentFile.isFile() ) {
				Application
						.log( this.getClass()
								+ "fillFromFile(): the given file doesn't exist or is not a file: "
								+ userEnrolmentFile, LogType.ERROR );
				return null;
			}
			final PostgreSqlDumpParser userEnrolmentParser = new PostgreSqlDumpParser(
					userEnrolmentFile );
			final List< Map< String, String > > resultingUserEnrolmentEntities = userEnrolmentParser
					.fetchEntities( "mdl_user_enrolments", "enrolid", "userid" );

			// parse mdl_user.sql
			final File userFile = new File( personCorpus, NAME_USERS_FILE );
			if( !userFile.exists() || !userFile.isFile() ) {
				Application
						.log( this.getClass()
								+ "fillFromFile(): the given file doesn't exist or is not a file: "
								+ userFile, LogType.ERROR );
				return null;
			}
			final PostgreSqlDumpParser userParser = new PostgreSqlDumpParser(
					userFile );
			final List< Map< String, String > > resultingUserEntities = userParser
					.fetchEntities( "mdl_user", "id", "deleted", "firstname",
							"lastname" );
			this.addPersonCorpora( resultingEnrolEntities,
					resultingUserEnrolmentEntities, resultingUserEntities );
		} else { // make global corpus only on user entities
			// parse mdl_user.sql
			final File userFile = new File( personCorpus, NAME_USERS_FILE );
			if( !userFile.exists() || !userFile.isFile() ) {
				Application
						.log( this.getClass()
								+ "fillFromFile(): the given file doesn't exist or is not a file: "
								+ userFile, LogType.ERROR );
				return null;
			}
			final PostgreSqlDumpParser userParser = new PostgreSqlDumpParser(
					userFile );
			final List< Map< String, String > > resultingUserEntities = userParser
					.fetchEntities( "mdl_user", "id", "deleted", "firstname",
							"lastname" );
			this.fillPersonSingleton( resultingUserEntities );

		}
		return null;
	}

	/**
	 * Fill the singleton corpus.
	 * @param resultingUserEntities
	 */
	private void fillPersonSingleton(
			final List< Map< String, String >> resultingUserEntities ) {
		final PersonNameCorpus singleton = ServiceFactory
				.getPersonNameCorpusSingleton();

		for( final Map< String, String > userEntity : resultingUserEntities ) {
			int userId = 0;
			try {
				userId = Integer.parseInt( userEntity.get( "id" ) );
			} catch( final NumberFormatException e ) {
				// below
			}

			if( 0 == userId ) {
				Application
						.log( this.getClass()
								+ "addPersonCorpora(): corrupt dump. userid of 'mdl_user' couldn't be parsed. Given file: "
								+ NAME_USER_ENROLE_FILE, LogType.ERROR );
			} else {
				final String prename = userEntity.get( "firstname" );
				if( null != prename && prename.trim().length() != 0 ) {
					singleton.fillPrename( prename
							 );
				}
				final String lastname = userEntity.get( "lastname" );
				if( null != lastname  && lastname.trim().length() != 0 ) {
					singleton.fillLastname( lastname
						);
				}

			}
		}
		
		// set singleton corpus on each course
		for( final Course course : this.savedCourses.values()) {
			course.setPersonNameCorpus( singleton );
		}

	}

	/**
	 * Add the given entities to the correct corpus.
	 * 
	 * @TODO add special "fallback" support
	 * 
	 * @param resultingEnrolEntities
	 * @param resultingUserEnrolmentEntities
	 * @param resultingUserEntities
	 */
	private void addPersonCorpora(
			final List< Map< String, String >> resultingEnrolEntities,
			final List< Map< String, String >> resultingUserEnrolmentEntities,
			final List< Map< String, String >> resultingUserEntities ) {
		for( final Map< String, String > enrolEntity : resultingEnrolEntities ) {
			int courseId = 0;
			int enrolId = 0;
			try {
				courseId = Integer.parseInt( enrolEntity.get( "courseid" ) );
				enrolId = Integer.parseInt( enrolEntity.get( "id" ) );
			} catch( final NumberFormatException e ) {
				// handled below
			}

			if( 0 == courseId || 0 == enrolId ) {
				Application
						.log( this.getClass()
								+ "addPersonCorpora(): corrupt dump. courseId or id of 'mdl_enrol' couldn't be parsed. Given file: "
								+ NAME_USER_ENROLE_FILE, LogType.ERROR );
			} else {
				// look for user ids depending on the course id
				for( final Map< String, String > userEnrolmentEntity : resultingUserEnrolmentEntities ) {
					int uEnrolId = 0;
					try {
						uEnrolId = Integer.parseInt( userEnrolmentEntity
								.get( "enrolid" ) );

					} catch( final NumberFormatException e ) {
						// handled below
					}

					if( 0 == uEnrolId ) {
						Application
								.log( this.getClass()
										+ "addPersonCorpora(): corrupt dump. enrolid couldn't be parsed. Given file: "
										+ NAME_USER_ENROLE_FILE, LogType.ERROR );
					} else {
						int uUserId = 0;
						try {
							uUserId = Integer.parseInt( userEnrolmentEntity
									.get( "userid" ) );
						} catch( final NumberFormatException e ) {
							// handled below
						}

						if( 0 == uUserId ) {
							Application
									.log( this.getClass()
											+ "addPersonCorpora(): corrupt dump. userid couldn't be parsed. Given file: "
											+ NAME_USER_ENROLE_FILE,
											LogType.ERROR );
						} else {
							for( final Map< String, String > userEntity : resultingUserEntities ) {
								int userId = 0;
								try {
									userId = Integer.parseInt( userEntity
											.get( "id" ) );
								} catch( final NumberFormatException e ) {
									// below
								}

								if( 0 == userId ) {
									Application
											.log( this.getClass()
													+ "addPersonCorpora(): corrupt dump. userid of 'mdl_user' couldn't be parsed. Given file: "
													+ NAME_USER_ENROLE_FILE,
													LogType.ERROR );
								} else {
									if( userId == uUserId ) { // mdl_user_enrolment
																// entity linked
																// to user
																// entity
										final Course enroledCourse = this.savedCourses
												.get( courseId );
										final String prename = userEntity
												.get( "firstname" );
										
										// create bare person corpus
										enroledCourse.setPersonNameCorpus( new PersonNameCorpus() );
										if( null != prename ) {
											enroledCourse.getPersonNameCorpus()
													.fillPrename( prename);
										}
										final String lastname = userEntity
												.get( "lastname" );
										if( null != lastname ) {
											enroledCourse.getPersonNameCorpus()
													.fillLastname( lastname);
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Filter courses which are not stored in the field storedCourses.
	 * 
	 * @param resultingEntities
	 */
	private void filterCourses( final List< Map< String, String >> resultingEntities ) {
		final Iterator< Map< String, String > > it = resultingEntities.iterator();
		while( it.hasNext() ) {
			final Map< String, String > entity = it.next();
			int courseId = 0;
			try {
				courseId = Integer.parseInt( entity.get( "courseid" ) );
			} catch( final NumberFormatException e ) {
				// handled below
			}

			if( 0 == courseId ) {
				Application
						.log( this.getClass()
								+ "filterCourses(): corrupt dump. courseId couldn't be parsed. Given file: "
								+ NAME_USER_ENROLE_FILE, LogType.ERROR );
			} else {
				if( !this.savedCourses.containsKey( courseId ) ) {
					resultingEntities.remove( entity );
				}
			}
		}

	}
}
