/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.model.tools;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
import de.bht.fb6.s778455.bachelor.model.tools.TopicZoomTagComparator;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains parametrized tests of the {@link TopicZoomTagComparator}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 * 
 */
@RunWith(value = Parameterized.class)
public class TopicZoomTagComparatorTest extends NoLoggingTest {
    protected TopicZoomTagComparator comparator;
    protected TopicZoomTag inputLeftTag;
    protected TopicZoomTag inputRightTag;
    protected int expectedValue;

    public TopicZoomTagComparatorTest(TopicZoomTag inputLeftTag,
            TopicZoomTag inputRightTag, int expectedValue) {
        this.inputLeftTag = inputLeftTag;
        this.inputRightTag = inputRightTag;
        this.expectedValue = expectedValue;
    }

    // @formatter:off
    /*
     * ################################## 
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
    public void setUpBeforeClass() throws Exception {
        this.comparator = new TopicZoomTagComparator();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        this.comparator = null;
    }

    // @formatter:off
    /*
     * ################################## 
     * #
     * # data providers 
     * #
     * ##################################
     */
    // @formatter:on
    @Parameters
    public static Collection<Object[]> data() {
        // [inputLeftTag] , [inputRightTag], [expectedValue]
        // first row: weight and significance of the left tag are less
        Object[] firstRow = new Object[] {
                new TopicZoomTag(1, 0, 1, "leftTag", "test.tld"),
                new TopicZoomTag(6, 0, 2, "rightTag", "test.tld"), -1 };
        // second row: weight of the left is lower, significance is equal
        Object[] secondRow = new Object[] {
                new TopicZoomTag(1, 0, 1, "leftTag", "test.tld"),
                new TopicZoomTag(1, 0, 2, "rightTag", "test.tld"), -1 };
        // third row: weight of the left is lower, signifcance higher
        Object[] thirdRow = new Object[] {
                new TopicZoomTag(6, 0, 1, "leftTag", "test.tld"),
                new TopicZoomTag(2, 0, 2, "rightTag", "test.tld"), 0 };
        // fourth row: weight of the left is equal, significance is lower
        Object[] fourthRow = new Object[] {
                new TopicZoomTag(2, 0, 2, "leftTag", "test.tld"),
                new TopicZoomTag(6, 0, 2, "rightTag", "test.tld"), -1 };
        // fifth row: weight and significance are equal
        Object[] fivthRow = new Object[] {
                new TopicZoomTag(2, 0, 1, "leftTag", "test.tld"),
                new TopicZoomTag(2, 0, 1, "rightTag", "test.tld"), 0 };
        // sixth row: weight is equal, significance of the left higher
        Object[] sixthRow = new Object[] {
                new TopicZoomTag(6, 0, 1, "leftTag", "test.tld"),
                new TopicZoomTag(2, 0, 1, "rightTag", "test.tld"), 1 };
        // seventh row: weight of the left is higher, significance lower
        Object[] seventhRow = new Object[] {
                new TopicZoomTag(2, 0, 2, "leftTag", "test.tld"),
                new TopicZoomTag(6, 0, 1, "rightTag", "test.tld"), -1 };
        // eighth row: weight of the left is higher, signifcance is equal
        Object[] eightRow = new Object[] {
                new TopicZoomTag(6, 0, 2, "leftTag", "test.tld"),
                new TopicZoomTag(6, 0, 1, "rightTag", "test.tld"), 0 };
        // ninth row: weight and significance are higher
        Object[] ninthRow = new Object[] {
                new TopicZoomTag(6, 0, 1, "leftTag", "test.tld"),
                new TopicZoomTag(3, 0, 0, "rightTag", "test.tld"), 1 };

        Object[][] dataProvider = new Object[][] { firstRow, secondRow,
                thirdRow, fourthRow, fivthRow, sixthRow, seventhRow, eightRow,
                ninthRow, };
        return Arrays.asList(dataProvider);
    }

    // @formatter:off
    /*
     * ################################## 
     * # 
     * # tests 
     * #
     * ##################################
     */
    // @formatter:on
    @Test
    public void testCompare() {
        assertEquals(this.expectedValue,
                this.comparator.compare(this.inputLeftTag, this.inputRightTag));
    }

}
