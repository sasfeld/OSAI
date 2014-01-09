/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.controller;

import java.util.Collection;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;

/**
 * <p>This class realizes the controller for all tasks issuing the semantic extraction..</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 *
 */
public class SemanticExtractionController {
	protected List< String > extractionChain;
	private boolean printInfo = false;
	
	/**
	 * Create a new semantic extraction controller.
	 * @throws InvalidConfigException 
	 * @throws IllegalArgumentException
	 */
	public SemanticExtractionController() throws InvalidConfigException {
		this.extractionChain = ServiceFactory.getConfigReader().fetchMultipleValues( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_CHAIN );
	}
	
	/**
	 * Perform the configured semantic extraction strategies.
	 * @param inputCourses
	 * @return
	 * @throws InvalidConfigException 
	 */
	public Collection< Course > performSemanticExtraction( final Collection< Course > inputCourses) throws InvalidConfigException {
		if ( null == inputCourses) {
			throw new IllegalArgumentException( "None of the given parameters may be null!" );
		}
		
		// iterate through configured extraction strategies
		for( String strategy : this.extractionChain ) {
			// try to get class
			final Object clazz = ServiceFactory.getConfigReader().getConfiguredClass( strategy );
			
			if ( ! (clazz instanceof AExtractionStrategy)) {
				throw new InvalidConfigException( getClass() + "performSemanticExtraction: the configuration value for the key " + strategy + " doesn't point to a class extending AExtractionStrategy. Please check the configuration.", "Illegal configuration value. Please check the logs.", null );
			}
			
			final AExtractionStrategy extractionStrategy = (AExtractionStrategy) clazz;
			
			this._performExtractionForPostings(inputCourses, extractionStrategy);
 		}
		
		// strategies only fill models attributes
		return inputCourses;
	}

	/**
	 * Iterate through postings and let the given {@link AExtractionStrategy} do its work.
	 * @param inputCourses
	 * @param extractionStrategy
	 */
	private void _performExtractionForPostings(
			Collection< Course > inputCourses,
			AExtractionStrategy extractionStrategy ) {
		for( Course course : inputCourses ) {
			for( Board board : course.getBoards() ) {
				for( BoardThread thread : board.getBoardThreads() ) {
					for( Posting posting : thread.getPostings() ) {
						try {
							if ( this.printInfo()) {
								System.out.println("Enriching posting " + posting.getTitle() + "...\n");
							}
							extractionStrategy.extractSemantics( posting );
						} catch( GeneralLoggingException e ) {
							// continue, the error is already logged
						}
					}
				}
			}
		}
		
	}
	
	private boolean printInfo() {
		return this.printInfo;
	}
	
	public void setPrintInfo(boolean printInfo) {
		this.printInfo = printInfo;
	}

	/**
	 * Get a statistics string.
	 * 
	 * @param anonymizedCourses
	 * @param elapsedTime
	 */
	public String getStatistics( Collection< Course > anonymizedCourses
		) {
		StringBuilder statisticsBuilder = new StringBuilder();

	
		statisticsBuilder
				.append( "Used chain of extraction strategies (config keys): "
						+ this.extractionChain 
						+ "\n" );
		statisticsBuilder.append( "Number of courses: "
				+ anonymizedCourses.size() + "\n" );
		int numberThreads = 0;
		int numberPostings = 0;

		for( Course course : anonymizedCourses ) {			
			// System.out.println("Course: " + course);
			// System.out.println("");
			// System.out.println(".............................");
			for( Board board : course.getBoards() ) {
				// System.out.println("Board: " + board);
				// System.out.println();
				// System.out.println("++++++++++++++++++++++++++++++");
				numberThreads += board.getBoardThreads().size();

				for( BoardThread boardThread : board.getBoardThreads() ) {
					// System.out.println("Thread: " + boardThread);
					// System.out.println();
					// System.out.println("--------------------------------");
					numberPostings += boardThread.getPostings().size();

					// for( Posting p : boardThread.getPostings() ) {
					// // System.out.println("Posting: " + p);
					// }
					// System.out.println("--------------------------------");
				}
				// System.out.println("++++++++++++++++++++++++++++++");
			}
			// System.out.println(".............................");
		}

	
		statisticsBuilder.append( "Number of threads: " + numberThreads + "\n" );
		statisticsBuilder.append( "Number of postings: " + numberPostings
				+ "\n" );

		return statisticsBuilder.toString();
	}
}
