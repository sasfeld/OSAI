/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sf.saxon.expr.sort.SortedGroupIterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import de.bht.fb6.s778455.bachelor.model.Tag;

/**
 * 
 * <p>This class is a response handler implementation for the HTTP response of TopicZoom.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class TopicZoomResponseHandler implements ResponseHandler< String > {

	@Override
	public String handleResponse( HttpResponse response )
			throws ClientProtocolException, IOException {
		// everything ok => status: 200
		if( 200 == response.getStatusLine().getStatusCode() ) { 
			// build a string from the response of TopicZoom
			StringBuilder strBuilder = new StringBuilder();

			InputStream is = response.getEntity().getContent();
			BufferedReader r = new BufferedReader(
					new InputStreamReader( is ) );

			String line;
			while( null != ( line = r.readLine() ) ) {
				strBuilder.append( line + "\n" );
			}

			return strBuilder.toString();
		} else {
			throw new IOException(
					getClass()
							+ ":extractSemantics(): TopicZoom returned bad status code: "
							+ response.getStatusLine().toString() );
		}
	}
	
	/**
	 * Fetch the tags from Topic Zoom's response.
	 * @param response {@link String} the XML response from Topic Zoom.
	 * @return a sorted list of tags (primary sorting criteria is the weight; the secondary sorting criteria is the significance)
	 */
	public List< Tag > fetchTags( final String response ) {
		// Create new list
		final List< Tag > fetchedTags = new ArrayList<Tag>();
		
		
		
		// instanciate comparator
		final Comparator< Tag > tagComparator = new TagComparator();
		
		return null;		
	}
}