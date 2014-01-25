/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * This class ....
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 * 
 */
public class SerializedImportStrategy extends AImportStrategy {

	@Override
	public Set< Course > importBoardFromStream( final InputStream inputStream ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LmsCourseSet importBoardFromFile( final File inputFile )
			throws GeneralLoggingException {
		if( !inputFile.exists() || !inputFile.isFile() ) {
			throw new GeneralLoggingException( this.getClass()
					+ ":importBoardFromFile(): the given inputFile ("
					+ inputFile + ") doesn't exist or point to a real file.",
					"Internal error in the import module. See the logs" );
		}
		
		final LmsCourseSet courseSet = new LmsCourseSet();
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream( new FileInputStream( inputFile ) );
			final Object readObject = is.readObject();
			if (readObject instanceof Collection< ? >) {
				final Collection< ? > courses = (Collection< ? >) readObject;
				for( final Object course : courses ) {
					if (course instanceof Course) {
						courseSet.add( (Course) course );
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new GeneralLoggingException( this.getClass()
					+ ":importBoardFromFile(): exception"
					+ e,
					"Internal error in the import module. See the logs" );
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch( final IOException e ) {
					// already caught
				}
			}
		}
		
		if (0 == courseSet.size()) {
			throw new GeneralLoggingException( this.getClass()
					+ ":importBoardFromFile(): the imported collection is empty.", "Internal error in the import module" );
		}
		return courseSet;
	}

	@Override
	public PersonNameCorpus fillFromFile( final File personCorpus,
			final PersonNameCorpus corpusInstance, final PersonNameType nameType )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

}
