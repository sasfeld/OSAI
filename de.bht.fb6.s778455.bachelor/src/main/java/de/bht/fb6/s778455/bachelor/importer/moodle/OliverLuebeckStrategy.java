/**
 * Copyright (c) 2013-14 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * This class realizes the import of Oliver Blums testdata xml files.
 * </p>
 * <p>
 * For the mapping documentation, please see https://docs.google.com/document/d/1sMt7StFDnKT-dssAXvA0mXy2Kpro5RpuZZlq4PUV-Po/edit?usp=sharing .
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 * 
 */
public class OliverLuebeckStrategy extends AImportStrategy {

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream(java.io.InputStream)
	 */
	@Override
	public Set<Course> importBoardFromStream(InputStream inputStream)
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile(java.io.File)
	 */
	@Override
	public LmsCourseSet importBoardFromFile(File inputFile)
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#fillFromFile(java.io.File, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType)
	 */
	@Override
	public PersonNameCorpus fillFromFile(File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType)
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

}
