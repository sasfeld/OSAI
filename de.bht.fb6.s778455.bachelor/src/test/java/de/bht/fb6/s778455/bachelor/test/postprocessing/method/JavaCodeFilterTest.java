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
import de.bht.fb6.s778455.bachelor.postprocessing.method.JavaCodeFilter;
import de.bht.fb6.s778455.bachelor.test.framework.service.ImportFactory;

/**
 * <p>This unittests test the {@link JavaCodeFilter} adapter which uses the {@link de.blum.nlp.filter.JavaCodeFilter} by Oliver Blum.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class JavaCodeFilterTest
{
    protected JavaCodeFilter filter;

    /*
     * ##################################
     * #
     * # test preparation
     * #
     * ##################################
     */
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        this.filter = new JavaCodeFilter();
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

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        this.filter = null;
    }
    
    /*
     * ##################################
     * #
     * # tests
     * #
     * ##################################
     */
    
    @Test
    public void filterInitialisationTest() 
    {
        testFilter("Hier ist Text CodeFilter filter = new CodeFilter(text);. Hier geht der Text weiter.", 
                "Hier ist Text. Hier geht der Text weiter.");   
        testFilter("char c = s.charAt(0);",
                "");
        testFilter("Hallo int a = 5; weiterer Text",
                "Hallo weiterer Text");
    }
    
    @Test
    public void filterCommonsTest() 
    {
        testFilter("Hier ist Text System.out.println(text);. Hier geht der Text weiter.", 
                "Hier ist Text . Hier geht der Text weiter.");   
        testFilter("Hier ist Text System.out.println(c1==c2);. Hier geht der Text weiter.", 
                "Hier ist Text . Hier geht der Text weiter.");
    }
    
    @Test
    public void filterHeadOfClassTest() 
    {
        testFilter("Hier ist Text abstract class Klasse extends Superklasse implements Interface. Hier geht der Text weiter.",
                "Hier ist Text Klasse. Hier geht der Text weiter.");   
        testFilter("Hier ist Text public class Klasse extends Superklasse implements Interface. Hier geht der Text weiter.",
                "Hier ist Text Klasse. Hier geht der Text weiter.");   
        testFilter("Hier ist Text public class Klasse. Hier geht der Text weiter.",
                "Hier ist Text Klasse. Hier geht der Text weiter.");   
        testFilter("Hier ist Text abstract class Klasse. Hier geht der Text weiter.",
                "Hier ist Text Klasse. Hier geht der Text weiter.");        
    }
    
    @Test
    public void filterPackagesAndImports() 
    {
        testFilter("import de.blum.nlp.dao;",
                "");   
        testFilter(" import de.blum.nlp.dao;",
                "");   
        testFilter("package java.utils.regex;",
                "");   
        testFilter(" package java.utils.regex;",
                "");        
    }

    private void testFilter(String inputContent, String expectedContent)
    {
        // when special posting contents with java code are given
        LmsCourseSet courseSet = givenLmsCourseSetWithPostingContents(inputContent);
        
        // and one template method is called with text
        this.filter.setLmsCourseSet(courseSet);
        this.filter.afterImport();
        
        // then expect the following filtered posting contents
        this.thenAssertPostingContents(courseSet, expectedContent);
    }
   

    

}
