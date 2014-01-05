/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>Instances of this strategy aim to use the WebService offered by TopicZoom to enrich {@link Posting} instances with auto-generated tags.</p>
 * <p>URL: <a href="http://www.topiczoom.de/">http://topiczoom.de</a></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class TopicZoomExtractionStrategy extends AExtractionStrategy {	
	/**
	 * HTTP Header key for the content type.
	 */
	public static final String HTTP_CONTENT_TYPE = "Content-Type";
	
	protected CloseableHttpClient httpClient;
	protected final String serviceUrl;
	protected boolean clientClosed;

	/**
	 * Create and instantiate a new topic zoom strategy.
	 */
	public TopicZoomExtractionStrategy() {
		this.initClient();
		this.serviceUrl = "http://s15415510.onlinehome-server.info:2208/quickindex.xml";		
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Posting)
	 */
	@Override
	public void extractSemantics( Posting p ) throws GeneralLoggingException {
		if (this.clientClosed) {
			Application.log( getClass() + ":extractSemantics(): the client was closed due an error within a previous http request to the topic zoom web service. However, I will reinstanciate it.", LogType.INFO  );
			
			// reinstanciate client
			this.initClient();		
		}
		
		// create HTTP GET request
		try {
			HttpPost postRequest = new HttpPost( this.serviceUrl );
			postRequest.addHeader( HTTP_CONTENT_TYPE , "text/xml; version=3.2" );
			postRequest.setEntity( new StringEntity( p.getContent() ) );		
			
			ResponseHandler< String > responseHandler = new ResponseHandler< String >() {
				@Override
				public String handleResponse( HttpResponse response )
						throws ClientProtocolException, IOException {
					// TODO Auto-generated method stub
					return null;
				}
			};
			
			String responseBody = httpClient.execute( postRequest, responseHandler );
			
			
			System.out.println("Response:\n" + responseBody);
		} catch (IOException e) {
			throw new GeneralLoggingException( getClass() + ":extractSemantics(): exception occured: " + e, "An exception occured while querying the TopicZoom WebService. See the logs for details." );
		}
			finally {
			try {
				this.httpClient.close();
			} catch( IOException e ) {
				this.clientClosed = true;
				Application.log( getClass() + ": extractSemantics: Exception when trying to close the http client: "+e, LogType.ERROR );
			}
		}
		
	}

	/**
	 * Initialize the HTTP client.
	 */
	protected void initClient() {
		this.httpClient = HttpClients.createDefault();		

		this.clientClosed = false;
	}

}
