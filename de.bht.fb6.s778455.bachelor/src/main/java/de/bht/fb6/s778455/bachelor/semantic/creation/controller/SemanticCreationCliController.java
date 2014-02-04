/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.controller;

import java.io.File;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * Command Line Interface controller for the Semantic Creation module.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 * 
 */
public class SemanticCreationCliController {
    protected SemanticCreationController semanticCreationController;
    protected File inputFile;
    protected File outputFile;

    protected AImportStrategy importStrategy;

    protected LmsCourseSet importedCourses;
    private long creationStartTime;
    private long creationStopTime;

    /**
     * Create a new controller instance.
     * 
     * @param finputFile
     *            the input file for the import strategy.
     */
    public SemanticCreationCliController( final File finputFile ) {
        this.prepareStrategies();
        this.prepareFiles( finputFile );
    }

    private void prepareFiles( final File finputFile ) {
        if( null == finputFile || !finputFile.isDirectory() ) {
            throw new IllegalArgumentException(
                    "Input file must be a valid directory!" );
        }

        this.inputFile = finputFile;
    }

    /**
     * Prepare the import and export strategy.
     */
    private void prepareStrategies() {
        this.importStrategy = ServiceFactory.getDirectoryImportStrategy();
    }

    /**
     * Perform the import.
     * 
     * @return
     * @throws GeneralLoggingException
     */
    public boolean performImport() throws GeneralLoggingException {
        if( null != this.importedCourses ) {
            throw new IllegalStateException( "Import was already performed!" );
        }

        System.out.println( "Importing courses..." );

        this.importStrategy.importBoardFromFile( this.inputFile );

        return true;
    }

    public boolean performCreation() {
        if( null == this.importedCourses ) {
            throw new IllegalStateException(
                    "You need to perform the import before the creation!" );
        }

        System.out.println( "Starting creation..." );

        this.creationStartTime = new Date().getTime();
        this.semanticCreationController
                .performSemanticCreation( this.importedCourses );
        this.creationStopTime = new Date().getTime();

        // erase imported coures
        this.importedCourses = null;
        
        return true;
    }

}
