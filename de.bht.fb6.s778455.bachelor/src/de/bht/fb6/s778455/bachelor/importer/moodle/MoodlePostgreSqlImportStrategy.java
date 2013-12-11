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
		this.importBoards( inputFile, courses );

		return courses.values();
	}

	/**
	 * Import the boards for the previously fetched courses.
	 * 
	 * @param inputFile
	 * @param courses
	 * @throws GeneralLoggingException
	 */
	private void importBoards( File inputFile, Map< Integer, Course > courses )
			throws GeneralLoggingException {
		final File boardDump = new File( inputFile, NAME_FORUM_FILE );

		// check if board dump exists
		if( !boardDump.exists() || !boardDump.isFile() ) {
			throw new GeneralLoggingException( getClass()
					+ "importBoards(): the given inputFile " + inputFile
					+ " is not valid! Either it doesn't exist or is no file.",
					"Internal error in the moodle postgre SQL import. Please see the logs" );
		}
		
		// parse board table and its entities
		PostgreSqlDumpParser boardParser = new PostgreSqlDumpParser(
				boardDump );
		// map of column => value
		List< Map< String, String > > resultingEntities = boardParser
				.fetchEntities( "mdl_forum", "id", "course", "name", "type",
						"intro", "timemodified" );
		
		// map entities
		this.mapBoards( courses, resultingEntities );
	}

	/**
	 * Map the resulting Board entities to our Board instances.
	 * @param courses
	 * @param resultingEntities
	 */
	private void mapBoards( Map< Integer, Course > courses,
			List< Map< String, String >> resultingEntities ) {
		for( Map< String, String > entity : resultingEntities ) {
			int courseId = 0;
			int boardId = 0;
			try {
				courseId = Integer.parseInt( entity.get( "course" ) );
				boardId = Integer.parseInt( entity.get( "id" ) );
			}
			catch (NumberFormatException e) {
				// handled below
			}
			
			// log if the board is not associated to a course or doesn't have an id
			if (0 == courseId || 0 == boardId) {
				Application
				.log( getClass()
						+ "mapBoards: the board entity doesn't have an id or course id. Given dump file: "
						+ NAME_FORUM_FILE, LogType.ERROR );
			}
			else { // otherwise, get belonging course instance and fill the board
				Course course = courses.get( courseId );
				if (null == course) { // no course instance -> some internal error
					Application
					.log( getClass()
							+ "mapBoards: the course instance to the given board doesn't exist. Is the dump corrupted or aren't the dumps asynchronous?"
							+ NAME_FORUM_FILE, LogType.ERROR );
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
					+ "importCourses(): the given inputFile " + inputFile
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
