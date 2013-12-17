/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.auditorium;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class realizes an {@link AImportStrategy} for the data recieved by the Auditorium of the University of Dresden.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 *
 */
public class AuditoriumImportStrategy extends AImportStrategy {

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream(java.io.InputStream)
	 */
	@Override
	public Set< Course > importBoardFromStream( InputStream inputStream ) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile(java.io.File)
	 */
	@Override
	public Collection< Course > importBoardFromFile( File inputFile )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#fillFromFile(java.io.File, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType)
	 */
	@Override
	public PersonNameCorpus fillFromFile( File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

}
