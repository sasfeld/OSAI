/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos;

import java.io.File;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * <p>This class realizes an {@link AExtractionStrategy} which works with Stanford Part of Speech (POS) tagging on the trained models.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.01.2014
 *
 */
public class PosExtractionStrategy extends AExtractionStrategy {
	protected MaxentTagger maxentTagger;
	
	/**
	 * Create a new {@link PosExtractionStrategy} which creates a tagger instance based on the given model.
	 * 
	 * @param model the trained POS tagger.
	 * @throws IllegalArgumentException
	 */
	public PosExtractionStrategy(final File model) {
		if ( null == model || !model.exists() ) {
			throw new IllegalArgumentException( getClass() + "PosExtractionStrategy(): the given model " + model.getAbsolutePath() + " doesn't exist or isn't a file." );
		}
		
		this.maxentTagger = new MaxentTagger(model.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
	 */
	@Override
	public void extractSemantics( final AUserContribution userContribution )
			throws GeneralLoggingException {
		if ( null == userContribution ) {
			throw new IllegalArgumentException( getClass() + "null values aren't allowed as parameter!" );			
		}
		
		// for a posting, extract the content and tag it
		if ( userContribution instanceof Posting ) {
			Posting p = (Posting) userContribution;
			final String untaggedContent = p.getContent();
			System.out.println(this.maxentTagger.tagString( untaggedContent ));
		}
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
	 */
	@Override
	public void extractSemantics( Course course )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub

	}

}
