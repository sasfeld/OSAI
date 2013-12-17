/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.auditorium;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>
 * Instances of this class connect to a configured database and create concrete
 * models which are used by the {@link AuditoriumImportStrategy}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 * 
 */
public class AuditoriumDbQuerier {
	/**
	 * Fully qualified name of the MySQL driver class.
	 */
	public static final String NAME_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	protected java.sql.Connection connection;
	private String database;

	/**
	 * Initialize a new instance. The database connectivity will be set up.
	 * 
	 * @throws GeneralLoggingException
	 */
	public AuditoriumDbQuerier() throws GeneralLoggingException {
		final String host = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_AUDITORIUM_DB_HOST );
		final String user = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_AUDITORIUM_DB_USER );
		final String password = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.IMPORT_STRATEGY_AUDITORIUM_DB_PW );
		this.initializeConnection( host, user, password );
	}

	/**
	 * Initialize the connection to the database server first.
	 * 
	 * @throws GeneralLoggingException
	 */
	private void initializeConnection( final String url, final String user,
			final String password ) throws GeneralLoggingException {
		try {
			Class.forName( NAME_MYSQL_DRIVER );
			this.setConnection( DriverManager.getConnection( url, user,
					password ) );
			this.setDatabase( ServiceFactory.getConfigReader().fetchValue(
					IConfigKeys.IMPORT_STRATEGY_AUDITORIUM_DB_DBNAME ) );
		} catch( SQLException e ) { // no connection
			throw new GeneralLoggingException(
					getClass()
							+ ":initializeConnection(): couldn't open connection. Original exception:\n"
							+ e, "An error occured in the import module." );
		} catch( ClassNotFoundException e ) { // no MySQL jdbc driver
			throw new GeneralLoggingException(
					"No MySQL jdbc driver found. Please make sure that you have the latest JDBC driver in your classpath.",
					"Internal error in the import module. Please read the logs" );
		}
	}

	/**
	 * @return the connection
	 */
	protected java.sql.Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	protected void setConnection( java.sql.Connection connection ) {
		this.connection = connection;
	}

	/**
	 * @return the database
	 */
	protected String getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	protected void setDatabase( String database ) {
		this.database = database;
	}

	/**
	 * Fetch the courses from the database.
	 * 
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Map< Integer, Course > fetchCourses() throws GeneralLoggingException {
		Map< Integer, Course > resultingMap = new HashMap< Integer, Course >();

		String command = "SELECT id, name, created_at, updated_at, url, description FROM "
				+ this.getDatabase() + ".lectures";
		try {
			Statement selectStmt = this.getConnection().createStatement();
			ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "name" );
				final long createdAt = results.getLong( "created_at" );
				final long updatedAt = results.getLong( "updated_at" );
				final String description = results.getString( "description" );
				final String url = results.getString( "url" );

				Course newCourse = new Course( title );
				newCourse.setId( id ).setCreationDate( new Date( createdAt ) )
						.setModificationDate( new Date( updatedAt ) )
						.setSummary( description ).setUrl( url );

				resultingMap.put( id, newCourse );
			}
		} catch( SQLException e ) {
			throw new GeneralLoggingException(
					getClass() + ":fetchCourses: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return resultingMap;
	}

	public Map< Integer, Board > fetchBoards( Map< Integer, Course > courseMap )
			throws GeneralLoggingException {
		Map< Integer, Board > boardMap = new HashMap< Integer, Board >();

		String command = "SELECT id, name, lecture_id AS course_id, description, created_at, updated_at FROM "
				+ this.getDatabase() + ".courses";

		try {
			Statement selectStmt = this.getConnection().createStatement();
			ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "name" );
				final long createdAt = results.getLong( "created_at" );
				final long updatedAt = results.getLong( "updated_at" );
				final String description = results.getString( "description" );
				final int courseId = results.getInt( "course_id" );

				Course course = courseMap.get( courseId );

				if( null == course ) {
					Application
							.log( getClass()
									+ ":fetchBoards(): lecture entity for given course doesn't exist. Lecture id: "
									+ courseId + "; course: " + id,
									LogType.ERROR );
				} else {
					Board newBoard = new Board( course );
					newBoard.setId( id ).setTitle( title )
							.setCreationDate( new Date( createdAt ) )
							.setModificationDate( new Date( updatedAt ) );
					newBoard.setIntro( description );
					course.addBoard( newBoard );

					boardMap.put( id, newBoard );
				}
			}
		} catch( SQLException e ) {
			throw new GeneralLoggingException(
					getClass() + ":fetchCourses: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return boardMap;
	}

	public Map< Integer, BoardThread > fetchBoardThreads(
			Map< Integer, Board > boardMap ) throws GeneralLoggingException {
		Map< Integer, BoardThread > threadMap = new HashMap< Integer, BoardThread >();

		String command = "SELECT id, subject, body, post_type, parent_id, course_id AS board_id, created_at, updated_at FROM "
				+ this.getDatabase()
				+ ".posts WHERE post_type IN ('question', 'announcement') AND parent_id IS NULL";

		try {
			Statement selectStmt = this.getConnection().createStatement();
			ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "subject" );
				final String content = results.getString( "body" );
				final String postType = results.getString( "post_type" );
				final int parentId = results.getInt( "parent_id" );
				final long createdAt = results.getLong( "created_at" );
				final long updatedAt = results.getLong( "updated_at" );
				final int boardId = results.getInt( "board_id" );

				Board board = boardMap.get( boardId );

				if( null == board ) {
					Application
							.log( getClass()
									+ ":fetchBoards(): course entity for given posting thread doesn't exist. course id: "
									+ boardId + "; post: " + id, LogType.ERROR );
				} else {
					BoardThread newThread = new BoardThread( board );
					newThread.setId( id )
							.setCreationDate( new Date( createdAt ) )
							.setModificationDate( new Date( updatedAt ) )
							.setTitle( title );

					Posting posting = new Posting( newThread );
					posting.setContent( content ).setPostingType( postType )
							.setCreationDate( new Date( createdAt ) )
							.setModificationDate( new Date( updatedAt ) );

					newThread.addPosting( posting );

					board.addThread( newThread );

					threadMap.put( id, newThread );
				}
			}
		} catch( SQLException e ) {
			throw new GeneralLoggingException(
					getClass() + ":fetchBoardThreads: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return threadMap;
	}

	public Collection< Posting > fetchPostings(
			Map< Integer, BoardThread > threads )
			throws GeneralLoggingException {
		Collection< Posting > postingCollection = new HashSet< Posting >();

		for( Integer threadid : threads.keySet() ) {
			this.fetchPostingRecusively( threadid, postingCollection, threads.get( threadid ) );
		}

		return postingCollection;
	}

	/**
	 * Fetch children postings recusiveley.
	 * @param threadid
	 * @param postingCollection
	 * @param boardThread 
	 * @throws GeneralLoggingException 
	 */
	private void fetchPostingRecusively( Integer parentId,
			Collection< Posting > postingCollection, BoardThread boardThread ) throws GeneralLoggingException {
		String command = "SELECT id, subject, body, post_type, parent_id, course_id AS board_id, created_at, updated_at FROM "
				+ this.getDatabase() + ".posts WHERE parent_id = " + parentId;
		
		try {
			Statement selectStmt = this.getConnection().createStatement();
			ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				
				// Create posting instance
				final int id = results.getInt( "id" );
				final String title = results.getString( "subject" );
				final String content = results.getString( "body" );
				final String postType = results.getString( "post_type" );
				final int parentPostingId = results.getInt( "parent_id" );
				final long createdAt = results.getLong( "created_at" );
				final long updatedAt = results.getLong( "updated_at" );
//				final int boardId = results.getInt( "board_id" );
				
				Posting newPosting = new Posting( boardThread );
				newPosting.setContent( content )
						.setParentPostingId( parentPostingId )
						.setPostingType( postType )
						.setTitle( title )
						.setId( id )
						.setCreationDate( new Date( createdAt ) )
						.setModificationDate( new Date( updatedAt ) );
				boardThread.addPosting( newPosting );
				postingCollection.add( newPosting );
				
				this.fetchPostingRecusively( id, postingCollection, boardThread );
			}
			
		
		} catch( SQLException e ) {
			throw new GeneralLoggingException(
					getClass() + ":fetchPostingRecursiveley: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

	}
}
