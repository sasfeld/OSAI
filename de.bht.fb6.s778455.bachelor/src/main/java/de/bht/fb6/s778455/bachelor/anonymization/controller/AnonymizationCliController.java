/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.controller;

import java.io.File;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.ExportMethod;
import de.bht.fb6.s778455.bachelor.importer.ImportMethod;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ProcessingFacade;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * <p>
 * This class offers a controller for use in command line interfaces (CLI).
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.12.2013
 * 
 */
public class AnonymizationCliController {
    private File inputFile;
    private File outputFile;
    private AnonymizationController anonymizationController;
    private LmsCourseSet rawCourses;
    private LmsCourseSet anonymizedCourses;
    private int numberImportedCourses;
    private int numberAnonymizedCourses;
    private long anonymizationStartTime;
    private long anonymizationStopTime;
    private ImportMethod importMethod;
    private ExportMethod exportMethod;

    /**
     * Create a new anonymization cli controller.
     * 
     * @param inputFile
     * @param outputFile
     * @param importMethod
     * @param exportMethod
     * @throws InvalidConfigException
     * @throws IllegalArgumentException
     * @throws {@link UnsupportedOperationException} when giving unsupported
     *         methods.
     */
    public AnonymizationCliController(final File inputFile,
            final File outputFile, final ImportMethod importMethod,
            final ExportMethod exportMethod) throws InvalidConfigException {
        setAttributes(inputFile, outputFile, importMethod, exportMethod);
        initializeAnonymizationController();
    }

    /**
     * 
     * @throws InvalidConfigException
     */
    protected void initializeAnonymizationController()
            throws InvalidConfigException {
        this.anonymizationController = new AnonymizationController();
        this.anonymizationController.setImportFile(this.inputFile);
        this.anonymizationController.setExportFile(this.outputFile);
        this.anonymizationController.setImportMethod(this.importMethod);
        this.anonymizationController.setExportMethod(this.exportMethod);
    }

    /**
     * 
     * @param inputFile
     * @param outputFile
     * @param importMethod
     * @param exportMethod
     */
    protected void setAttributes(final File inputFile, final File outputFile,
            final ImportMethod importMethod, final ExportMethod exportMethod) {
        if (null == inputFile || !inputFile.exists()) {
            throw new IllegalArgumentException(
                    "Illegal argument for inputFile: " + inputFile);
        }
        if (null == outputFile) {
            throw new IllegalArgumentException(
                    "Illegal argument for outputFile: " + outputFile);
        }

        // store files
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        this.importMethod = importMethod;
        this.exportMethod = exportMethod;
    }

    /**
     * Perform the import delegation to the given import strategy.
     * 
     * @return true on success
     * @throws GeneralLoggingException
     */
    public boolean performImport() throws GeneralLoggingException {
        this.rawCourses = ProcessingFacade.processImport(this.importMethod,
                this.inputFile);
        this.numberImportedCourses = this.rawCourses.size();

        return true;
    }

    /**
     * Perform the anonymization delegating to the
     * {@link AnonymizationController}.
     * 
     * @return
     * @throws GeneralLoggingException
     */
    public boolean performAnonymization() throws GeneralLoggingException {
        if (null == this.rawCourses) {
            throw new IllegalStateException(
                    "performAnonymization() must be called after performImport()! Maybe the import wasn't succesful.");
        }

        this.anonymizationStartTime = new Date().getTime();
        this.anonymizedCourses = this.anonymizationController
                .performAnonymization(this.rawCourses);
        this.anonymizationStopTime = new Date().getTime();

        // really erase the raw material
        this.rawCourses = null;

        this.numberAnonymizedCourses = this.anonymizedCourses.size();

        return true;
    }

    /**
     * Perform the export on the given {@link AExportStrategy}.
     * 
     * @return
     * @throws GeneralLoggingException
     */
    public boolean performExport() throws GeneralLoggingException {
        if (null == this.anonymizedCourses) {
            throw new IllegalStateException(
                    "performExport() must be called after performAnonymization()! Maybe the anonymization wasn't succesful.");
        }

        de.bht.fb6.s778455.bachelor.exporter.organization.service.ProcessingFacade
                .processExport(this.exportMethod, this.anonymizedCourses,
                        this.outputFile);

        return true;
    }

    /**
     * Get a string containing statistics.
     * 
     * @return
     */
    public String getStatistics() {
        final StringBuilder statisticsBuilder = new StringBuilder();

        statisticsBuilder.append("Number of imported courses: "
                + this.numberImportedCourses + "\n");
        statisticsBuilder.append("Number of anonymized courses: "
                + this.numberAnonymizedCourses + "\n");

        final long elapsedTime = this.anonymizationStopTime
                - this.anonymizationStartTime;
        statisticsBuilder.append(this.anonymizationController.getStatistics(
                this.anonymizedCourses, elapsedTime));

        return statisticsBuilder.toString();
    }

    
    // ############################################
    // static methods
    // ############################################
    public static void main(final String[] args) {
        showWelcome();

        // read args
        int ind = 0;
        String inputFile = null;
        String outputFile = null;
        String importMethodString = null;
        String exportMethodString = null;

        // show help
        if (printHelpIfConfigured(args)) {
            return;
        }

        // read options
        for (final String arg : args) {
            final String argPrepared = arg.trim().toLowerCase();
            System.out.println(argPrepared);
            if (argPrepared.equals("-inputfile")) {
                inputFile = ((ind + 1) < args.length) ? args[ind + 1] : null;
            } else if (argPrepared.equals("-outputfile")) {
                outputFile = ((ind + 1) < args.length) ? args[ind + 1] : null;
            } else if (argPrepared.equals("-importmethod")) {
                importMethodString = ((ind + 1) < args.length) ? args[ind + 1]
                        : null;
            } else if (argPrepared.equals("-exportmethod")) {
                exportMethodString = ((ind + 1) < args.length) ? args[ind + 1]
                        : null;
            }
            ind++;
        }

        if (!validateInputParameters(inputFile, outputFile, importMethodString,
                exportMethodString)) {
            return;
        }

        // check given methods
        ImportMethod importMethod = validateAndReturnImportMethod(importMethodString);

        if (null == importMethod) {
            return;
        }

        ExportMethod exportMethod = validateAndReturnExportMethod(exportMethodString);

        if (null == exportMethod) {
            return;
        }

        // initialize controller
        AnonymizationCliController controller = initializeController(inputFile,
                outputFile, importMethod, exportMethod);
        
        if (null == controller) {
            return;
        }

        // import
        if (!performImport(controller)) {
            return;
        }

        // perform anonymization
        if (!performAnonymization(controller)) {
            return;
        }

        // perform export
        if (!performExport(controller)) {
            return;
        }

        // Show stats
        showStatistics(controller);
    }

    protected static void showWelcome() {
        System.out.println("..:: Moodle anonymization tool ::..");
        System.out.println("Welcome!");
        System.out.println("");
        System.out.println("Append --help for a help text.");
        System.out.println("");
    }

    /**
     * 
     * @param controller
     */
    protected static void showStatistics(AnonymizationCliController controller) {
        System.out.println(controller.getStatistics());

        System.out.println("Goodybe :)");
    }
    
    /**
     * @param controller
     */
    protected static boolean performExport(AnonymizationCliController controller) {
        boolean exportSuccess = false;

        try {
            exportSuccess = controller.performExport();
        } catch (final GeneralLoggingException e) {
            exportSuccess = false;
            System.err.println("Export wasn't succesful. Error: "
                    + e.getLocalizedMessage());
        }

        if (!exportSuccess) {
            System.err.println("Export wasn't successful.");
            return false;
        }
        System.out.println("Export was successful!");
        System.out.println();
        return true;
    }

    /**
     * 
     * @param controller
     */
    protected static boolean performAnonymization(
            AnonymizationCliController controller) {
        boolean anonymSuccess = false;

        try {
            anonymSuccess = controller.performAnonymization();
        } catch (final GeneralLoggingException e) {
            anonymSuccess = false;
            System.err.println("Anonymization wasn't succesful. Error: "
                    + e.getLocalizedMessage());
        }

        if (!anonymSuccess) {
            System.err.println("Anonymization wasn't successful.");
            return false;
        }
        System.out.println("Anonymization was successful!");
        
        return true;
    }

    /**
     * 
     * @param controller
     */
    protected static boolean performImport(AnonymizationCliController controller) {
        // perform import
        System.out.println("Starting import...");
        boolean importSuccess = false;
        try {
            importSuccess = controller.performImport();
        } catch (final GeneralLoggingException e) {
            importSuccess = false;
            System.err.println("Import wasn't successful. Error: "
                    + e.getLocalizedMessage());
            // handled below again
        }

        if (!importSuccess) {
            System.err.println("Import wasn't successful.");
            return false;
        }
        System.out.println("Import was successful!");
        
        return true;
    }

    /**
     * 
     * @param inputFile
     * @param outputFile
     * @param importMethod
     * @param exportMethod
     * @return
     */
    protected static AnonymizationCliController initializeController(
            String inputFile, String outputFile, ImportMethod importMethod,
            ExportMethod exportMethod) {
        // initialize controller
        AnonymizationCliController controller = null;
        try {
            controller = new AnonymizationCliController(new File(inputFile),
                    new File(outputFile), importMethod, exportMethod);

            System.out.println("Controller is initialized...");
            System.out.println("Input file: " + inputFile);
            System.out.println("Output file: " + outputFile);
            System.out
                    .println("Import method: "
                            + importMethod
                            + (importMethod.equals(ImportMethod.AUDITORIUM_DB) ? "Make sure that you have configured the database connection in the anonymization.properties!"
                                    : ""));
            System.out.println("Export method: " + exportMethod);
            System.out.println();
        } catch (UnsupportedOperationException | IllegalArgumentException
                | InvalidConfigException e) {
            System.err.println("Controller couldn't get initialized. Error: "
                    + e.getLocalizedMessage());
            return null;
        }
        return controller;
    }

    /**
     * 
     * @param exportMethodString
     * @return
     */
    protected static ExportMethod validateAndReturnExportMethod(
            String exportMethodString) {
        ExportMethod exportMethod = null;
        try {
            exportMethod = ExportMethod.mapInputParameter(exportMethodString);
        } catch (final GeneralLoggingException e) {
            System.err
                    .println("Wrong export method given. Allowed values are: 'fileystem'");
            return null;
        }
        return exportMethod;
    }

    /**
     * 
     * @param importMethodString
     * @return
     */
    protected static ImportMethod validateAndReturnImportMethod(
            String importMethodString) {
        ImportMethod importMethod = null;
        try {
            importMethod = ImportMethod.mapInputParameter(importMethodString);
        } catch (final GeneralLoggingException e) {
            System.err
                    .println("Wrong import method given. Allowed values are: 'postgredump' or 'fileystem'");
            return null;
        }
        return importMethod;
    }

    /**
     * 
     * @param inputFile2
     * @param outputFile2
     * @param importMethodString
     * @param exportMethodString
     * @return
     */
    private static boolean validateInputParameters(String inputFile2,
            String outputFile2, String importMethodString,
            String exportMethodString) {
        if (null == inputFile2) {
            System.err
                    .println("No inputFile given. Please give a fully qualified path after the '-inputFile' key.");
            return false;
        } else if (null == outputFile2) {
            System.err
                    .println("No outputtFile given. Please give a fully qualified path after the '-outputFile' key.");
            return false;
        } else if (null == importMethodString) {
            System.out
                    .println("No import method given. The standard import method 'postgredump' will be used. You can append another method after the '-importMethod' key.");
            importMethodString = ImportMethod.POSTGREDUMP.toString()
                    .toLowerCase();
            return false;
        } else if (null == exportMethodString) {
            System.out
                    .println("No export method given. The standard export method 'filesystem' will be used. You can append another export method after the '-exportMethod' key.");
            exportMethodString = ExportMethod.FILESYSTEM.toString()
                    .toLowerCase();
            return false;
        }

        return true;
    }

    /**
     * 
     * @param args
     * @return
     */
    protected static boolean printHelpIfConfigured(final String[] args) {
        if (0 == args.length || args[0].equals("--help")) {
            System.out.println(printHelp());
            return true;
        }

        return false;
    }

    /**
     * 
     * @return
     */
    private static String printHelp() {
        final StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("..:: HELP ::..\n");
        helpBuilder
                .append("The anonymization tool takes a file or directory that contains the unanonymized data from Moodle and exports it using a given method.\n");
        helpBuilder
                .append("Make sure, that you configured the anonymization chain in the anonymization.properties config file.\n");
        helpBuilder.append("\n");
        helpBuilder.append("Required arguments:\n\n");
        helpBuilder.append("-inputfile [FILE]\n");
        helpBuilder.append("-importmethod "
                + ImportMethod.getAllowedInputParameters() + "\n");
        helpBuilder.append("-exportmethod "
                + ExportMethod.getAllowedInputParameters() + "\n");
        helpBuilder.append("-outputfile [FILE]\n");

        return helpBuilder.toString();
    }

}
