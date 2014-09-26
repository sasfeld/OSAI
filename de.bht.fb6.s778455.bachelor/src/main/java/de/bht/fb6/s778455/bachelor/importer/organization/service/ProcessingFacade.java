/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de)  
 */
package de.bht.fb6.s778455.bachelor.importer.organization.service;

import java.io.File;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.ImportMethod;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This facade class is the central facade for accessing the process that this module is designed for.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class ProcessingFacade {       
    
    /** 
     * @param importMethod
     * @param inputFile
     * @return
     * 
     * @throws GeneralLoggingException 
     * @throws NullPointerException
     */
    public static LmsCourseSet processImport(final ImportMethod importMethod, final File inputFile) throws GeneralLoggingException 
    {
        AImportStrategy strategy = StrategyRegistry.getImportStrategy(importMethod);
        return strategy.importBoardFromFile(inputFile);       
    }
    
    /**
     * 
     * @param importMethod
     * @return
     */
    public static String getStrategyClassName(final ImportMethod importMethod)
    {
        AImportStrategy strategy = StrategyRegistry.getImportStrategy(importMethod);
        return strategy.getClass().getName();
    }
}
