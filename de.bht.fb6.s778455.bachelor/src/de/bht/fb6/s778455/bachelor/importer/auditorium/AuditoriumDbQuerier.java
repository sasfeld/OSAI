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
import java.util.HashSet;

import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Course;
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
	public Collection< Course > fetchCourses() throws GeneralLoggingException {
		Collection< Course > resultingCollection = new HashSet< Course >();

		String command = "SELECT id, name, created_at, updated_at, url, description FROM "
				+ this.getDatabase() + ".lectures";
		try {
			Statement selectStmt = this.getConnection().createStatement();
			ResultSet results = selectStmt.executeQuery( command );
			
			// Iterate through entities
			while ( results.next() ) {
				// Create course instance for each entity
				final int id = results.getInt( "id" );
				final String title = results.getString( "name" );
				final long createdAt = results.getLong( "created_at" );
				final long updatedAt = results.getLong( "updated_at" );
				final String description = results.getString( "description" );
				final String url = results.getString( "url" );
				
				
				Course newCourse = new Course( title );
				newCourse.setId( id )
						.setCreationDate( new Date( createdAt ) )
						.setModificationDate( new Date( updatedAt ) )
						.setSummary( description ) 
						.setUrl( url );
				
				resultingCollection.add( newCourse );
			}
		} catch( SQLException e ) {
			throw new GeneralLoggingException(
					getClass() + ":fetchCourses: MySQL error: \n" + e,
					"Internal error while working with MySQL in the importer module. Please see the logs." );
		}

		return resultingCollection;
	}

}
