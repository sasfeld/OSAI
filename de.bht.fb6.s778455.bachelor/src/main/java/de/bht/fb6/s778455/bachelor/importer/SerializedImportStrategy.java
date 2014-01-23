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
import java.util.HashSet;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.model.Course;
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
	public Set< Course > importBoardFromStream( InputStream inputStream ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection< Course > importBoardFromFile( File inputFile )
			throws GeneralLoggingException {
		if( !inputFile.exists() || !inputFile.isFile() ) {
			throw new GeneralLoggingException( getClass()
					+ ":importBoardFromFile(): the given inputFile ("
					+ inputFile + ") doesn't exist or point to a real file.",
					"Internal error in the import module. See the logs" );
		}
		
		Collection< Course > courseCollection = new HashSet<Course>();
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream( new FileInputStream( inputFile ) );
			Object readObject = is.readObject();
			if (readObject instanceof Collection< ? >) {
				Collection< ? > courses = (Collection< ? >) readObject;
				for( Object course : courses ) {
					if (course instanceof Course) {
						courseCollection.add( (Course) course );
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new GeneralLoggingException( getClass()
					+ ":importBoardFromFile(): exception"
					+ e,
					"Internal error in the import module. See the logs" );
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch( IOException e ) {
					// already caught
				}
			}
		}
		
		if (0 == courseCollection.size()) {
			throw new GeneralLoggingException( getClass()
					+ ":importBoardFromFile(): the imported collection is empty.", "Internal error in the import module" );
		}
		return courseCollection;
	}

	@Override
	public PersonNameCorpus fillFromFile( File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

}
