/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.controller;

import java.io.File;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ImportProcessingFacade;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.GeneralStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.LanguageStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.TagStatisticsBuilder;

/**
 * <p>
 * This class defines a controller to be runned from a command line interface.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 * 
 */
public class SemanticExtractionCliController {
    protected SemanticExtractionController semanticExtractionController;
    protected File inputFile;
    protected File outputFile;

    protected LmsCourseSet rawCourses;
    protected LmsCourseSet enrichedCourses;

    protected int numberImportedCourses;
    protected long extractionStartTime;
    protected long extractionStopTime;
    private final boolean statisticsMode;

    public SemanticExtractionCliController( final File finputFile,
            final File foutputFile, final boolean statisticsMode )
            throws InvalidConfigException {
        if( !statisticsMode ) {
            this.semanticExtractionController = new SemanticExtractionController(
                    true );
        }

        this.statisticsMode = statisticsMode;

        this.inputFile = finputFile;
        this.outputFile = foutputFile;
    }

    /**
     * Perform the import delegation to the given import strategy.
     * 
     * @return true on success
     * @throws GeneralLoggingException
     */
    public boolean performImport() throws GeneralLoggingException {
        this.rawCourses = ImportProcessingFacade.importFromFileSystem(this.inputFile);
        this.numberImportedCourses = this.rawCourses.size();

        return true;
    }

    /**
     * Perform the semantic extraction.
     * 
     * @return
     * @throws InvalidConfigException
     */
    public boolean performExtraction() throws InvalidConfigException {
        if( null == this.rawCourses || this.statisticsMode ) {
            throw new IllegalStateException(
                    "performAnonymization() must be called after performImport()! Maybe the import wasn't succesful." );
        }

        this.extractionStartTime = new Date().getTime();
        this.enrichedCourses = this.semanticExtractionController
                .performSemanticExtraction( this.rawCourses );
        this.extractionStopTime = new Date().getTime();

        // really erase the raw material
        this.rawCourses = null;

        return true;
    }

    /**
     * Perform the export on the given {@link AExportStrategy}.
     * 
     * @return
     * @throws GeneralLoggingException
     */
    public boolean performExport() throws GeneralLoggingException {
        if( null == this.enrichedCourses || this.statisticsMode ) {
            throw new IllegalStateException(
                    "performExport() must be called after performAnonymization()! Maybe the anonymization wasn't succesful." );
        }

        de.bht.fb6.s778455.bachelor.exporter.organization.service.ExportProcessingFacade.processFileSystemExport(this.enrichedCourses, this.outputFile);

        return true;
    }

    /**
     * Get a string containing statistics.
     * 
     * @return
     */
    public String getStatistics( final boolean showTime ) {
        final StringBuilder statisticsBuilder = new StringBuilder();

        if( showTime ) {
            final long elapsedTime = this.extractionStopTime
                    - this.extractionStartTime;
            statisticsBuilder.append( "Elapsed time (seconds): " + elapsedTime );
        }

        if( !this.statisticsMode && null != this.rawCourses ) {
            statisticsBuilder.append( this.semanticExtractionController
                    .getStatistics( this.rawCourses ) );
        }
        return statisticsBuilder.toString();
    }

    /**
     * Get statistics about enriched tags. This method will be perfomed on the
     * given collection of {@link Course}.
     * 
     * @return
     */
    public String getTagAndLangStatistics( final LmsCourseSet collection ) {
        final AStatisticsBuilder builder = new GeneralStatisticsBuilder(
                new TagStatisticsBuilder( new LanguageStatisticsBuilder() ) );

        return builder.buildStatistics( collection ).toString();
    }

    public static void main( final String[] args ) {
        showWelcomeText();

        // read args
        int ind = 0;
        String inputFile = null;
        String outputFile = null;

        if( 0 == args.length || args[0].equals( "--help" ) ) {
            System.out.println( printHelp() );
            return;
        }

        boolean statisticsMode = false;
        String forceLang = "";
        // read options
        for( final String arg : args ) {
            final String argPrepared = arg.trim().toLowerCase();
            System.out.println( argPrepared );
            if( argPrepared.equals( "-inputfile" ) ) {
                inputFile = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
                        : null;
            } else if( argPrepared.equals( "-outputfile" ) ) {
                outputFile = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
                        : null;
            } else if( argPrepared.equals( "--statistics" ) ) {
                statisticsMode = true;
            } else if( argPrepared.equals( "-forcelang" ) ) {
                forceLang = ( ( ind + 1 ) < args.length ) ? args[ind + 1] : "";
            }
            ind++;
        }

        // validate
        if (!validateInputParameters(inputFile, outputFile)) {
            return;
        }

        // instantiate files and controller
        final File finputFile = new File( inputFile );
        final File foutputFile = new File( outputFile );

        if (!validateFiles(inputFile, outputFile, finputFile, foutputFile)) {
            return;
        }

        final String lang = validateLanguage(forceLang);
        
        if (null == lang) {
            return;
        }

        SemanticExtractionCliController controller = initializeController(
                inputFile, outputFile, statisticsMode, finputFile, foutputFile,
                lang);
        
        if (null == controller) {
            return;
        }

        // perform import
        if (!performImport(controller)) {
            return;
        }

        // perform extraction
        if( !statisticsMode ) {
            if (!performExtraction(controller)) {
                return;
            }

            if (!performExport(controller)) {
                return;
            }
        } else {
            showIncreasedStatistics(controller);
        }

        showFinalStatisticsAndGoodbye(controller);
    }

    protected static void showFinalStatisticsAndGoodbye(
            SemanticExtractionCliController controller) {
        System.out.println( controller.getStatistics( true ) );
        System.out
                .println( "See the posting files saved in your outputFolder. The postings are now enriched with tags and stuff.\n\n" );
        System.out.println( "Goodbye :)" );
    }

    protected static void showIncreasedStatistics(
            SemanticExtractionCliController controller) {
        System.out.println( controller
                .getTagAndLangStatistics( controller.rawCourses ) );
    }

    protected static boolean performExport(
            SemanticExtractionCliController controller) {
        // perform export
        try {
            System.out.println( "Starting export..." );
            controller.performExport();
        } catch( final GeneralLoggingException e ) {
            System.err.println( "An error occured: "
                    + e.getLocalizedMessage() );
            return false;
        }
        System.out.println( "Export was successfull!\n\n" );
        return true;
    }

    protected static boolean performExtraction(
            SemanticExtractionCliController controller) {
        try {
            // statistics
            System.out.println( controller.getStatistics( false ) );

            System.out.println( "Starting extraction...\n\n" );
            controller.performExtraction();
        } catch( final GeneralLoggingException e ) {
            System.err.println( "An error occured: "
                    + e.getLocalizedMessage() );
            return false;
        }
        System.out.println( "Extraction was successfull!\n\n" );
        return true;
    }

    protected static boolean performImport(
            SemanticExtractionCliController controller) {
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

    protected static SemanticExtractionCliController initializeController(
            String inputFile, String outputFile, boolean statisticsMode,
            final File finputFile, final File foutputFile, final String lang) {
        SemanticExtractionCliController controller = null;
        try {
            controller = new SemanticExtractionCliController( finputFile,
                    foutputFile, statisticsMode );

            if( !lang.equals( "" ) ) {
                controller.setForcedLanguage( Language.getFromString( lang ) );
            }
            System.out.println( "Controller is initialized..." );
            System.out.println( "Input file: " + inputFile );
            System.out.println( "Output file: " + outputFile );
            System.out.println( "Statistics mode: "
                    + ( statisticsMode ? "enabled" : "disabled" ) );

        } catch( final InvalidConfigException e ) {
            System.err.println( "Controller couldn't get initialized. Error: "
                    + e.getLocalizedMessage() );
            return null;
        }
        return controller;
    }

    protected static String validateLanguage(String forceLang) {
        final String lang = forceLang.trim().toLowerCase();
        if( !lang.equals( "" ) && null == Language.getFromString( lang ) ) {
            System.err.println( "The given language doesn't exist: " + lang
                    + ". Make sure to use one of these options: "
                    + Language.values() );
            return null;
        }
        return lang;
    }

    protected static boolean validateFiles(String inputFile, String outputFile,
            final File finputFile, final File foutputFile) {
        if( !finputFile.exists() ) {
            System.err
                    .println( "The given inputFile doesn't exist: "
                            + inputFile
                            + ". Make sure that you appended the correct file and retry." );
            return false;
        } else if( !foutputFile.exists() ) {
            System.err
                    .println( "The given outputFile doesn't exist: "
                            + outputFile
                            + ". Make sure that you appended the correct file and retry." );
            return false;
        }
        return true;
    }

    protected static boolean validateInputParameters(String inputFile,
            String outputFile) {
        if( null == inputFile ) {
            System.err
                    .println( "No inputFile given. Please give a fully qualified path after the '-inputFile' key." );
            return false;
        } else if( null == outputFile ) {
            System.err
                    .println( "No outputtFile given. Please give a fully qualified path after the '-outputFile' key." );
            return false;
        }
        
        return true;
    }

    protected static void showWelcomeText() {
        System.out.println( "..:: Semantic extraction tool ::.." );
        System.out.println( "Welcome!" );
        System.out.println( "" );
        System.out.println( "Append --help for a help text." );
        System.out.println( "" );
    }

    private void setForcedLanguage( final Language valueOf ) {
        this.semanticExtractionController.setForcedLanguage( valueOf );
    }

    private static String printHelp() {
        final StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append( "..:: HELP ::..\n" );
        helpBuilder
                .append( "The semantic extraction tool takes a folder containing e learning courses and their boards, threads and postings to enrich them semantical.\n" );
        helpBuilder
                .append( "The structure of the file system must follow the one described in the documentation (see FileSystemImportStrategy).\n" );
        helpBuilder
                .append( "In general, the structure was created by the anonymization module after it anonymized postings from different sources.\n" );
        helpBuilder
                .append( "Make sure, that you configured the semantic extraction chain in the semantics.properties config file.\n" );
        helpBuilder.append( "\n" );
        helpBuilder.append( "Required arguments:\n\n" );
        helpBuilder.append( "-inputfile [FILE]\n" );
        helpBuilder.append( "-outputfile [FILE]\n" );
        helpBuilder.append( "[-forcelang [english,german]]\n" );
        helpBuilder.append( "\n\n" );
        helpBuilder
                .append( "Use the -forcelang option carefully! If the language of the material is really unknown and can't be detected automatically, but you know the exact language, than you should use this option." );
        helpBuilder
                .append( "Append --statistics if you want an overview statistics about the number of tags in relation to the number of postings." );

        return helpBuilder.toString();
    }
}
