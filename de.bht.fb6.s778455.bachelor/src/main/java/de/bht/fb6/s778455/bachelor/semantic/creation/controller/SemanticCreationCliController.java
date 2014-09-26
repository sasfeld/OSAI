/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.controller;

import java.io.File;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.importer.organization.service.ProcessingFacade;
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
     * Perform the import.
     * 
     * @return
     * @throws GeneralLoggingException
     */
    public boolean performImport() throws GeneralLoggingException {
        if( null != this.importedCourses ) {
            throw new IllegalStateException( "Import was already performed!" );
        }      

        this.importedCourses = ProcessingFacade.importFromFileSystem(this.inputFile);

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
        if ( null == this.importedCourses ) {
            throw new IllegalStateException( "You need to perform creation before!" );
        }
        
        final GeneralStatisticsBuilder statBuilder = new GeneralStatisticsBuilder();

        statBuilder
                .buildStatistics( this.importedCourses );

        return statBuilder.getStringRepresentation();
    }

    // ############################################
    // static methods
    // ############################################
    public static void main( final String[] args ) {
        showWelcomeText();

        // read args
        int ind = 0;
        String inputFile = null;        

        if (printHelpIfConfigured(args)) {
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
        if (!validateParameters(inputFile)) {
            return;
        }

        // instantiate files and controller
        final File finputFile = validateInputFile(inputFile);
        
        if (null == finputFile) {
            return;
        }

        SemanticCreationCliController controller = initializeController(
                inputFile, finputFile);
        
        if (null == controller) {
            return;
        }

        // perform import
        if (!performImport(controller)) {
            return;
        }
        
        // statistics
        showImportStatistics(controller);

        // perform creation
        if (!performCreation(controller)) {
            return;
        }
        
        showFinalStatistics(controller);

        // perform export
        showGoodbyeMessage(controller);

    }

    protected static void showGoodbyeMessage(
            SemanticCreationCliController controller) {
        System.out.println( "The model was released with the following URI: "
                + controller.getGraphUri() );
        System.out
                .println( "You can access the semantic network using the NAMED GRAPH pattern (see SPARQL commands)." );
        System.out.println( "Goodbye!" );
    }

    protected static void showFinalStatistics(
            SemanticCreationCliController controller) {
        // elapsed time
        System.out.println( "Elapsed time (s) " + ((controller.creationStopTime - controller.creationStartTime) / 1000));
    }

    protected static void showImportStatistics(
            SemanticCreationCliController controller) {
        System.out.println( controller.getStatistics() );
    }

    protected static boolean performCreation(
            SemanticCreationCliController controller) {
        try {
            System.out.println( "Starting semantic creation...\n\n" );
            controller.performCreation();
        } catch( final Exception e ) {
            System.err.println( "An error occured: " + e.getLocalizedMessage() );
            return false;
        }
        System.out.println( "Creation was successfull!\n\n" );
        
        return true;

    }

    /**
     * 
     * @param controller
     */
    protected static boolean performImport(SemanticCreationCliController controller) {
        try {
            System.out.println( "Starting import...\n\n" );
            controller.performImport();
        } catch( final GeneralLoggingException e ) {
            System.err.println( "An error occured: " + e.getLocalizedMessage() );
            return false;
        }
        System.out.println( "Import was successfull!\n\n" );
        
        return true;
    }

    protected static SemanticCreationCliController initializeController(
            String inputFile, final File finputFile) {
        SemanticCreationCliController controller = null;
        try {
            controller = new SemanticCreationCliController( finputFile );

            System.out.println( "Controller is initialized..." );
            System.out.println( "Input file: " + inputFile );

        } catch( final InvalidConfigException e ) {
            System.err.println( "Controller couldn't get initialized. Error: "
                    + e.getLocalizedMessage() );
            return null;
        }
        return controller;
    }

    protected static File validateInputFile(String inputFile) {
        final File finputFile = new File( inputFile );

        if( !finputFile.exists() ) {
            System.err
                    .println( "The given inputFile doesn't exist: "
                            + inputFile
                            + ". Make sure that you appended the correct file and retry." );
            return null;
        }
        return finputFile;
    }

    protected static boolean validateParameters(String inputFile) {
        if( null == inputFile ) {
            System.err
                    .println( "No inputFile given. Please give a fully qualified path after the '-inputFile' key." );
            return false;
        }
        
        return true;
    }

    protected static boolean printHelpIfConfigured(final String[] args) {
        if( 0 == args.length || args[0].equals( "--help" ) ) {
            System.out.println( printHelp() );
            return true;
        }
        
        return false;
    }

    protected static void showWelcomeText() {
        System.out.println( "..:: Semantic creation tool ::.." );
        System.out.println( "Welcome!" );
        System.out.println( "" );
        System.out.println( "Append --help for a help text." );
        System.out.println( "" );
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
