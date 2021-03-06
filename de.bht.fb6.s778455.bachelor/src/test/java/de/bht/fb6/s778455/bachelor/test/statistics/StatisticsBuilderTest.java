/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.statistics;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.StatisticsModel;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.GeneralStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.TagStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the decorater {@link AStatisticsBuilder}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class StatisticsBuilderTest extends NoLoggingTest {
    private LmsCourseSet collection;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.collection = this.createDummyCollection();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.collection = null;
    }

    private LmsCourseSet createDummyCollection() {
        final LmsCourseSet coll = new LmsCourseSet("unit test");

        final Course dummyCourse = new Course("dummy", coll);
        coll.add(dummyCourse);
        final Board dummyBoard = new Board(dummyCourse, "Dummy board");
        dummyCourse.addBoard(dummyBoard);
        final BoardThread dummyThread = new BoardThread(dummyBoard);
        dummyBoard.addThread(dummyThread);

        // posting
        final Posting p = new Posting(dummyThread);
        // add some test tags
        p.addTag(new Tag(8.0, "testtag", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(new Tag(4.0, "testtag2", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(new Tag(3.0, "testtag3", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(new Tag(2.0, "testtag4", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(new Tag(1.0, "testtag5", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(new Tag(0.626123, "testtag6", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(
                new Tag(0.1351533, "testtag7", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        p.addTag(new Tag(5, "nertag1", "some uri", TagType.NER_TAG),
                TagType.NER_TAG);
        p.addTag(new Tag(4, "nertag2", "some uri", TagType.NER_TAG),
                TagType.NER_TAG);
        p.addTag(new Tag(3, "nertag3", "some uri", TagType.NER_TAG),
                TagType.NER_TAG);
        dummyThread.addPosting(p);

        // add a posting without tags
        dummyThread.addPosting(new Posting(dummyThread));

        // another thread
        final BoardThread anotherThread = new BoardThread(dummyBoard);
        dummyBoard.addThread(anotherThread);

        // posting
        final Posting anotherP = new Posting(dummyThread);
        // add some test tags
        anotherP.addTag(
                new Tag(8.0, "testtag", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        anotherP.addTag(
                new Tag(4.0, "testtag2", "some uri", TagType.TOPIC_ZOOM),
                TagType.TOPIC_ZOOM);
        anotherP.addTag(new Tag(5, "nertag1", "some uri", TagType.TOPIC_ZOOM),
                TagType.NER_TAG);
        anotherThread.addPosting(anotherP);

        return coll;
    }

    @Test
    public void testBuildStatistics() {
        // first only test the GeneralStatisticsBuilder
        final AStatisticsBuilder generalBuilder = new GeneralStatisticsBuilder();
        StatisticsModel statisticsModel = generalBuilder
                .buildStatistics(this.collection);

        assertEquals(1, statisticsModel.getNumberCourses());
        assertEquals(1, statisticsModel.getNumberBoards());
        assertEquals(2, statisticsModel.getNumberThreads());
        assertEquals(3, statisticsModel.getNumberPostings());

        // now, test only the TagStatisticsBuilder
        final AStatisticsBuilder tagBuilder = new TagStatisticsBuilder();
        statisticsModel = tagBuilder.buildStatistics(this.collection);

        assertEquals(0, statisticsModel.getNumberCourses());
        assertEquals(0, statisticsModel.getNumberBoards());
        assertEquals(0, statisticsModel.getNumberThreads());
        assertEquals(0, statisticsModel.getNumberPostings());

        assertEquals(9, statisticsModel.getNumberTopicZoomTags());
        assertEquals(4, statisticsModel.getNumberNerTags());
        assertEquals(13, statisticsModel.getNumberTags());

        assertEquals(2, statisticsModel.getNumberTopicZoomTaggedPostings());
        assertEquals(2, statisticsModel.getNumberNerTaggedPostings());
        assertEquals(2, statisticsModel.getNumberTaggedPostings());
        assertEquals(2, statisticsModel.getNumberTzAndNerTaggedPostings());

        // now test the decoration
        final AStatisticsBuilder decoratedBuilder = new GeneralStatisticsBuilder(
                new TagStatisticsBuilder());
        statisticsModel = decoratedBuilder.buildStatistics(this.collection);

        assertEquals(1, statisticsModel.getNumberCourses());
        assertEquals(1, statisticsModel.getNumberBoards());
        assertEquals(2, statisticsModel.getNumberThreads());
        assertEquals(3, statisticsModel.getNumberPostings());

        assertEquals(9, statisticsModel.getNumberTopicZoomTags());
        assertEquals(4, statisticsModel.getNumberNerTags());
        assertEquals(13, statisticsModel.getNumberTags());

        assertEquals(2, statisticsModel.getNumberTopicZoomTaggedPostings());
        assertEquals(2, statisticsModel.getNumberNerTaggedPostings());
        assertEquals(2, statisticsModel.getNumberTaggedPostings());
        assertEquals(2, statisticsModel.getNumberTzAndNerTaggedPostings());
    }

}
