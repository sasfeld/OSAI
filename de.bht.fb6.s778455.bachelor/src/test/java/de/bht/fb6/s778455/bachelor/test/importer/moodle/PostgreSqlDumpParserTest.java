/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.moodle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.moodle.PostgreSqlDumpParser;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link PostgreSqlDumpParser}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 11.12.2013
 * 
 */
public class PostgreSqlDumpParserTest extends NoLoggingTest {
    protected static final File TEST_FILE = new File(PATH_UNITTEST_DATA_FOLDER
            + "/importer/moodle/postgredump/mdl_course.sql");

    @Test
    public void testFetchEntities() {
        PostgreSqlDumpParser parser = new PostgreSqlDumpParser(TEST_FILE);

        List<Map<String, String>> entities = parser.fetchEntities("mdl_course",
                "completionnotify", "id", "fullname", "shortname", "summary");

        System.out.println("resulting:\n" + entities);
        assertEquals(2, entities.size());

        // assert that each fetched entity contains the required columns
        for (Map<String, String> entity : entities) {
            assertTrue(entity.containsKey("id"));
            assertTrue(entity.containsKey("fullname"));
            assertTrue(entity.containsKey("shortname"));
            assertTrue(entity.containsKey("summary"));
        }
    }

}
