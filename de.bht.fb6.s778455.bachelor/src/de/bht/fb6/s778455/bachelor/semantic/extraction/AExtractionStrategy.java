/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.MoodleHelper;
import de.bht.fb6.s778455.bachelor.organization.StringUtil;

/**
 * <p>This class defines the API for all concrete strategies which aim to extract semantic information from {@link Posting} instances.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public abstract class AExtractionStrategy {	
	/**
	 * Flag whether the lazy mode is enabled (true) or not (false).
	 * The lazy mode means: if a UserContribution was already tagged by the same {@link TagType}, than it will not be tagged again.
	 */
	protected boolean lazyMode = true;
	
	
	/**
	 * @return the lazyMode
	 */
	public final boolean isLazyMode() {
		return this.lazyMode;
	}


	/**
	 * Flag whether the lazy mode is enabled (true) or not (false).
	 * The lazy mode means: if a UserContribution was already tagged by the same {@link TagType}, than it will not be tagged again.
	 * @param lazyMode the lazyMode to set
	 */
	public final void setLazyMode( final boolean lazyMode ) {
		this.lazyMode = lazyMode;
	}

	/**
	 * Prepare the given string - text.
	 * @param preparedText
	 * @return
	 */
	protected String prepareText( final String text ) {
	    if ( null == text ) {
	        throw new IllegalArgumentException( "The value for the argument text mustn't be null!" );
	    }
	    
	    String cleanedText = StringUtil.fillMissingWhitespaces( text );
	    
	    cleanedText = MoodleHelper.removeMoodleChars( cleanedText );
	    
	    return cleanedText;
	}

	/**
	 * Extract semantic information from a given {@link AUserContribution}
	 * @param p
	 * @throws GeneralLoggingException 
	 */
	public abstract void extractSemantics(final AUserContribution userContribution) throws GeneralLoggingException;

	/**
	 * Extract semantic information from the given {@link Course}. 
	 * This is only meant to extract information from the course model itself, not from underlying {@link Board}, {@link BoardThread} and {@link Posting} instances!
	 * @param course
	 * @throws GeneralLoggingException
	 */
	public abstract void extractSemantics(final Course course) throws GeneralLoggingException;
}
