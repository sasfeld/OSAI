/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class offers general static methods to work with {@link File} instances.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 * 
 */
public class FileUtil {

	/**
	 * Read a file line based and return a list of String.
	 * 
	 * @param file
	 * @param encoding
	 *            the encoding, like "UTF-8" (see {@link Charset} for legal
	 *            values)
	 * @return
	 * @throws GeneralLoggingException
	 */
	public static List< String > readFileLineBased( File file, String encoding )
			throws GeneralLoggingException {
		try {
			Charset.forName( encoding );
		} catch( UnsupportedCharsetException e ) {
			throw new GeneralLoggingException(
					"readFileLineBased(): the given charset/encoding "
							+ encoding
							+ " is illegal. See the JAVA-API Charset documentation for legal values.",
					"Internal error, read the logs." );
		}

		if( file == null || !file.exists() ) {
			throw new IllegalArgumentException(
					"readFileLineBased(): The given file is null or doesn't exist!" );
		}

		List< String > lines = new ArrayList< String >();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ), encoding ) );
			String line;
			while( null != ( line = reader.readLine() ) ) {
				lines.add( line ); // add single line to collection
			}
		} catch( IOException e ) {
			throw new GeneralLoggingException(
					"readFileLineBased(): The given file ("
							+ file.getAbsolutePath()
							+ ") is not valid. Original exception:\n" + e,
					"Internal error. See the logs" );
		} finally { // clean up
			if( null != reader ) {
				try {
					reader.close();
				} catch( IOException e ) {
					// don't do anything here. an exception was already caught
					// above
				}
			}
		}

		return lines;
	}
}
