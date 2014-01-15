/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * <p>
 * This class offers methods to parse postgre sql dump files.
 * </p>
 * 
 * <p>
 * The command to create the dump file must contain the '-c' option.<br />
 * It implies that 'copy' statements are used.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 * 
 */
public class PostgreSqlDumpParser {
	protected File dumpFile;

	/**
	 * Create a new parser for the given dump file.
	 * 
	 * @param dumpFile
	 *            mustn't be null and the file must exist
	 */
	public PostgreSqlDumpParser( final File dumpFile ) {
		if( null == dumpFile || !dumpFile.exists() || !dumpFile.isFile() ) {
			throw new IllegalArgumentException(
					getClass()
							+ ": the given value for file ("
							+ dumpFile
							+ ") isn't allowed here. Either it is null or doesn't exist" );
		}
		this.dumpFile = dumpFile;
	}

	/**
	 * Fetch the entities for a given table and the given columns.
	 * 
	 * @param tableName
	 * @param colums
	 *            String multiple columns name. Mustn't be null!
	 * @return a multidimensional {@link List} of entities. Each entitiy is a
	 *         {@link Map> of key-value pairs (key = columnName). Might return
	 *         an empty list.
	 */
	public List< Map< String, String > > fetchEntities( final String tableName,
			final String... columns ) {
		final List< Map< String, String > > returnedRows = new ArrayList< Map< String, String >>();

		BufferedReader reader = null;
		try {
			reader = this.openReader();

			// Scan until table line is reached
			final Map< Integer, String > columnPositions = new HashMap< Integer, String >();

			String line;
			boolean tableMatched = false;
			boolean matchingCompleted = false;

			while( null != ( line = reader.readLine() ) ) {
				String manipulatedEnding = line.concat( "\n" );
				// is this table of interest?
				if( tableMatched ) {
					// end of table data
					if( line.trim().toLowerCase().startsWith( "\\." ) ) {
						matchingCompleted = true;
						break;
					}
					// empty line
					else if( 0 == line.trim().length() ) {
						matchingCompleted = false;
						break;
					} else { // match each row and map the values depending on
								// the positon
						Pattern pRow = Pattern.compile( "(?i)(.*?[\\t\\n]{1})", Pattern.MULTILINE );
						// form: column => value
						Map< String, String > rowMap = new HashMap< String, String >();
						this.mapRow( pRow, rowMap, columnPositions, manipulatedEnding,
								tableName );

						returnedRows.add( rowMap );
					}
				} else if( !tableMatched
						&& line.trim().toLowerCase()
								.startsWith( "copy " + tableName ) ) {
					tableMatched = true;

					// This pattern matches the columns names including their
					// separators
					Pattern pColumns = Pattern
							.compile( "(?i)([a-z]+[,\\s)]{2})" );
					final Map< String, Integer > allColumnPositions = this
							.mapGroupPositions( pColumns, line );

					// only keep required positions
					for( String column : columns ) {
						if( allColumnPositions.containsKey( column ) ) {
							// store: column name => position
							columnPositions.put(
									allColumnPositions.get( column ), column );
						} else { // isn't contained in matched columns -> log
									// this
							Application
									.log( getClass()
											+ "fetchEntities(): The given column ("
											+ column
											+ ") couldn't be matched in the input file "
											+ this.dumpFile, LogType.ERROR );
						}
					}
				}
			}

			if( tableMatched && !matchingCompleted ) {
				Application
						.log( getClass()
								+ "fetchEntities: the given dump file seems to be corrupt. Didn't reach a valid sequence which closes the table data area. Given file: "
								+ this.dumpFile + "; given tableName: "
								+ tableName, LogType.ERROR );
			}
			else if( !tableMatched ) {
				Application
				.log( getClass()
						+ "fetchEntities: the given dump file seems to be corrupt. Didn't find the given tableName. Given file: "
						+ this.dumpFile + "; given tableName: "
						+ tableName, LogType.ERROR );
			}

			this.freeReader( reader );
		} catch( IOException e ) {
			Application.log( getClass() + "fetchEntities: exception: " + e,
					LogType.ERROR );
		} finally { // try to close the reader
			if( null != reader ) {
				this.freeReader( reader );
			}
		}

		return returnedRows;
	}

	/**
	 * Map the given data values in the given row to the resulting map of key =
	 * columnName and value = dataValue. Use the map of columnPositions to
	 * decide to which column the value belongs.
	 * 
	 * @param pRow
	 * @param rowMap
	 * @param columnPositions
	 * @param line
	 * @param tableName
	 */
	private void mapRow( Pattern pRow, Map< String, String > rowMap,
			Map< Integer, String > columnPositions, String line,
			String tableName ) {

		int groupNum = 0;
		int matchedColumns = 0;
		Matcher matcher = pRow.matcher( line );
		while( matcher.find() ) {
			// find the position of the value in the position map
			if ( columnPositions.containsKey( groupNum ) ) {
				String dataValue = matcher.group( 1 );
				
				
				// replace separators of data values
				dataValue = dataValue.replace( "\t", "" );
				dataValue = dataValue.replace( "\n", "" );
				
				rowMap.put( columnPositions.get( groupNum ), dataValue );
				
				matchedColumns++;
			}			
			

			groupNum++;
		}

		// check if the number of positions in both maps is equal
		if( columnPositions.keySet().size() != matchedColumns ) {
			Application
					.log( getClass()
							+ "mapRow(): It seems like the given dump file is corrupt because the number of data elements ("
							+ matchedColumns
							+ ") doesn't match the number of columns ("
							+ columnPositions.keySet().size() + ")."
							+ " Given input file " + this.dumpFile
							+ "; given table: " + tableName, LogType.ERROR );
		}

	}

	/**
	 * Save the groups' positions in a map.
	 * 
	 * @param pColumns
	 * @param line
	 * @return
	 */
	private Map< String, Integer > mapGroupPositions( Pattern pColumns,
			String line ) {
		Map< String, Integer > posMap = new HashMap< String, Integer >();

		int groupNum = 0;
		Matcher matcher = pColumns.matcher( line );
		while( matcher.find() ) {
			String groupValue = matcher.group( 0 );
			// replace column separator
			groupValue = groupValue.replace( ", ", "" );
			// replace colum separator of the last column
			groupValue = groupValue.replace( ") ", "" );
			posMap.put( groupValue, groupNum );

			groupNum++;
		}

		return posMap;
	}

	/**
	 * Do your work to free the reader.
	 * 
	 * @param reader
	 */
	private void freeReader( BufferedReader reader ) {
		try {
			reader.close();
		} catch( IOException e ) {
			// already caught
		}
	}

	/**
	 * Do your work before working with the {@link BufferedReader}
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	private BufferedReader openReader() throws FileNotFoundException {
		BufferedReader reader = new BufferedReader( new FileReader(
				this.dumpFile ) );
		return reader;
	}

}
