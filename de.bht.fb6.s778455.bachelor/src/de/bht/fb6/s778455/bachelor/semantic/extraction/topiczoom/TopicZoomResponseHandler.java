/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.xml.StringXmlExtractor;
import de.bht.fb6.s778455.bachelor.organization.xml.XmlExtractor;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;

/**
 * 
 * <p>
 * This class is a response handler implementation for the HTTP response of
 * TopicZoom.
 * </p>
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
			BufferedReader r = new BufferedReader( new InputStreamReader( is ) );

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
	 * 
	 * @param response
	 *            {@link String} the XML response from Topic Zoom.
	 * @return a sorted list of tags (primary sorting criteria is the weight;
	 *         the secondary sorting criteria is the significance)
	 * @throws GeneralLoggingException
	 */
	public List< Tag > fetchTags( final String response ) throws GeneralLoggingException {
		// Create new list
		final List< Tag > fetchedTags = new ArrayList<Tag>();
		
		// parse xml
		final XmlExtractor xmlExtractor = new StringXmlExtractor( response, ServiceFactory.getTZNamespaces() );
		final String[] rdfIdQuery = ( String[] ) xmlExtractor.buildXPath( "//TZTopic/@RDFID/text()", true );
		
		for( String rdfId : rdfIdQuery ) {
			// prepare queries
			String queryPrefix = "//TZTopic[@RDFID=" + rdfId + "]";
			String queryTxt = queryPrefix + "/txt/text()";
			String queryWeight = queryPrefix + "/weight";
			String queryDoG = queryPrefix + "/DoG";
			String querySig = queryPrefix + "/Sig";
			
			// execute queries
			String txt = ( String ) xmlExtractor.buildXPath( queryTxt, false );
			
			double weight;
			try {
				weight = Double.parseDouble( ( String ) xmlExtractor.buildXPath( queryWeight, false ));
			} catch ( ClassCastException e) {
				throw new GeneralLoggingException( getClass() + ":fetchTags(): couldn't parse the given value for weight and convert it to a double. The weight is neccessary! Check the API.", "Error in the TopicZoomResponseHandler. See the logs." );
			}
			
			int doG;
			try {
				doG = Integer.parseInt( ( String ) xmlExtractor.buildXPath( queryDoG, false ));
			} catch ( ClassCastException e) {
				throw new GeneralLoggingException( getClass() + ":fetchTags(): couldn't parse the given value for weight and convert it to a double. The weight is neccessary! Check the API.", "Error in the TopicZoomResponseHandler. See the logs." );
			}			
			
			double sig;
			try {
				sig = Double.parseDouble( ( String ) xmlExtractor.buildXPath( querySig, false ));
			} catch ( ClassCastException e) {
				throw new GeneralLoggingException( getClass() + ":fetchTags(): couldn't parse the given value for weight and convert it to a double. The weight is neccessary! Check the API.", "Error in the TopicZoomResponseHandler. See the logs." );
			}
			
			// assign values to new Tag instance
			TopicZoomTag newTag = new TopicZoomTag( sig, doG, weight, txt, rdfId );					
			fetchedTags.add( newTag );
		}
		
		return fetchedTags;		
	}
}