/**
 * Copyright (c) 2013-14 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.xml.XmlExtractor;

/**
 * <p>
 * This class realizes the import of Oliver Blums testdata xml files.
 * </p>
 * <p>
 * For the mapping documentation, please see
 * https://docs.google.com/document/d/1
 * sMt7StFDnKT-dssAXvA0mXy2Kpro5RpuZZlq4PUV-Po/edit?usp=sharing .
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 * 
 */
public class OliverLuebeckStrategy extends AImportStrategy {
	/**
	 * Name of the {@link LmsCourseSet} that is given by this strategy.
	 */
	public static final String NAME_COURSE_SET = "oliver_luebeck";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream
	 * (java.io.InputStream)
	 */
	@Override
	public Set<Course> importBoardFromStream(InputStream inputStream)
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile
	 * (java.io.File)
	 */
	@Override
	public LmsCourseSet importBoardFromFile(File inputFile)
			throws GeneralLoggingException {
		if (!inputFile.exists() || !inputFile.isDirectory()) {
			throw new GeneralLoggingException(this.getClass()
					+ "importBoardFromFile: the given input file (" + inputFile
					+ ") doesn't exist or appear to be a directory.",
					"Internal error in the importer module. Please see the logs.");
		}

		// set the lms course set name from the upper file's name
		final LmsCourseSet courseSet = new LmsCourseSet(NAME_COURSE_SET);
		final Set<Course> importedCourses = this.importCourses(
				inputFile, courseSet);
		courseSet.addAll(importedCourses);
		return courseSet;
	}

	/**
	 * Import courses and go deeper into the hierarchy.
	 * 
	 * @param inputFile
	 * @param courseSet
	 * @return
	 * @throws GeneralLoggingException
	 */
	private Set<Course> importCourses(File inputFile,
			LmsCourseSet courseSet) throws GeneralLoggingException {
		File[] forumFiles = inputFile.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.trim().toLowerCase().startsWith("forum");
			}
		});

		Set<Course> courses = new HashSet<>();
		for (File forumFile : forumFiles) {

			int fileNumber = this.getNumberFromFileName(forumFile);
			// add the currently only one existing course
			Course newCourse = ServiceFactory.newCourse(fileNumber,
					"oliver_luebeck_course_" + fileNumber, courseSet);
			// create the currently only existing board
			Board newBoard = ServiceFactory.newBoard(fileNumber,
					forumFile.getName(), newCourse, Language.GERMAN);

			// parse the XML files to add threads and postings
			this.importThreads(forumFile, newBoard);

			// add instances to hierarchy
			newCourse.addBoard(newBoard);
			courses.add(newCourse);
		}

		return courses;
	}

	/**
	 * Import threads from the given xml file and delegate the parsing.
	 * 
	 * @param forumFile
	 * @param newBoard
	 * @throws GeneralLoggingException
	 */
	private void importThreads(File forumFile, Board newBoard)
			throws GeneralLoggingException {
		XmlExtractor forumExtractor = ServiceFactory.getXmlExtractor(forumFile);
		final String[] threadNodes = (String[]) forumExtractor.buildXPath(
				"//thread/@title", true);

		int threadNumber = 1;
		for (final String threadTitle : threadNodes) {
			// create BoardThread
			final BoardThread newThread = ServiceFactory.newBoardThread(
					threadNumber, threadTitle, newBoard, Language.GERMAN);
			newThread.setFirstPostingId(1);

			// get attributes
			final String threadNode = "//thread[@title='" + threadTitle + "']";
			this.importPostings(forumExtractor, threadNode, newThread);

			// add instance to hierarchy
			newBoard.addThread(newThread);

			threadNumber++;
		}
	}

	/**
	 * Import the postings using the given {@link XmlExtractor} and the
	 * threadNode XPath query.
	 * 
	 * @param forumExtractor
	 * @param threadNode
	 * @param newThread
	 */
	private void importPostings(XmlExtractor forumExtractor, String threadNode,
			BoardThread newThread) {
		final String[] postNodes = (String[]) forumExtractor.buildXPath(
				threadNode + "/post/@id", true);

		for (final String postId : postNodes) {
			final String postNode = threadNode + "/post[@id='" + postId + "']";
			// fetch attributes
			Integer parentId = null;
			try {
				final Object parentPostingId = forumExtractor.buildXPath(
						postNode + "/@reply", false);
				parentId = Integer.parseInt((String) parentPostingId);
			} catch (NullPointerException e) {
				// just continue, this only occurs if no reply attribute was
				// given.
			}

			// fetch content
			String content = (String) forumExtractor.buildXPath(postNode
					+ "/text()", false);

			// create Posting instance and assign attributes
			Posting newPosting = ServiceFactory.newPosting(
					Integer.parseInt(postId), content, newThread,
					Language.GERMAN);
			if (null != parentId) {
				newPosting.setParentPostingId(parentId);
			}

			// add to hierarchy
			newThread.addPosting(newPosting);
		}
	}

	/**
	 * Get the file number of the given xml file, so that [NUMBER] is extracted
	 * from 'forum[NUMBER].xml' .
	 * 
	 * @param inputFile
	 * @return
	 */
	private int getNumberFromFileName(File inputFile) {
		return Integer.parseInt(inputFile.getName().substring(5, 6));
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
	public PersonNameCorpus fillFromFile(File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType)
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}

}
