/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization.controller;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.experimental.DirectoryExportStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodlePostgreSqlImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Course;
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
	/**
	 * 
	 * <p>
	 * This enum contains the allowed import methods to be used.
	 * </p>
	 * 
	 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
	 * @since 12.12.2013
	 * 
	 */
	public enum ImportMethods {
		POSTGREDUMP, FILESYSTEM,
	}

	/**
	 * 
	 * <p>
	 * This enum contains the allowed export methods that can be used.
	 * </p>
	 * 
	 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
	 * @since 12.12.2013
	 * 
	 */
	public enum ExportMethods {
		FILESYSTEM,
	}

	private File inputFile;
	private File outputFile;
	private AImportStrategy importStrategy;
	private AExportStrategy exportStrategy;
	private AnonymizationController anonymizationController;
	private Collection< Course > rawCourses;
	private Collection< Course > anonymizedCourses;
	private int numberImportedCourses;
	private int numberAnonymizedCourses;
	private long anonymizationStartTime;
	private long anonymizationStopTime;

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
	public AnonymizationCliController( File inputFile, File outputFile,
			ImportMethods importMethod, ExportMethods exportMethod )
			throws InvalidConfigException {
		if( null == inputFile || !inputFile.exists() ) {
			throw new IllegalArgumentException(
					"Illegal argument for inputFile: " + inputFile );
		}
		if( null == outputFile ) {
			throw new IllegalArgumentException(
					"Illegal argument for outputFile: " + outputFile );
		}

		// store files
		this.inputFile = inputFile;
		this.outputFile = outputFile;

		// initialize import strategy
		if( importMethod.equals( ImportMethods.POSTGREDUMP ) ) {
			this.importStrategy = new MoodlePostgreSqlImportStrategy();
		} else if( importMethod.equals( ImportMethods.FILESYSTEM ) ) {
			this.importStrategy = new DirectoryImportStrategy();
		} else {
			throw new UnsupportedOperationException(
					"The given import strategy " + importStrategy
							+ " is not supported yet!" );
		}

		// initialize export strategy
		if( exportMethod.equals( ExportMethods.FILESYSTEM ) ) {
			this.exportStrategy = new DirectoryExportStrategy();
		} else {
			throw new UnsupportedOperationException(
					"The given export strategy " + exportStrategy
							+ " is not supported yet!" );
		}

		this.anonymizationController = new AnonymizationController(
				this.importStrategy, this.exportStrategy );
	}

	/**
	 * Perform the import delegation to the given import strategy.
	 * 
	 * @return true on success
	 * @throws GeneralLoggingException
	 */
	public boolean performImport() throws GeneralLoggingException {
		this.rawCourses = this.importStrategy
				.importBoardFromFile( this.inputFile );
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
		if( null == this.rawCourses ) {
			throw new IllegalStateException(
					"performAnonymization() must be called after performImport()! Maybe the import wasn't succesful." );
		}
		
		this.anonymizationStartTime = new Date().getTime();
		this.anonymizedCourses = this.anonymizationController
				.performAnonymization( this.rawCourses );
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
		if( null == this.anonymizedCourses ) {
			throw new IllegalStateException(
					"performExport() must be called after performAnonymization()! Maybe the anonymization wasn't succesful." );
		}

		this.exportStrategy.exportToFile( this.anonymizedCourses,
				this.outputFile );

		return true;
	}
	
	/**
	 * Get a string containing statistics.
	 * @return
	 */
	public String getStatistics() {
		StringBuilder statisticsBuilder = new StringBuilder();
		
		statisticsBuilder.append( "Number of imported courses: " + this.numberImportedCourses +"\n");
		statisticsBuilder.append( "Number of anonymized courses: " + this.numberAnonymizedCourses +"\n");
		
		long elapsedTime = this.anonymizationStopTime - this.anonymizationStartTime;
		statisticsBuilder.append( this.anonymizationController.getStatistics( this.anonymizedCourses, elapsedTime ));
		
		return statisticsBuilder.toString();
	}

	public static void main( String[] args ) {
		System.out.println("..:: Moodle anonymization tool ::..");
		System.out.println("Welcome!");
		System.out.println("");
		System.out.println("Append --help for a help text.");
		System.out.println("");
		
		// read args
		int ind = 0;
		String inputFile = null;
		String outputFile = null;
		String importMethodString = null;
		String exportMethodString = null;

		if ( 0 == args.length || args[0].equals( "--help" ) ) {
			System.out.println(printHelp());
			return;
		}
		// read options
		for( String arg : args ) {
			String argPrepared = arg.trim().toLowerCase();
			System.out.println( argPrepared );
			if( argPrepared.equals( "-inputfile" ) ) {
				inputFile = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
						: null;
			} else if( argPrepared.equals( "-outputfile" ) ) {
				outputFile = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
						: null;
			} else if( argPrepared.equals( "-importmethod" ) ) {
				importMethodString = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
						: null;
				importMethodString = importMethodString.trim().toUpperCase();
			} else if( argPrepared.equals( "-exportmethod" ) ) {
				exportMethodString = ( ( ind + 1 ) < args.length ) ? args[ind + 1]
						: null;
				exportMethodString = exportMethodString.trim().toUpperCase();
			}
			ind++;
		}

		if( null == inputFile ) {
			System.err
					.println( "No inputFile given. Please give a fully qualified path after the '-inputFile' key." );
		} else if( null == outputFile ) {
			System.err
					.println( "No outputtFile given. Please give a fully qualified path after the '-outputFile' key." );
		} else if( null == importMethodString ) {
			System.out
					.println( "No import method given. The standard import method 'postgredump' will be used. You can append another method after the '-importMethod' key." );
			importMethodString = ImportMethods.POSTGREDUMP.toString()
					.toLowerCase();
		} else if( null == exportMethodString ) {
			System.out
					.println( "No export method given. The standard export method 'filesystem' will be used. You can append another export method after the '-exportMethod' key." );
			exportMethodString = ExportMethods.FILESYSTEM.toString()
					.toLowerCase();
		} else {
			// check given methods
			ImportMethods importMethod = null;
			try {
				importMethod = ImportMethods.valueOf( importMethodString );
			} catch( IllegalArgumentException e ) {
				System.err
						.println( "Wrong import method given. Allowed values are: 'postgredump' or 'fileystem'" );
				return;
			}

			ExportMethods exportMethod = null;
			try {
				exportMethod = ExportMethods.valueOf( exportMethodString );
			} catch( IllegalArgumentException e ) {
				System.err
						.println( "Wrong export method given. Allowed values are: 'fileystem'" );
				return;
			}

			// initialize controller
			AnonymizationCliController controller = null;
			try {
				controller = new AnonymizationCliController( new File(
						inputFile ), new File( outputFile ), importMethod,
						exportMethod );

				System.out.println( "Controller is initialized..." );
				System.out.println( "Input file: " + inputFile );
				System.out.println( "Output file: " + outputFile );
				System.out.println( "Import method: " + importMethod );
				System.out.println( "Export method: " + exportMethod );
				System.out.println();
			} catch( UnsupportedOperationException | IllegalArgumentException
					| InvalidConfigException e ) {
				System.err
						.println( "Controller couldn't get initialized. Error: "
								+ e.getLocalizedMessage() );
				return;
			}

			// perform import
			System.out.println( "Starting import..." );
			boolean importSuccess = false;
			try {
				importSuccess = controller.performImport();
			} catch( GeneralLoggingException e ) {
				importSuccess = false;
				System.err.println( "Import wasn't successful. Error: "
						+ e.getLocalizedMessage() );
				// handled below again
			}

			if( !importSuccess ) {
				System.err.println( "Import wasn't successful." );
				return;
			}
			System.out.println("Import was successful!");

			// perform anonymization
			boolean anonymSuccess = false;

			try {
				anonymSuccess = controller.performAnonymization();
			} catch( GeneralLoggingException e ) {
				anonymSuccess = false;
				System.err.println( "Anonymization wasn't succesful. Error: " + e.getLocalizedMessage() );
			}
			
			if (!anonymSuccess) {
				System.err.println( "Anonymization wasn't successful." );
				return;
			}
			System.out.println("Anonymization was successful!");
			
			// perform export
			boolean exportSuccess = false;

			try {
				exportSuccess = controller.performExport();
			} catch( GeneralLoggingException e ) {
				exportSuccess = false;
				System.err.println( "Export wasn't succesful. Error: " + e.getLocalizedMessage() );
			}
			
			if (!exportSuccess) {
				System.err.println( "Export wasn't successful." );
				return;
			}
			System.out.println("Export was successful!");
			System.out.println();
			
			// Show stats
			System.out.println(controller.getStatistics());
			
			System.out.println("Goodybe :)");
		}

	}

	private static String printHelp() {
		StringBuilder helpBuilder = new StringBuilder();
		
		helpBuilder.append( "..:: HELP ::..\n" );
		helpBuilder.append( "The anonymization tool takes a file or directory that contains the unanonymized data from Moodle and exports it using a given method.\n" );
		helpBuilder.append( "Make sure, that you configured the anonymization chain in the anonymization.properties config file.\n" );
		helpBuilder.append( "\n" );
		helpBuilder.append( "Required arguments:\n\n" );
		helpBuilder.append( "-importfile [FILE]\n" );
		helpBuilder.append( "-importmethod [postgredump|filesystem]\n" );
		helpBuilder.append( "-exportmethod [filesystem]\n" );
		helpBuilder.append( "-exportfile [FILE]\n" );	
		
		return helpBuilder.toString();
	}

}
