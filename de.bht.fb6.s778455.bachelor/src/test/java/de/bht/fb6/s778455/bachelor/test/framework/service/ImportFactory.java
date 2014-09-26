/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.framework.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.NerTag;
import de.bht.fb6.s778455.bachelor.model.PosTag;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;

/**
 * <p>This class contains factory methods to create instances used in the importer module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class ImportFactory {
    
    
    public static LmsCourseSet newDummyCourseSet()
    {
        final Course course = new Course("Sample course", new LmsCourseSet(
                "unit test course set"));
        try {
            course.setWebUrl(new URL("http://test.de"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        course.setLanguage(Language.GERMAN);
        course.addTag(new PosTag("NN", 0.0, "Beispiel", "beispiel.xad"),
                TagType.POS_TAG);
        course.addTag(new NerTag("I-LOC", 0.0, "Berlin", "berlin.xad"),
                TagType.NER_TAG);
        course.addTag(new TopicZoomTag(5, 1, 3, "Berlin", "214152"),
                TagType.TOPIC_ZOOM);

        // input boards
        final Board sampleBoard1 = new Board(course, "Sample board");

        final BoardThread sampleThread1 = new BoardThread();
        sampleThread1.setTitle("sampleThread1");
        sampleThread1.setCreationDate(new Date());

        final Posting samplePosting1 = new Posting();
        samplePosting1.setContent("This is a sample posting.\nAnd a newline.");
        samplePosting1.setCreationDate(new Date());
        samplePosting1
                .setTaggedContent("This content is tagged by <I-PERS>Max Mustermann</I-PERS>.");
        samplePosting1.setPostingType("question");
        samplePosting1.setLang(Language.GERMAN);
        try {
            samplePosting1.setWebUrl(new URL("http://example.org/posting1"));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sampleThread1.addPosting(samplePosting1);

        final ArrayList<Tag> tagList = new ArrayList<Tag>();
        tagList.add(new TopicZoomTag(6, 4, 3, "test", "testuri"));
        tagList.add(new TopicZoomTag(3, 2, 1, "test", "testuri2"));

        samplePosting1.setTags(tagList, TagType.TOPIC_ZOOM);
        samplePosting1.addTag(new NerTag("I-LOC", -1.0, "Berlin", "berlin"),
                TagType.NER_TAG);
        samplePosting1.addTag(new PosTag("NN", 0.0, "Variable", ""),
                TagType.POS_TAG);

        final Posting samplePosting2 = new Posting();
        samplePosting2.setContent("This is a another posting");
        samplePosting2.setCreationDate(new Date(samplePosting1
                .getCreationDate().getTime() + 1000 * 60 * 60 * 24)); // + 1 day
        samplePosting2
                .setTaggedContent("This content is tagged by <I-PERS>Max Mustermann</I-PERS>.");
        sampleThread1.addPosting(samplePosting2);

        sampleBoard1.addThread(sampleThread1);

        final BoardThread sampleThread2 = new BoardThread();
        sampleThread2.setTitle("sampleThread2");
        sampleThread2.setCreationDate(new Date(sampleThread1.getCreationDate()
                .getTime() + 1000 * 60 * 60 * 24));
        sampleThread2.addPosting(samplePosting2);
        sampleThread2.addPosting(samplePosting1);

        sampleBoard1.addThread(sampleThread2);

        course.addBoard(sampleBoard1);

        final LmsCourseSet courseSet = new LmsCourseSet("unit test lms course");
        courseSet.add(course);
        
        return courseSet;
    }

}
