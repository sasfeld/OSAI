/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.controller;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.experimental.DirectoryExportStrategy;
import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

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

	protected AImportStrategy importStrategy;
	protected AExportStrategy exportStrategy;
	protected Collection< Course > rawCourses;
	protected Collection< Course > enrichedCourses;
	
	protected int numberImportedCourses;
	private long extractionStartTime;
	private long extractionStopTime;

	public SemanticExtractionCliController(final File finputFile, final File foutputFile) throws InvalidConfigException {
		this.semanticExtractionController = new SemanticExtractionController();
		
		this.inputFile = finputFile;
		this.outputFile = foutputFile;
		
		this.importStrategy = new de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy();
		this.exportStrategy = new DirectoryExportStrategy();
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
	 * Perform the semantic extraction.
	 * @return
	 * @throws InvalidConfigException 
	 */
	public boolean performExtraction() throws InvalidConfigException {
		if( null == this.rawCourses ) {
			throw new IllegalStateException(
					"performAnonymization() must be called after performImport()! Maybe the import wasn't succesful." );
		}
		
		this.extractionStartTime = new Date().getTime();
		this.enrichedCourses = this.semanticExtractionController
				.performSemanticExtraction(  this.rawCourses );
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
		if( null == this.enrichedCourses ) {
			throw new IllegalStateException(
					"performExport() must be called after performAnonymization()! Maybe the anonymization wasn't succesful." );
		}

		this.exportStrategy.exportToFile( this.enrichedCourses,
				this.outputFile );

		return true;
	}
	
	/**
	 * Get a string containing statistics.
	 * 
	 * @return
	 */
	public String getStatistics() {
		StringBuilder statisticsBuilder = new StringBuilder();

		long elapsedTime = this.extractionStopTime
				- this.extractionStartTime;
		statisticsBuilder.append( "Elapsed time (seconds): " + elapsedTime);

		return statisticsBuilder.toString();
	}



	public static void main( String[] args ) {
		System.out.println( "..:: Semantic extraction tool ::.." );
		System.out.println( "Welcome!" );
		System.out.println( "" );
		System.out.println( "Append --help for a help text." );
		System.out.println( "" );

		// read args
		int ind = 0;
		String inputFile = null;
		String outputFile = null;

		if( 0 == args.length || args[0].equals( "--help" ) ) {
			System.out.println( printHelp() );
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
			}
			ind++;
		}

		// validate
		if( null == inputFile ) {
			System.err
					.println( "No inputFile given. Please give a fully qualified path after the '-inputFile' key." );
		} else if( null == outputFile ) {
			System.err
					.println( "No outputtFile given. Please give a fully qualified path after the '-outputFile' key." );
		}

		// instantiate files and controller
		File finputFile = new File( inputFile );
		File foutputFile = new File( outputFile );

		if( !finputFile.exists() ) {
			System.err
					.println( "The given inputFile doesn't exist: "
							+ inputFile
							+ ". Make sure that you appended the correct file and retry." );
		} else if( !foutputFile.exists() ) {
			System.err
					.println( "The given outputFile doesn't exist: "
							+ outputFile
							+ ". Make sure that you appended the correct file and retry." );
		}

		SemanticExtractionCliController controller = null;
		try {
			controller = new SemanticExtractionCliController(finputFile, foutputFile);
			System.out.println( "Controller is initialized..." );
			System.out.println( "Input file: " + inputFile );
			System.out.println( "Output file: " + outputFile );

		} catch( InvalidConfigException e ) {
			System.err.println( "Controller couldn't get initialized. Error: "
					+ e.getLocalizedMessage() );
			return;
		}

		// perform import
		try {
			System.out.println("Starting import...\n\n");
			controller.performImport();
		} catch( GeneralLoggingException e ) {
			System.err.println("An error occured: " + e.getLocalizedMessage());
			return;
		}
		System.out.println("Import was successfull!\n\n");
		
		// perform extraction
		try {
			System.out.println("Starting extraction...\n\n");
			controller.performExtraction();
		} catch ( GeneralLoggingException e) {
			System.err.println("An error occured: " + e.getLocalizedMessage());
			return;
		}
		System.out.println("Extraction was successfull!\n\n");
		
		// perform export		
		try {
			System.out.println("Starting export...");
			controller.performExport();
		} catch( GeneralLoggingException e ) {
			System.err.println("An error occured: " + e.getLocalizedMessage());
			return;
		}		
		System.out.println("Export was successfull!\n\n");
		
		System.out.println(controller.getStatistics());
		System.out.println("See the posting files saved in your outputFolder. The postings are now enriched with tags and stuff.\n\n");
		System.out.println("Goodbye :)");
	}

	private static String printHelp() {
		StringBuilder helpBuilder = new StringBuilder();

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

		return helpBuilder.toString();
	}
}
