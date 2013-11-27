/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.anonymization;

import de.bht.fb6.s778455.bachelor.anonymization.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * 
 * <p>This class handles the anonymization of a single {@link Board}.</p>
 * <p>It uses an {@link AAnomyzationStrategy} to perform the anonymization.</p>
 * <p>The anonymization strategy can be configured in the conf/anonymization.properties config file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class Anonymizer {
	protected AAnomyzationStrategy anonymizationStrategy;
	
	/**
	 * Instanciate an Anonymizer and all depending instances.
	 * @throws InvalidConfigException if the configuration doesn't work
	 */
	public Anonymizer() throws InvalidConfigException {
		this.anonymizationStrategy = ((ConfigReader) ServiceFactory.getConfigReader()).getConfiguredAnonymizationStrategy();
	}
	
	/**
	 * Instanciate a new Anonymizer using the given strategy.
	 * @param strategy
	 */
	public Anonymizer( AAnomyzationStrategy strategy ) {
		this.anonymizationStrategy = strategy;
	}

	/**
	 * Anonymize a given {@link Board}.
	 * The given instance will be maniuplated immediatly. 
	 * @param inputBoard
	 * @return the anonymized {@link Board} 
	 */
	public Board anonymizeBoard(Board inputBoard) {
		// iterate through threads and postings and hand in the text to be anonymized by the configured strategy
		for( BoardThread boardThread : inputBoard.getBoardThreads() ) {
			for( Posting posting : boardThread.getPostings() ) {
				String anonymizedTaggedText = this.anonymizationStrategy.anonymizeText( posting.getContent() );
				// String without NER tags (but anonymization tags)
				String anonymizedUntaggedText = this.anonymizationStrategy.removeSpecialTags( anonymizedTaggedText );
				
				posting.setContent( anonymizedUntaggedText );
				posting.setTaggedContent( anonymizedTaggedText );
			}
		}
		
		return inputBoard;
	
	}

	/**
	 * Get the {@link AAnomyzationStrategy} instance which is used in the Anonymizer.
	 * @return
	 */
	public AAnomyzationStrategy getAnonymizationStrategy() {
		return this.anonymizationStrategy;
	}
}
