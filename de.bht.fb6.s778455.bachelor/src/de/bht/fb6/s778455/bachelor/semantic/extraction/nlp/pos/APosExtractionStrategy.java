/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.pos;

import java.io.File;
import java.util.List;

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
public abstract class APosExtractionStrategy extends AExtractionStrategy {
	protected MaxentTagger maxentTagger;
	
	/**
	 * Create a new {@link APosExtractionStrategy} which creates a tagger instance based on the given model.
	 * 
	 * @param model the trained POS tagger.
	 * @throws IllegalArgumentException
	 */
	public APosExtractionStrategy(final File model, final String... allowedFileNames) {
		if ( null == model || !model.exists() ) {
			throw new IllegalArgumentException( this.getClass() + "PosExtractionStrategy(): the given model " + model.getAbsolutePath() + " doesn't exist or isn't a file." );
		}
		
		 // check if the model file is allowed for the language (sub classes
        // define that)
        // so we try to forbid the usage of pos taggers for other languages than
        // the given subclass
        boolean isAllowed = false;
        for( final String allowedFileName : allowedFileNames ) {
            if( model.getName().startsWith( allowedFileName ) ) {
                isAllowed = true;
            }
        }

        if( !isAllowed ) {
            throw new IllegalArgumentException(
                    this.getClass()
                            + ": The given model doesn't seem to be allowed for this language. It mus be one of the following: "
                            + allowedFileNames );
        }

		
		this.maxentTagger = new MaxentTagger(model.getAbsolutePath());	
	}
	
	/**
     * Check whether the expected POS tags are included in the tagger.
     */
    protected void _checkTaggerLabels() {
        boolean notContained = false;
        for( final String expectedLabel : this.getExpectedPosTags() ) {
            if ( !this.maxentTagger.getTags().getOpenTags().contains( expectedLabel )) {
                notContained = true;
            }
        }
        
        // throw exception if not contained
        if ( notContained ) {
            throw new IllegalArgumentException(
                    this.getClass()
                            + ": The given classifierFile doesn't seem to be allowed for this language.");
        }
    }
    
    /**
     * Each subclass needs to define the POS tags/labels that it expects from
     * the tagger. So we can do an error handling if a foreign tagger is
     * used.
     */
    public abstract List< String > getExpectedPosTags();

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
	 */
	@Override
	public void extractSemantics( final AUserContribution userContribution )
			throws GeneralLoggingException {
		if ( null == userContribution ) {
			throw new IllegalArgumentException( this.getClass() + "null values aren't allowed as parameter!" );			
		}
		
		// for a posting, extract the content and tag it
		if ( userContribution instanceof Posting ) {
			final Posting p = (Posting) userContribution;
			final String untaggedContent = p.getContent();
			System.out.println(this.maxentTagger.tagString( untaggedContent ));
		}
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
	 */
	@Override
	public void extractSemantics( final Course course )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub

	}

}
