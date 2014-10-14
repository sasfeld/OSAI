/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.anonymization.controller;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.controller.AnonymizationController;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains UnitTests of the {@link AnonymizationController}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class AnonymizationControllerTest extends NoLoggingTest {
    protected static final File DIRECTORYIMPORT_TESTFOLDER = new File(
            PATH_UNITTEST_DATA_FOLDER + "/importer/file_system_test");
    protected AnonymizationController anonymizationController;

    // @formatter:off
    /*
    * ################################### 
    * #	
    * # test preparation 
    * #
    * ##################################
     */
    // @formatter:on
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.anonymizationController = new AnonymizationController();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.anonymizationController = null;
    }

    // @formatter:off
    /*
    * ################################### 
    * # 
    * # tests
    * #
    * ##################################
     */
    // @formatter:on
    @Test
    /**
     * This is a general test of the anonymization controller.
     * @throws GeneralLoggingException
     */
    public void testPerformAnonymization() throws GeneralLoggingException {
        final AImportStrategy strategy = ServiceFactory.newDirectoryImportStrategy();
        final LmsCourseSet courses = strategy
                .importBoardFromFile(DIRECTORYIMPORT_TESTFOLDER);
        final LmsCourseSet anonymizedCourses = this.anonymizationController
                .performAnonymization(courses);
        assertTrue(null != anonymizedCourses);

        // ensure that course set is iterable
        for (final Course course : anonymizedCourses) {
            assertTrue(null != course.getTitle());
            for (final Board courseBoard : course.getBoards()) {
                assertTrue(null != courseBoard.getTitle());
                for (final BoardThread boardThread : courseBoard
                        .getBoardThreads()) {
                    assertTrue(null != boardThread.getTitle());
                    for (final Posting posting : boardThread.getPostings()) {
                        assertTrue(null != posting.getTitle());
                    }

                }
            }
        }

    }

}
