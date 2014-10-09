/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.test.exporter.organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import de.bht.fb6.s778455.bachelor.exporter.ExportMethod;
import de.bht.fb6.s778455.bachelor.exporter.experimental.DirectoryExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.organization.service.ExportProcessingFacade;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;
import de.bht.fb6.s778455.bachelor.test.framework.service.ImportFactory;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class ProcessingFacadeTest extends NoLoggingTest {
    protected static final File EXPORT_FOLDER = new File(
            PATH_UNITTEST_DATA_FOLDER + "/exporter/file_system_test");

    @Test
    /**
     * Also tests processExport().
     */
    public void testProcessFileSystemExport() throws GeneralLoggingException {
       ExportProcessingFacade.processFileSystemExport(ImportFactory.newDummyCourseSet(), EXPORT_FOLDER);
       
       assertTrue(EXPORT_FOLDER.listFiles().length > 0);
    }
    
    @Test
    public void testGestStrategyName() 
    {
        // DirectoryExportStrategy
        String resultName = ExportProcessingFacade.getStrategyClassName(ExportMethod.FILESYSTEM);        
        assertEquals(DirectoryExportStrategy.class.getName(), resultName);   
        
    }

}
