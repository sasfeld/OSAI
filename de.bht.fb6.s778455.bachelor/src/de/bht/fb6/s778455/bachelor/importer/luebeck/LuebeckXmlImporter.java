/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.luebeck;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.xml.XmlExtractor;

/**
 * <p>
 * This class realizes the import of XML export files of Lübeck's Moodle system.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 * 
 */
public class LuebeckXmlImporter extends AImportStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream
	 * (java.io.InputStream)
	 */
	@Override
	public Set< Course > importBoardFromStream( InputStream inputStream ) {
		throw new UnsupportedOperationException(
				"This operation is currently not supported." );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile
	 * (java.io.File)
	 */
	@Override
	public Collection< Course > importBoardFromFile( File inputFile )
			throws GeneralLoggingException {
		if( !inputFile.exists() || !inputFile.isDirectory() ) {
			throw new GeneralLoggingException( getClass()
					+ "importBoardFromFile: the given input file (" + inputFile
					+ ") doesn't exist or appear to be a directory.",
					"Internal error in the importer module. Please see the logs." );
		}

		Map< Integer, Course > importedCourses = this.importCourses( inputFile );
		return null;
	}

	/**
	 * Import courses from underlying course directories.
	 * 
	 * @param inputFile
	 * @return
	 * @throws GeneralLoggingException
	 */
	private Map< Integer, Course > importCourses( File topDir )
			throws GeneralLoggingException {
		Map< Integer, Course > courses = new HashMap< Integer, Course >();

		for( File courseDir : topDir.listFiles() ) {
			File courseFile = new File( courseDir, "course/course.xml" );

			if( !courseFile.exists() ) {
				throw new GeneralLoggingException(
						getClass()
								+ "importCourses: the given sub folder ( "
								+ courseDir
								+ " ) doesn't have a child directory representing a course.",
						"Internal error in the importer module" );
			}

			// parse course file
			this.parseCourseFile( courses, courseFile );
		}
		return courses;
	}

	/**
	 * Parse the "course.xml"
	 * 
	 * @param courses
	 * @param courseFile
	 * @throws GeneralLoggingException
	 */
	private void parseCourseFile( Map< Integer, Course > courses,
			File courseFile ) throws GeneralLoggingException {
		XmlExtractor extractor = new XmlExtractor(
				courseFile.getAbsolutePath(), new HashMap< String, String >() );

		// only handle the first course xml node
		try {
			final int courseId = Integer.parseInt( ( String ) extractor
					.buildXPath( "/course[1]/@id", false ) );
			final String shortName = ( String ) extractor.buildXPath(
					"/course[1]/shortname/text()", false );
			final String fullName = ( String ) extractor.buildXPath(
					"/course[1]/fullname/text()", false );
			final String summary = ( String ) extractor.buildXPath(
					"/course[1]/summary/text()", false );
			String lang = "";
			try {
				lang = ( String ) extractor.buildXPath(
						"/course[1]/lang/text()", false );
			} catch( NullPointerException e ) {
				lang = "";
			}
			final long timeCreated = Long.parseLong( ( String ) extractor
					.buildXPath( "//course[1]/timecreated/text()", false ) );
			final long timeModified = Long.parseLong( ( String ) extractor
					.buildXPath( "//course[1]/timemodified/text()", false ) );

			Course newCourse = new Course( fullName );
			newCourse.setShortName( shortName ).setSummary( summary )
					.setLang( lang ).setCreationDate( new Date( timeCreated ) )
					.setModificationDate( new Date( timeModified ) )
					.setId( courseId );

			courses.put( courseId, newCourse );

		} catch( NullPointerException | ClassCastException e ) {
			throw new GeneralLoggingException( getClass()
					+ "parseCourseFile: exception " + e,
					"Internal error in the importer module. See the logs" );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#fillFromFile(java
	 * .io.File, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus,
	 * de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType)
	 */
	@Override
	public PersonNameCorpus fillFromFile( File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

}
