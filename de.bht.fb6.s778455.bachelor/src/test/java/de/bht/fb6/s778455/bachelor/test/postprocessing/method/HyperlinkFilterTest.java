/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.postprocessing.method;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.AbstractPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.method.HyperlinkFilter;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IReplacementTags;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;
import de.bht.fb6.s778455.bachelor.test.framework.service.ImportFactory;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.10.2014
 *
 */
public class HyperlinkFilterTest extends NoLoggingTest
{
    protected AbstractPostprocessMethod method;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.method = new HyperlinkFilter();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        this.method = null;
    }
    
    private LmsCourseSet givenLmsCourseSetWithPostingContents(String postingContent)
    {
        LmsCourseSet dummyCourseSet = ImportFactory.newDummyCourseSetWithPostingContent(postingContent);
        return dummyCourseSet;       
    }
    
    private void thenAssertPostingContents(LmsCourseSet courseSet, String res)
    {
        for (Course course : courseSet) {
            for (Board board : course.getBoards()) {
                for (BoardThread boardThread : board.getBoardThreads()) {
                    for (Posting posting : boardThread.getPostings()) {
                        assertEquals(res, posting.getContent());
                    }
                }
            }
        }
        
    }
    
    private void testFilter(String inputContent, String expectedContent)
    {
        // when special posting contents with java code are given
        LmsCourseSet courseSet = givenLmsCourseSetWithPostingContents(inputContent);
        
        // and one template method is called with text
        this.method.setLmsCourseSet(courseSet);
        this.method.afterImport();
        
        // then expect the following filtered posting contents
        this.thenAssertPostingContents(courseSet, expectedContent);
    }

    @Test
    public void testApplyFilterHyperlinkInBeginning()
    {
        testFilter("Hallo http://www.einedomain.de Link", 
                "Hallo " + IReplacementTags.HYPERLINK_REPLACEMENT_TAG + " Link"); 
    }

}
