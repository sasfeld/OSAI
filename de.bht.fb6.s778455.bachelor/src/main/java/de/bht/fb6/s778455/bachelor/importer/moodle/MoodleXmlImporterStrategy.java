/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.model.Posting;
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
public class MoodleXmlImporterStrategy extends AImportStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream
	 * (java.io.InputStream)
	 */
	@Override
	public Set< Course > importBoardFromStream( final InputStream inputStream ) {
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
	public LmsCourseSet importBoardFromFile( final File inputFile )
			throws GeneralLoggingException {
		if( !inputFile.exists() || !inputFile.isDirectory() ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importBoardFromFile: the given input file (" + inputFile
					+ ") doesn't exist or appear to be a directory.",
					"Internal error in the importer module. Please see the logs." );
		}

		final Map< Integer, Course > importedCourses = this.importCourses( inputFile );
		return new LmsCourseSet( importedCourses.values() );
	}

	/**
	 * Import courses from underlying course directories.
	 * 
	 * @param inputFile
	 * @return
	 * @throws GeneralLoggingException
	 */
	private Map< Integer, Course > importCourses( final File topDir )
			throws GeneralLoggingException {
		final Map< Integer, Course > courses = new HashMap< Integer, Course >();

		for( final File courseDir : topDir.listFiles() ) {
			final File courseFile = new File( courseDir, "course/course.xml" );

			if( !courseFile.exists() ) {
				throw new GeneralLoggingException(
						this.getClass()
								+ "importCourses: the given sub folder ( "
								+ courseDir
								+ " ) doesn't have a child directory representing a course.",
						"Internal error in the importer module" );
			}

			// parse course file
			final Course newCourse = this.parseCourseFile( courses, courseFile );

			// import boards
			this.importBoards( newCourse, courseDir );
			
			// fill name corpu
			final File personCorpusFile = new File( courseDir, "users.xml" );
			newCourse.setPersonNameCorpus( new PersonNameCorpus() );
			this.fillFromFile( personCorpusFile, newCourse.getPersonNameCorpus(), null );
		}
		return courses;
	}

	/**
	 * Import boards for the given course and the course's directory.
	 * 
	 * @param newCourse
	 * @param courseFile
	 * @throws GeneralLoggingException
	 */
	private void importBoards( final Course newCourse, final File courseFile )
			throws GeneralLoggingException {
		// Get a list of files containing xml files for a single forum
		final File[] forumDirs = new File( courseFile, "activities" )
				.listFiles( new FileFilter() {
					@Override
					public boolean accept( final File f ) {
						return f.getName().startsWith( "forum_" );
					}
				} );

		for( final File forumDir : forumDirs ) {
			final File forumXml = new File( forumDir, "forum.xml" );
			if( !forumXml.exists() || !forumXml.isFile() ) {
				throw new GeneralLoggingException( this.getClass()
						+ ": the given file " + forumXml + " doesn't exist.",
						"Internal error in the importer module." );
			}

			final XmlExtractor forumExtractor = new XmlExtractor(
					forumXml.getAbsolutePath(), new HashMap< String, String >() );
			final int boardId = Integer.parseInt( ( String ) forumExtractor
					.buildXPath( "//forum[1]/@id", false ) );
			final String type = ( String ) forumExtractor.buildXPath(
					"//forum[1]/type/text()", false );
			final String name = ( String ) forumExtractor.buildXPath(
					"//forum[1]/name/text()", false );
			final String intro = ( String ) forumExtractor.buildXPath(
					"//forum[1]/intro/text()", false );
			final Date timeModified = new Date(
					Long.parseLong( ( String ) forumExtractor.buildXPath(
							"//forum[1]/timemodified/text()", false ) ) );

			final Board newBoard = new Board( newCourse );
			newBoard.setId( boardId );
			newBoard.setIntro( intro );
			newBoard.setType( type );
			newBoard.setTitle( name );
			newBoard.setModificationDate( timeModified );

			newCourse.addBoard( newBoard );

			// import board's threads
			try {
				this.importBoardThreads( newBoard, forumExtractor );
			} catch( final GeneralLoggingException e ) {
				// continue
			}
		}
	}

	/**
	 * Import the board threads from the given forumExtractor (which is based on
	 * an xml file).
	 * 
	 * @param newBoard
	 * @param forumExtractor
	 * @throws GeneralLoggingException
	 */
	private void importBoardThreads( final Board newBoard, final XmlExtractor forumExtractor )
			throws GeneralLoggingException {
		try {
			final String[] discussions = ( String[] ) forumExtractor.buildXPath(
					"//forum[1]/discussions/discussion/@id", true );

			for( final String discussionId : discussions ) {
				// get attributes
				final String discussionNode = "//discussion[@id=" + discussionId
						+ "]";
				final String name = ( String ) forumExtractor.buildXPath(
						discussionNode + "/name", false );
				final int firstPostId = Integer.parseInt( ( String ) forumExtractor
						.buildXPath( discussionNode + "/firstpost", false ) );
				final Date timeModified = new Date(
						Long.parseLong( ( String ) forumExtractor.buildXPath(
								discussionNode + "/timemodified", false ) ) );

				final BoardThread newThread = new BoardThread( newBoard );
				newThread.setId( Integer.parseInt( discussionId ) );
				newThread.setTitle( name );
				newThread.setFirstPostingId( firstPostId );
				newThread.setModificationDate( timeModified );

				newBoard.addThread( newThread );

				// import postings for the thread
				try {
				this.importPostings( newThread, discussionNode, forumExtractor );
				} catch (final GeneralLoggingException e) {
					// continue
				}
			}
		} catch( NullPointerException | ClassCastException e ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importBoardThreads(): illegal format for discussions.",
					"Internal error. Please read the logs" );
		}
	}

	/**
	 * Import the postings for the new thread identified by the given discussion
	 * Node and accessible in the given xml extractor.
	 * 
	 * @param newThread
	 * @param discussionNode
	 * @param forumExtractor
	 * @throws GeneralLoggingException 
	 */
	private void importPostings( final BoardThread newThread, final String discussionNode,
			final XmlExtractor forumExtractor ) throws GeneralLoggingException {
		try {
			final String[] postings = ( String[] ) forumExtractor.buildXPath(
					discussionNode + "/posts/post/@id", true );
			
			for( final String postingId : postings ) {
				final String postingNode = "//post[@id=" + postingId
						+ "]";
				final String subject = (String) forumExtractor.buildXPath( postingNode + "/subject/text()", false );
				final String message = (String) forumExtractor.buildXPath( postingNode + "/message/text()", false );
				
				final Date timeModified = new Date(
						Long.parseLong( ( String ) forumExtractor.buildXPath(
								postingNode + "/modified", false ) ) );
				final Date timeCreated = new Date(
						Long.parseLong( ( String ) forumExtractor.buildXPath(
								postingNode + "/created", false ) ) );
				final int parentPosting = Integer.parseInt( (String ) forumExtractor.buildXPath( postingNode + "/parent/text()", false ));
				
				final Posting newPosting = new Posting( newThread );
				newPosting.setId( Integer.parseInt( postingId ));
				newPosting.setTitle( subject );
				newPosting.setContent( message );
				newPosting.setCreationDate( timeCreated );
				newPosting.setModificationDate( timeModified );
				newPosting.setParentPostingId( parentPosting );

				newThread.addPosting( newPosting );
			}
			
		} catch( NullPointerException | ClassCastException e ) {
			throw new GeneralLoggingException( this.getClass()
					+ "importPostings(): illegal format for discussions.",
					"Internal error. Please read the logs" );
		}
	}

	/**
	 * Parse the "course.xml"
	 * 
	 * @param courses
	 * @param courseFile
	 * @return
	 * @throws GeneralLoggingException
	 */
	private Course parseCourseFile( final Map< Integer, Course > courses,
			final File courseFile ) throws GeneralLoggingException {
		final XmlExtractor extractor = new XmlExtractor(
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
			} catch( final NullPointerException e ) {
				lang = "";
			}
			final long timeCreated = Long.parseLong( ( String ) extractor
					.buildXPath( "//course[1]/timecreated/text()", false ) );
			final long timeModified = Long.parseLong( ( String ) extractor
					.buildXPath( "//course[1]/timemodified/text()", false ) );

			final Course newCourse = new Course( fullName );
			newCourse.setShortName( shortName ).setSummary( summary )
					.setLang( lang ).setCreationDate( new Date( timeCreated ) )
					.setModificationDate( new Date( timeModified ) )
					.setId( courseId );

			courses.put( courseId, newCourse );

			return newCourse;

		} catch( NullPointerException | ClassCastException e ) {
			throw new GeneralLoggingException( this.getClass()
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
	public PersonNameCorpus fillFromFile( final File personCorpus,
			final PersonNameCorpus corpusInstance, final PersonNameType nameType )
			throws GeneralLoggingException {
		if (!personCorpus.exists() || !personCorpus.isFile()) {
			throw new GeneralLoggingException( this.getClass() + "fillFromFile(): the given person corpus doesn't exist: " + personCorpus, "Internal error in the importer module" );
		}
		
		final XmlExtractor extractor = new XmlExtractor( personCorpus.getAbsolutePath(), new HashMap<String, String>() );
		
		// firstnames
		final String[] firstnames = (String[]) extractor.buildXPath( "//firstname/text()", true );
		for( final String firstname : firstnames ) {
			corpusInstance.fillPrename( firstname );
		}
		
		// lastnames
		final String[] lastnames = (String[]) extractor.buildXPath( "//lastname/text()", true );
		for( final String lastname : lastnames ) {
			corpusInstance.fillLastname( lastname );
		}		
		
		return corpusInstance;
	}

}
