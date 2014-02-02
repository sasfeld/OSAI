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
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
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
		} catch( final SQLException e ) { // no connection
			throw new GeneralLoggingException(
					this.getClass()
							+ ":initializeConnection(): couldn't open connection. Original exception:\n"
							+ e, "An error occured in the import module." );
		} catch( final ClassNotFoundException e ) { // no MySQL jdbc driver
			throw new GeneralLoggingException(
					"No MySQL jdbc driver found. Please make sure that you have the latest JDBC driver in your classpath.",
					"Internal error in the import module. Please read the logs" );
		}
	}

	/**
	 * @return the connection
	 */
	protected java.sql.Connection getConnection() {
		return this.connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	protected void setConnection( final java.sql.Connection connection ) {
		this.connection = connection;
	}

	/**
	 * @return the database
	 */
	protected String getDatabase() {
		return this.database;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	protected void setDatabase( final String database ) {
		this.database = database;
	}

	/**
	 * Fetch the courses from the database.
	 * 
	 * @param auditoriumCourseSet 	 * 
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Map< Integer, Course > fetchCourses(final LmsCourseSet auditoriumCourseSet) throws GeneralLoggingException {
		final Map< Integer, Course > resultingMap = new HashMap< Integer, Course >();

		final String command = "SELECT id, name, created_at, updated_at, url, description FROM "
				+ this.getDatabase() + ".lectures";
		try {
			final Statement selectStmt = this.getConnection().createStatement();
			final ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "name" );
				
				final java.sql.Date createdAt = results.getDate( "created_at" );
				final java.sql.Date updatedAt = results.getDate( "updated_at" );
				final String description = results.getString( "description" );
				final String url = results.getString( "url" );

				final Course newCourse = new Course( title, auditoriumCourseSet );
				newCourse.setId( id ).setCreationDate( createdAt )
						.setModificationDate( updatedAt )
						.setSummary( description ).setUrl( url );

				resultingMap.put( id, newCourse );
			}
		} catch( final SQLException e ) {
			throw new GeneralLoggingException(
					this.getClass() + ":fetchCourses: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return resultingMap;
	}

	/**
	 * Fetch board entities and map it to the given {@link Course} and Board
	 * instances.
	 * 
	 * @param courseMap
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Map< Integer, Board > fetchBoards( final Map< Integer, Course > courseMap )
			throws GeneralLoggingException {
		final Map< Integer, Board > boardMap = new HashMap< Integer, Board >();

		final String command = "SELECT id, name, lecture_id AS course_id, description, created_at, updated_at FROM "
				+ this.getDatabase() + ".courses";

		try {
			final Statement selectStmt = this.getConnection().createStatement();
			final ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "name" );
				final Date createdAt = results.getDate( "created_at" );
				final Date updatedAt = results.getDate( "updated_at" );
				final String description = results.getString( "description" );
				final int courseId = results.getInt( "course_id" );

				final Course course = courseMap.get( courseId );

				if( null == course ) {
					Application
							.log( this.getClass()
									+ ":fetchBoards(): lecture entity for given course doesn't exist. Lecture id: "
									+ courseId + "; course: " + id,
									LogType.ERROR );
				} else {
					final Board newBoard = new Board( course );
					newBoard.setId( id ).setTitle( title )
							.setCreationDate(  createdAt  )
							.setModificationDate(  updatedAt  );
					newBoard.setIntro( description );
					course.addBoard( newBoard );

					boardMap.put( id, newBoard );
				}
			}
		} catch( final SQLException e ) {
			throw new GeneralLoggingException(
					this.getClass() + ":fetchCourses: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return boardMap;
	}

	/**
	 * Fetch the thread entities and map it to {@link BoardThread} instances.
	 * 
	 * @param boardMap
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Map< Integer, BoardThread > fetchBoardThreads(
			final Map< Integer, Board > boardMap ) throws GeneralLoggingException {
		final Map< Integer, BoardThread > threadMap = new HashMap< Integer, BoardThread >();

		final String command = "SELECT id, subject, body, post_type, parent_id, course_id AS board_id, created_at, updated_at FROM "
				+ this.getDatabase()
				+ ".posts WHERE post_type IN ('question', 'announcement') AND parent_id IS NULL";

		try {
			final Statement selectStmt = this.getConnection().createStatement();
			final ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "subject" );
				final String content = results.getString( "body" );
				final String postType = results.getString( "post_type" );
				final Date createdAt = results.getDate( "created_at" );
				final Date updatedAt = results.getDate( "updated_at" );
				final int boardId = results.getInt( "board_id" );

				final Board board = boardMap.get( boardId );

				if( null == board ) {
					Application
							.log( this.getClass()
									+ ":fetchBoards(): course entity for given posting thread doesn't exist. course id: "
									+ boardId + "; post: " + id, LogType.ERROR );
				} else {
					final BoardThread newThread = new BoardThread( board );
					newThread.setId( id )
							.setCreationDate( createdAt  )
							.setModificationDate( updatedAt  )
							.setTitle( title );

					final Posting posting = new Posting( newThread );
					posting.setContent( content ).setPostingType( postType )
							.setCreationDate(  createdAt  )
							.setModificationDate( updatedAt  )
							.setId( id );

					newThread.addPosting( posting );
					newThread.setFirstPostingId( id );

					board.addThread( newThread );

					threadMap.put( id, newThread );
				}
			}
		} catch( final SQLException e ) {
			throw new GeneralLoggingException(
					this.getClass() + ":fetchBoardThreads: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return threadMap;
	}

	/**
	 * Fetch posting entities and fill it into the given {@link BoardThread}.
	 * 
	 * @param threads
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Collection< Posting > fetchPostings(
			final Map< Integer, BoardThread > threads )
			throws GeneralLoggingException {
		final Collection< Posting > postingCollection = new HashSet< Posting >();

		for( final Integer threadid : threads.keySet() ) {
			this.fetchPostingRecusively( threadid, postingCollection,
					threads.get( threadid ) );
		}

		return postingCollection;
	}

	/**
	 * Fetch children postings recusiveley.
	 * 
	 * @param threadid
	 * @param postingCollection
	 * @param boardThread
	 * @throws GeneralLoggingException
	 */
	private void fetchPostingRecusively( final Integer parentId,
			final Collection< Posting > postingCollection, final BoardThread boardThread )
			throws GeneralLoggingException {
		final String command = "SELECT id, subject, body, post_type, parent_id, course_id AS board_id, created_at, updated_at FROM "
				+ this.getDatabase() + ".posts WHERE parent_id = " + parentId;

		try {
			final Statement selectStmt = this.getConnection().createStatement();
			final ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {

				// Create posting instance
				final int id = results.getInt( "id" );
				final String title = results.getString( "subject" );
				final String content = results.getString( "body" );
				final String postType = results.getString( "post_type" );
				final int parentPostingId = results.getInt( "parent_id" );
				final Date createdAt = results.getDate( "created_at" );
				final Date updatedAt = results.getDate( "updated_at" );
				// final int boardId = results.getInt( "board_id" );

				final Posting newPosting = new Posting( boardThread );
				newPosting.setContent( content )
						.setParentPostingId( parentPostingId )
						.setPostingType( postType ).setTitle( title )
						.setId( id ).setCreationDate( createdAt  )
						.setModificationDate( updatedAt );
				boardThread.addPosting( newPosting );
				postingCollection.add( newPosting );

				this.fetchPostingRecusively( id, postingCollection, boardThread );
			}

		} catch( final SQLException e ) {
			throw new GeneralLoggingException(
					this.getClass() + ":fetchPostingRecursiveley: MySQL error: \n"
							+ e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

	}

	/**
	 * Fetch a set of all prenames.
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Set< String > fetchPrenames() throws GeneralLoggingException {
		final Set< String > prenameSet = new HashSet<String>();
		
		final String command = "SELECT DISTINCT(first_name) FROM " + this.getDatabase() + ".users";
		
		try {
			final Statement selectStmt = this.getConnection().createStatement();
			final ResultSet results = selectStmt.executeQuery( command );

			// Iterate through entities
			while( results.next() ) {
				prenameSet.add( results.getString( "first_name" ) );
			}

		} catch( final SQLException e ) {
			throw new GeneralLoggingException(
					this.getClass() + ":fetchPrenames: MySQL error: \n"
							+ e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return prenameSet;
	}
	
	/**
	 * Fetch a set of all lastnames.
	 * @return
	 * @throws GeneralLoggingException
	 */
	public Set< String > fetchLastnames() throws GeneralLoggingException {
		final Set< String > lastnameSet = new HashSet<String>();
		
		final String command = "SELECT DISTINCT(last_name) FROM " + this.getDatabase() + ".users";
		
		try {
			final Statement selectStmt = this.getConnection().createStatement();
			final ResultSet results = selectStmt.executeQuery( command );
			
			// Iterate through entities
			while( results.next() ) {
				lastnameSet.add( results.getString( "last_name" ) );
			}
			
		} catch( final SQLException e ) {
			throw new GeneralLoggingException(
					this.getClass() + ":fetchLastnames: MySQL error: \n"
							+ e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}
		
		return lastnameSet;
	}
}
