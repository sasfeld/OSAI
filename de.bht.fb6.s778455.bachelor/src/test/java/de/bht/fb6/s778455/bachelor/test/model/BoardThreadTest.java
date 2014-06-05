/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class defines tests of the {@link BoardThread} model.
 * </p>
 * 
 * <p>
 * Especially the sorting algorithm shall be tested here.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
@RunWith(value = Parameterized.class)
public class BoardThreadTest extends NoLoggingTest {
    protected BoardThread boardThread;
    private List<Posting> inputList;
    private List<Posting> outputList;

    // @formatter:off
	/*
	 * ################################## 
	 * # 
	 * #       test preparation 
	 * #
	 * ##################################
	 */
	// @formatter:on
    public BoardThreadTest(List<Posting> inputList, List<Posting> outputList) {
        this.inputList = inputList;
        this.outputList = outputList;
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.boardThread = new BoardThread();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.boardThread = null;
    }

    // @formatter:off
        /*
         * ################################## 
         * # 
         * #       data provider
         * #
         * ##################################
         */
        // @formatter:on
    @Parameters
    public static Collection<Object[]> data() {
        /*
         * ######################### # # input values #
         * #########################
         */
        List<Posting> inputList = new ArrayList<Posting>();
        Posting posting = new Posting();
        // creation date: 20.11.2013 - 16:26:30
        posting.setCreationDate(new Date(1384964790));
        inputList.add(posting);
        // creation date: 20.11.2013 - 08:26:30
        posting = new Posting();
        posting.setCreationDate(new Date(1384935990));
        inputList.add(posting);
        // creation date: 06.11.2013 - 10:12:17
        posting = new Posting();
        posting.setCreationDate(new Date(1383732737));
        inputList.add(posting);
        // creation date: 06.11.2013 - 09:19:01
        posting = new Posting();
        posting.setCreationDate(new Date(1383729541));
        inputList.add(posting);
        // creation date: 10.11.2013 - 14:19:01
        posting = new Posting();
        posting.setCreationDate(new Date(1384093141));
        inputList.add(posting);

        /*
         * ######################### # # expected values #
         * #########################
         */
        List<Posting> sortedOutputList = new ArrayList<Posting>();
        posting = new Posting();
        // creation date: 06.11.2013 - 09:19:01
        posting.setCreationDate(new Date(1383729541));
        sortedOutputList.add(posting);
        // creation date: 06.11.2013 - 10:12:17
        posting = new Posting();
        posting.setCreationDate(new Date(1383732737));
        sortedOutputList.add(posting);
        // creation date: 10.11.2013 - 14:19:01
        posting = new Posting();
        posting.setCreationDate(new Date(1384093141));
        sortedOutputList.add(posting);
        // creation date: 20.11.2013 - 08:26:30
        posting = new Posting();
        posting.setCreationDate(new Date(1384935990));
        sortedOutputList.add(posting);
        // creation date: 20.11.2013 - 16:26:30
        posting = new Posting();
        posting.setCreationDate(new Date(1384964790));
        sortedOutputList.add(posting);

        Object[][] dataProvider = new Object[][] {
        // [inputValue] , [expectedValue]
        { inputList, sortedOutputList } };
        return Arrays.asList(dataProvider);
    }

    // @formatter:off
        /*
         * ################################## 
         * # 
         * #       tests
         * #
         * ##################################
         */
        // @formatter:on

    @Test
    /**
     * Test of the method:
     * @see de.bht.fb6.s778455.bachelor.model.BoardThread#addPosting()
     */
    public void testAddPosting() {
        // add postings first
        for (Posting posting : this.inputList) {
            this.boardThread.addPosting(posting);
        }

        // now check if the sorting matches the expectation
        List<Posting> resultingPostings = this.boardThread.getPostings();
        for (int i = 0; i < this.inputList.size(); i++) {
            assertEquals(this.outputList.get(i), resultingPostings.get(i));
        }
    }

}
