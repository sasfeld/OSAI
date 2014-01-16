/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.controller;

import java.io.File;
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
import de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.GeneralStatisticsBuilder;

/**
 * <p>
 * This class realizes the controller for all tasks issuing the semantic
 * extraction..
 * </p>
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
	 * 
	 * @throws InvalidConfigException
	 * @throws IllegalArgumentException
	 */
	public SemanticExtractionController() throws InvalidConfigException {
		this.extractionChain = ServiceFactory.getConfigReader()
				.fetchMultipleValues(
						IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_CHAIN );
	}

	/**
	 * Perform the configured semantic extraction strategies.
	 * 
	 * @param inputCourses
	 * @return
	 * @throws InvalidConfigException
	 */
	public Collection< Course > performSemanticExtraction(
			final Collection< Course > inputCourses )
			throws InvalidConfigException {
		if( null == inputCourses ) {
			throw new IllegalArgumentException(
					"None of the given parameters may be null!" );
		}

		// iterate through configured extraction strategies
		for( String strategy : this.extractionChain ) {
			// try to get class
			Object clazz = null;
			if( strategy.equals( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_NER ) ) {
				// get cascade
				List< String > cascade = ServiceFactory.getConfigReader().fetchMultipleValues( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_NER_GERMAN_CASCADE );
				
				for( String cascadeFile : cascade ) {
					clazz = ServiceFactory.getConfigReader().getConfiguredClass(
							strategy, new File( ServiceFactory.getConfigReader().fetchValue( cascadeFile ) ) );
					if( !( clazz instanceof AExtractionStrategy ) ) {
						throw new InvalidConfigException(
								getClass()
										+ "performSemanticExtraction: the configuration value for the key "
										+ strategy
										+ " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
								"Illegal configuration value. Please check the logs.",
								null );
					}

					final AExtractionStrategy extractionStrategy = ( AExtractionStrategy ) clazz;

					this._performExtractionForPostingsAndCourses( inputCourses,
							extractionStrategy );
				}
			
			} else { // TopicZoomExtractionStrategy
				clazz = ServiceFactory.getConfigReader().getConfiguredClass(
						strategy );
				if( !( clazz instanceof AExtractionStrategy ) ) {
					throw new InvalidConfigException(
							getClass()
									+ "performSemanticExtraction: the configuration value for the key "
									+ strategy
									+ " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
							"Illegal configuration value. Please check the logs.",
							null );
				}

				final AExtractionStrategy extractionStrategy = ( AExtractionStrategy ) clazz;

				this._performExtractionForPostingsAndCourses( inputCourses,
						extractionStrategy );
			}

		}

		// strategies only fill models attributes
		return inputCourses;
	}

	/**
	 * Iterate through postings and let the given {@link AExtractionStrategy} do
	 * its work.
	 * 
	 * @param inputCourses
	 * @param extractionStrategy
	 */
	private void _performExtractionForPostingsAndCourses(
			Collection< Course > inputCourses,
			AExtractionStrategy extractionStrategy ) {
		for( Course course : inputCourses ) {
			if( this.printInfo() ) {
				System.out.println( "Enriching course " + course.getTitle()
						+ "...\n" );
			}
			try {
				extractionStrategy.extractSemantics( course );
			} catch( GeneralLoggingException e1 ) {
				// continue, error is already logged
			}
			for( Board board : course.getBoards() ) {
				if( this.printInfo() ) {
					System.out.println( "Enriching board " + board.getTitle()
							+ "...\n" );
				}
				try {
					extractionStrategy.extractSemantics( board );
				} catch( GeneralLoggingException e1 ) {
					// continue, error is already logged
				}

				for( BoardThread thread : board.getBoardThreads() ) {
					for( Posting posting : thread.getPostings() ) {
						try {
							if( this.printInfo() ) {
								System.out.println( "Enriching posting "
										+ posting.getTitle() + "...\n" );
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

	public void setPrintInfo( boolean printInfo ) {
		this.printInfo = printInfo;
	}

	/**
	 * Get a statistics string.
	 * 
	 * @param anonymizedCourses
	 * @param elapsedTime
	 */
	public String getStatistics( Collection< Course > anonymizedCourses ) {
		StringBuilder statisticsBuilder = new StringBuilder();

		statisticsBuilder
				.append( "Used chain of extraction strategies (config keys): "
						+ this.extractionChain + "\n" );

		AStatisticsBuilder builder = new GeneralStatisticsBuilder();
		statisticsBuilder.append( builder.buildStatistics( anonymizedCourses )
				.toString() + "\n" );

		return statisticsBuilder.toString();
	}
}
