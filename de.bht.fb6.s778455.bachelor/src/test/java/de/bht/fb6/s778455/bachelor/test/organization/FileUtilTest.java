/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.organization;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.bht.fb6.s778455.bachelor.organization.FileUtil;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class realizes tests of the {@link FileUtil} class.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 * 
 */
public class FileUtilTest extends NoLoggingTest {
    protected static final File TEST_FILE = new File(PATH_UNITTEST_DATA_FOLDER
            + "/organization/fileLineBased.txt");


    @Test
    public void testReadFileLineBased() throws GeneralLoggingException {
        List<String> lines = FileUtil.readFileLineBased(TEST_FILE, "UTF-8");

        assertEquals(3, lines.size());

        assertEquals("First line", lines.get(0));
        assertEquals("Second line", lines.get(1));
        assertEquals("Third line", lines.get(2));
    }

}
