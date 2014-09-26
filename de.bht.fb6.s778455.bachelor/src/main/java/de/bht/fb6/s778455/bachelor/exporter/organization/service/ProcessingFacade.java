/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.organization.service;

import java.io.File;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.ExportMethod;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class ProcessingFacade {

    /**
     * Process the export.
     * 
     * This method should be only used when a special export (beside filesystem) method should be used.
     * 
     * @param exportMethod
     * @param courseSet
     * @param outputFile
     * @throws GeneralLoggingException
     */
    public static void processExport(final ExportMethod exportMethod, LmsCourseSet courseSet, final File outputFile) throws GeneralLoggingException 
    {
        AExportStrategy exportStrategy = StrategyRegistry.getExportStrategy(exportMethod);
        exportStrategy.exportToFile(courseSet, outputFile);
    }
    
    /**
     * @param exportMethod
     * @return
     */
    public static String getStrategyClassName(final ExportMethod exportMethod) {
        AExportStrategy exportStrategy = StrategyRegistry.getExportStrategy(exportMethod);
        return exportStrategy.getClass().getName();
    }

    /**
     * 
     * @param courseSet
     * @param outputFile
     * @throws GeneralLoggingException
     */
    public static void processFileSystemExport(LmsCourseSet courseSet,
            File outputFile) throws GeneralLoggingException {
        processExport(ExportMethod.FILESYSTEM, courseSet, outputFile);        
    }
}
