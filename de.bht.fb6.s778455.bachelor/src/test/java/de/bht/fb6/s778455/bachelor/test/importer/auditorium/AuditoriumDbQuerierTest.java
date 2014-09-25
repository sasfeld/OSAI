/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.auditorium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.auditorium.AuditoriumDbQuerier;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.ConfigReader;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link AuditoriumDbQuerier}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 * 
 */
public class AuditoriumDbQuerierTest extends NoLoggingTest {

    protected AuditoriumDbQuerier dbQuerier;
    protected boolean skipTest = false;
    private ConfigReader configReader;

    public AuditoriumDbQuerierTest()
    {
        super();
        
        this.configReader = super.getServiceFactory().getService(ConfigReader.SERVICE_NAME);
        skipTest = !configReader.isAuditoriumImportTestEnabled();
    }
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        if (this.skipTest) {
            return;
        }
        final String host = this.configReader.getAuditoriumHost();
        final String user = this.configReader.getAuditoriumUser();
        final String password = this.configReader.getAuditoriumPw();
        final String dbname = this.configReader.getAuditoriumDbName();
        this.dbQuerier = new AuditoriumDbQuerier(host, user, password, dbname);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.dbQuerier = null;
    }

    @Test
    public void testFetchCourses() throws GeneralLoggingException {
        if (this.skipTest) {
            return;
        }
        
        final Map<Integer, Course> fetchedCourses = this.dbQuerier
                .fetchCourses(new LmsCourseSet("unit test course set"));

        assertTrue(null != fetchedCourses);

        final int numberFecthedCourses = fetchedCourses.size();
        assertTrue(0 < numberFecthedCourses);


        final Collection<Course> courses = fetchedCourses.values();
        for (final Course course : courses) {
            assertTrue(0 != course.getId());
        }

        // test fetchBoards
        final Map<Integer, Board> fetchedBoards = this.dbQuerier
                .fetchBoards(fetchedCourses);

        assertTrue(null != fetchedBoards);

        final int numberFetchedBoards = fetchedBoards.size();
        assertTrue(0 < numberFetchedBoards);

        for (final Board board : fetchedBoards.values()) {
            assertTrue(0 != board.getId());
        }

        // test fetchBoardThreads
        final Map<Integer, BoardThread> fetchedThreads = this.dbQuerier
                .fetchBoardThreads(fetchedBoards);

        assertTrue(null != fetchedThreads);
        final int numberFetchedThreads = fetchedThreads.size();
        assertTrue(0 < numberFetchedThreads);

        for (final BoardThread thread : fetchedThreads.values()) {
            assertTrue(0 != thread.getId());
        }

        // test fetchPostings
        final Collection<Posting> fetchedPostings = this.dbQuerier
                .fetchPostings(fetchedThreads);

        assertTrue(null != fetchedPostings);
        final int numberFetchedPostings = fetchedPostings.size();
        assertTrue(0 < numberFetchedPostings);

        for (final Posting posting : fetchedPostings) {
            assertTrue(0 != posting.getId());
        }

        // ensure that the number of courses didn'T change
        assertEquals(numberFecthedCourses, fetchedCourses.size());

        // iterate through boards and compare maps above
        int numContainedBoards = 0;
        int numContainedThreads = 0;
        int numContainedPostings = 0;
        for (final Course fCourse : fetchedCourses.values()) {
            numContainedBoards += fCourse.getBoards().size();

            for (final Board board : fCourse.getBoards()) {
                numContainedThreads += board.getBoardThreads().size();

                for (final BoardThread thread : board.getBoardThreads()) {
                    numContainedPostings += thread.getPostings().size();
                }
            }
        }

        assertEquals(numberFetchedBoards, numContainedBoards);
        assertEquals(numberFetchedThreads, numContainedThreads);
        // Fetched postings only contains child postings, not parent postings,
        // so it must be less than
        assertTrue(numberFetchedPostings < numContainedPostings);
        assertEquals(655, numContainedPostings - numberFetchedPostings);
    }

    @Test
    public void testFetchnames() throws GeneralLoggingException {
        if (this.skipTest) {
            return;
        }
        
        final Set<String> prenames = this.dbQuerier.fetchPrenames();
        final Set<String> lastnames = this.dbQuerier.fetchLastnames();

        assertTrue(null != prenames);
        assertTrue(null != lastnames);

        assertTrue(0 < prenames.size());
        assertTrue(0 < lastnames.size());
    }

}
