/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.organization;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.ImportMethod;
import de.bht.fb6.s778455.bachelor.importer.auditorium.AuditoriumImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodlePostgreSqlImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodleXmlImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.OliverLuebeckStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ImportProcessingFacade;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class ProcessingFacadeTest extends NoLoggingTest {
    protected static final File IMPORT_FOLDER = new File(
            PATH_UNITTEST_DATA_FOLDER + "/importer/file_system_test");
    
    @Test
    /**
     * Also tests ProcessingFacade::processImport.
     * @throws GeneralLoggingException
     */
    public void testProcessImport() throws GeneralLoggingException 
    {
        LmsCourseSet courseSet = ImportProcessingFacade.importFromFileSystem(IMPORT_FOLDER);
        
        // only make sure that the course set is filled
        assertTrue(courseSet.size() > 0);
        
        // the correct functionality of the DirectoryImportStrategy is tested in a separate unit test
    }
    
    @Test
    public void testGestStrategyName() 
    {
        // DirectoryImportStrategy
        String resultName = ImportProcessingFacade.getStrategyClassName(ImportMethod.FILESYSTEM);        
        assertEquals(DirectoryImportStrategy.class.getName(), resultName);
        
        // AuditoriumImportStrategy
        resultName = ImportProcessingFacade.getStrategyClassName(ImportMethod.AUDITORIUM_DB);        
        assertEquals(AuditoriumImportStrategy.class.getName(), resultName);        
        
        // MoodleXmlImportStrategy
        resultName = ImportProcessingFacade.getStrategyClassName(ImportMethod.LUEBECK_XML);        
        assertEquals(MoodleXmlImportStrategy.class.getName(), resultName);
        
        // OliverLuebeckStrategy
        resultName = ImportProcessingFacade.getStrategyClassName(ImportMethod.OLIVER_LUEBECK_XML);        
        assertEquals(OliverLuebeckStrategy.class.getName(), resultName);
        
        // MoodlePostgreSqlImportStrategy
        resultName = ImportProcessingFacade.getStrategyClassName(ImportMethod.POSTGREDUMP);        
        assertEquals(MoodlePostgreSqlImportStrategy.class.getName(), resultName);      
        
    }

}
