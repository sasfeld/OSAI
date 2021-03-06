/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.tools.TopicZoomTagComparator;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;

/**
 * <p>
 * Instances of this strategy aim to use the WebService offered by TopicZoom to
 * enrich {@link Posting} instances with auto-generated tags.
 * </p>
 * <p>
 * URL: <a href="http://www.topiczoom.de/">http://topiczoom.de</a>
 * </p>
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
	protected String serviceUrl;
	protected boolean clientClosed;

	/**
	 * Create and instantiate a new topic zoom strategy.
	 */
	public TopicZoomExtractionStrategy() {
		this.initClient();
		this.serviceUrl = ServiceFactory.getConfigReader().fetchValue(
				IConfigKeys.SEMANTICS_EXTRACTION_TOPICZOOM_ENDPOINT );
	}

	protected List< Tag > getTagsFromString( final String inputString )
			throws GeneralLoggingException {
		if( this.clientClosed ) {
			Application
					.log( getClass()
							+ ":extractSemantics(): the client was closed due an error within a previous http request to the topic zoom web service. However, I will reinstanciate it.",
							LogType.INFO );
		}

		// create HTTP GET request
		try {
			this.initClient();

			HttpPost postRequest = new HttpPost( this.serviceUrl );
			postRequest.addHeader( HTTP_CONTENT_TYPE, "text/xml; version=3.2" );
			postRequest.setEntity( new StringEntity( inputString ) );

			final ResponseHandler< String > responseHandler = new TopicZoomResponseHandler();

			// execute request
			final String responseBody = httpClient.execute( postRequest,
					responseHandler );

			// fetch tags from xml response
			final List< Tag > fetchedTags = ( ( TopicZoomResponseHandler ) responseHandler )
					.fetchTags( responseBody );

			// sort tags decending
			final TopicZoomTagComparator tagComparator = new TopicZoomTagComparator();
			Collections.sort( fetchedTags, tagComparator );
			// reverse so the order is descending
			Collections.reverse( fetchedTags );

			return fetchedTags;
		} catch( IOException e ) {
			throw new GeneralLoggingException(
					getClass() + ":getTagsFromString(): exception occured: "
							+ e,
					"An exception occured while querying the TopicZoom WebService. See the logs for details." );
		} finally {
			try {
				this.httpClient.close();
			} catch( IOException e ) {
				this.clientClosed = true;
				Application
						.log( getClass()
								+ ": getTagsFromString: Exception when trying to close the http client: "
								+ e, LogType.ERROR );
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#
	 * extractSemantics(de.bht.fb6.s778455.bachelor.model.Posting)
	 */
	@Override
	public void extractSemantics( final AUserContribution userContribution )
			throws GeneralLoggingException {
		// first check if the posting already has TopicZoomTags when the
		// lazyMode is enabled
		if( super.isLazyMode()
				&& null != userContribution.getTags( TagType.TOPIC_ZOOM )
				&& 0 < userContribution.getTags( TagType.TOPIC_ZOOM ).size() ) {
			Application
					.log( getClass()
							+ ":extractSemantics(): the lazy mode is enabled and the user contribution with id "
							+ userContribution.getTitle()
							+ "already has TopiczoomTags. I will not poll TopicZoom again.",
							LogType.INFO );
			return;
		}
		// else: proceed

		String strSend = "";
		if( userContribution instanceof Posting ) {
			// posting: sent the content of the posting
			strSend = ( ( Posting ) userContribution ).getContent();
		} else if( userContribution instanceof Board ) {
			Board b = ( Board ) userContribution;

			// board: concat title and intro
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append( b.getTitle() + "\n\n" ).append( b.getIntro() );

			strSend = strBuilder.toString();
		} else {
			// return because the given userContribution isn't supported
			return;
		}

		// fetch tags from xml response
		final List< Tag > fetchedTags = this.getTagsFromString( strSend );

		// add tags to posting
		userContribution.setTags( fetchedTags, TagType.TOPIC_ZOOM );
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#
	 * extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
	 */
	public void extractSemantics( final Course course )
			throws GeneralLoggingException {
		// first check if the posting already has TopicZoomTags when the
		// lazyMode is enabled
		if( super.isLazyMode() && null != course.getTags( TagType.TOPIC_ZOOM )
				&& 0 < course.getTags( TagType.TOPIC_ZOOM ).size() ) {
			Application
					.log( getClass()
							+ ":extractSemantics(): the lazy mode is enabled and the course with id "
							+ course.getTitle()
							+ "already has TopiczoomTags. I will not poll TopicZoom again.",
							LogType.INFO );
			return;
		}

		// concat the title and summary of the course to get a maxmimum of words
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append( course.getTitle() + "\n\n" )
				.append( course.getSummary() );
		
		// fetch tags from xml response
		final List< Tag > fetchedTags = this.getTagsFromString( strBuilder.toString() );

		// add tags to posting
		course.setTags( fetchedTags, TagType.TOPIC_ZOOM );
	}

	/**
	 * Initialize the HTTP client.
	 */
	protected void initClient() {
		this.httpClient = HttpClients.createDefault();

		this.clientClosed = false;
	}
}
