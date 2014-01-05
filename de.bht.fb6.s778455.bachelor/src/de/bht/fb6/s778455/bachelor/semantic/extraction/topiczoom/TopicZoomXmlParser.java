/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom;

import de.bht.fb6.s778455.bachelor.model.Posting;

/**
 * <p>
 * This class takes the response of TopicZoom and handles the XML.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 * 
 */
public class TopicZoomXmlParser {
	protected Posting posting;

	/**
	 * Create a new XML Parser.
	 */
	public TopicZoomXmlParser( final Posting p ) {
		this.posting = p;
	}

	/**
	 * Annotate the {@link Posting} by the reponse of TopicZoom. Use an XML
	 * parser to order the resulting tags and to add them to the posting.
	 * 
	 * @param reponse
	 */
	public void annotatePostingByResponse( final String reponse ) {

	}
}
