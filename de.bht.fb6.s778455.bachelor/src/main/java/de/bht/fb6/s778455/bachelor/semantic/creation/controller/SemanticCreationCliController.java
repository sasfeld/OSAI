/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.controller;

import java.io.File;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.GeneralStatisticsBuilder;

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
     * @throws InvalidConfigException 
     */
    public SemanticCreationCliController( final File finputFile ) throws InvalidConfigException {
        this.prepareStrategies();
        this.prepareFiles( finputFile );
        
        this.semanticCreationController = new SemanticCreationController( true );
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

        this.importStrategy.importBoardFromFile( this.inputFile );

        return true;
    }

    /**
     * Perform the chain of creation strategies.
     * 
     * @return
     */
    public boolean performCreation() {
        if( null == this.importedCourses ) {
            throw new IllegalStateException(
                    "You need to perform the import before the creation!" );
        }      

        this.creationStartTime = new Date().getTime();
        this.semanticCreationController
                .performSemanticCreation( this.importedCourses );
        this.creationStopTime = new Date().getTime();

        // erase imported coures
        this.importedCourses = null;

        return true;
    }

    /**
     * Get the URI of the released graph. The URI can be used to access the
     * named graph in the triple store.
     * 
     * @return
     */
    public String getGraphUri() {
        return this.semanticCreationController.getUriForReleasedModel();
    }

    /**
     * Get some statistics from the {@link AStatisticsBuilder}.
     * 
     * @return
     */
    public String getStatistics() {
        final GeneralStatisticsBuilder statBuilder = new GeneralStatisticsBuilder();

        statBuilder
                .buildStatistics( this.importedCourses );

        return statBuilder.getStringRepresentation();
    }

    public static void main( final String[] args ) {
        System.out.println( "..:: Semantic creation tool ::.." );
        System.out.println( "Welcome!" );
        System.out.println( "" );
        System.out.println( "Append --help for a help text." );
        System.out.println( "" );

        // read args
        int ind = 0;
        String inputFile = null;
        ;

        if( 0 == args.length || args[0].equals( "--help" ) ) {
            System.out.println( printHelp() );
            return;
        }

        // read options
        for( final String arg : args ) {
            final String argPrepared = arg.trim().toLowerCase();
            System.out.println( argPrepared );
            if( argPrepared.equals( "-inputfile" ) ) {
                inputFile = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
                        : null;
            }
            ind++;
        }

        // validate
        if( null == inputFile ) {
            System.err
                    .println( "No inputFile given. Please give a fully qualified path after the '-inputFile' key." );
        }

        // instantiate files and controller
        final File finputFile = new File( inputFile );

        if( !finputFile.exists() ) {
            System.err
                    .println( "The given inputFile doesn't exist: "
                            + inputFile
                            + ". Make sure that you appended the correct file and retry." );
        }

        SemanticCreationCliController controller = null;
        try {
            controller = new SemanticCreationCliController( finputFile );

            System.out.println( "Controller is initialized..." );
            System.out.println( "Input file: " + inputFile );

        } catch( final InvalidConfigException e ) {
            System.err.println( "Controller couldn't get initialized. Error: "
                    + e.getLocalizedMessage() );
            return;
        }

        // perform import
        try {
            System.out.println( "Starting import...\n\n" );
            controller.performImport();
        } catch( final GeneralLoggingException e ) {
            System.err.println( "An error occured: " + e.getLocalizedMessage() );
            return;
        }
        System.out.println( "Import was successfull!\n\n" );

        // perform extraction

        // statistics
        System.out.println( controller.getStatistics() );

        System.out.println( "Starting creation...\n\n" );
        controller.performCreation();

        System.out.println( "Creation was successfull!\n\n" );
        
        // elapsed time
        System.out.println( "Elapsed time (s) " + ((controller.creationStopTime - controller.creationStartTime) / 1000));

        // perform export
        System.out.println( "The model was released with the following URI: "
                + controller.getGraphUri() );
        System.out
                .println( "You can access the semantic network using the NAMED GRAPH pattern (see SPARQL commands)." );
        System.out.println( "Goodbye!" );

    }

    private static String printHelp() {
        final StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append( "..:: HELP ::..\n" );
        helpBuilder
                .append( "The semantic creation tool takes the semantically enriched models and adds representations of them in an RDF network.\n" );
        helpBuilder
                .append( "The structure of the file system must follow the one described in the documentation (see FileSystemImportStrategy).\n" );
        helpBuilder
                .append( "In general, the structure was created by the semantic extraction module after it tagged the coures, boards, threads and postings, maybe defined the language and so on.\n" );
        helpBuilder
                .append( "Make sure, that you configured the semantic creation chain in the semantics.properties config file.\n" );
        helpBuilder.append( "\n" );
        helpBuilder.append( "Required arguments:\n\n" );
        helpBuilder.append( "-inputfile [FILE]\n" );
        helpBuilder.append( "\n\n" );

        return helpBuilder.toString();
    }
}
