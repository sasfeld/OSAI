/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.model;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class describes the a Posting in a {@link BoardThread}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 * 
 */
public class Posting extends AUserContribution {
	public enum TagType {
		/**
		 * key for tags extracted by the TopicZoom WebTag service.
		 */
		TOPIC_ZOOM, 
		/**
		 * key for tags won by Stanford Named Entity Recognition (NER)
		 */
		NER_TAGS,
	}

	/**
	 * Untagged content.
	 */
	protected String content;
	/**
	 * Tagged content (by NER).
	 */
	protected String taggedContent;
	protected int parentPostingId;
	protected BoardThread belongingThread;
	protected String postType;
	
	/**
	 * Create a Posting with a link to the belonging thread {@link Thread}
	 * 
	 * @param thread
	 */
	public Posting( BoardThread thread ) {
		this.belongingThread = thread;

		this._initialize();
	}

	/**
	 * Create a bare posting.
	 */
	public Posting() {
		this.belongingThread = null;

		this._initialize();
	}

	private void _initialize() {
		this.tagMap = new HashMap< Posting.TagType, List< Tag > >();
	}

	/**
	 * @return the belongingThread
	 */
	public BoardThread getBelongingThread() {
		return belongingThread;
	}

	/**
	 * @return the parentPostingId
	 */
	public int getParentPostingId() {
		return parentPostingId;
	}

	/**
	 * @param parentPostingId
	 *            the parentPostingId to set
	 * @return
	 */
	public Posting setParentPostingId( int parentPostingId ) {
		this.parentPostingId = parentPostingId;
		return this;
	}

	/**
	 * @return the untagged content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the untagged content to set
	 * @return
	 */
	public Posting setContent( String content ) {
		this.content = content;
		return this;
	}

	/**
	 * @return the taggedContent
	 */
	public String getTaggedContent() {
		return taggedContent;
	}

	/**
	 * @param taggedContent
	 *            the taggedContent to set
	 * @return
	 */
	public Posting setTaggedContent( String taggedContent ) {
		this.taggedContent = taggedContent;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
	 */
	public String exportToTxt() {
		StringBuilder txtExport = new StringBuilder();

		txtExport.append( super.exportToTxt() );

		// tags
		List< Tag > topicZoomTags = this.getTags( TagType.TOPIC_ZOOM );

		if( null != topicZoomTags ) {
			for( Tag tag : topicZoomTags ) {
				if( tag instanceof TopicZoomTag ) {
					TopicZoomTag topicZoomTag = ( TopicZoomTag ) tag;
					txtExport.append( "TOPIC_ZOOM_TAG: " + "value:"
							+ topicZoomTag.getValue() + ";" + "weight:"
							+ topicZoomTag.getWeight() + ";" + "significance:"
							+ topicZoomTag.getSignificance() + ";"
							+ "degreegeneralization:"
							+ topicZoomTag.getDegreeGeneralization() + ";"
							+ "uri:" + topicZoomTag.getUri() + "\n" );
				} else {
					Application
							.log( getClass()
									+ ":exportToTxt(): the saved tag is not of type TopicZoomTag. There must be some error in the extraction process. Only add TopicZoomTags when using TopicZoom.",
									LogType.ERROR );
				}
			}
		}

		txtExport.append( "PARENT_POSTING_ID: " + this.getParentPostingId()
				+ "\n" );
		String postingType = this.getPostingType();
		if( null != postingType ) {
			txtExport.append( "POSTING_TYPE: " + postingType + "\n" );
		}
		txtExport.append( "CONTENT:\n" );

		if( null != this.getContent() ) {
			String[] postingLines = this.getContent().split( "\n" );

			for( String line : postingLines ) {
				txtExport.append( line + "\n" );
			}
		}

		txtExport.append( "TAGGED_CONTENT:\n" );
		if( null != this.getTaggedContent() ) {
			String[] taggedPostingLines = this.getTaggedContent().split( "\n" );

			for( String taggedLine : taggedPostingLines ) {
				txtExport.append( taggedLine );
			}
		}

		return txtExport.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.model.AUserContribution#importFromTxt(java
	 * .lang.String, java.lang.String)
	 */
	public void importFromTxt( String key, String value ) {
		super.importFromTxt( key, value );

		if( key.equals( "PARENT_POSTING_ID" ) ) {
			try {
				this.setParentPostingId( Integer.parseInt( value ) );
			} catch( NumberFormatException e ) {
				Application
						.log( getClass()
								+ ":importFromTxt: couldn't parse given value to an integer parentPostingId. Given value: "
								+ value, LogType.ERROR );
			}
		} else if( key.equals( "CONTENT" ) ) {
			this.setContent( value );
		} else if( key.equals( "TAGGED_CONTENT" ) ) {
			this.setTaggedContent( value );
		} else if( key.equals( "POSTING_TYPE" ) ) {
			this.setPostingType( value );
		} else if( key.equals( "TOPIC_ZOOM_TAG" ) ) {
			this.parseTopicZoomValue( value );
		}
	}

	/**
	 * Parse the value for the key "TOPIC_ZOOM_TAG" within a txt file and fill
	 * the Posting's tags.
	 * 
	 * @param value
	 */
	private void parseTopicZoomValue( final String value ) {
		/*
		 * structure:
		 * value:xyz;weight:1.0;significance:3.0;degreegeneralization:
		 * 6;uri:testuri
		 */

		String[] splitValues = value.split( ";" );		

		String name = "";
		double weight = 0;
		double significance = 0;
		int degreegeneralization = 0;
		String uri = "";
		
		for( String splitValue : splitValues ) {
			final Pattern pKeyValue = Pattern.compile( "([a-z]+):(.*)" );
			final Matcher mKeyValue = pKeyValue.matcher( splitValue );
			
			while( mKeyValue.find() ) {
				final String key = mKeyValue.group(1);
				final String kValue = mKeyValue.group(2);			
				
				switch( key ) {
				case "value":
					name = kValue;
					break;
				case "weight":
					weight = Double.parseDouble( kValue );
					break;
				case "significance":
					significance = Double.parseDouble( kValue );
					break;
				case "degreegeneralization":
					degreegeneralization = Integer.parseInt( kValue );
				case "uri":
					uri = kValue;
				default:
					Application.log( getClass() + ":parseTopicZoomValue(): key " + key + " couldn't be matched.", LogType.ERROR );;
				}
				
			}
		}		
		
		
		Tag newTag = new TopicZoomTag( significance, degreegeneralization, weight, name, uri );
		this.addTag(newTag, TagType.TOPIC_ZOOM);
	}

	

	public Posting setPostingType( String postType ) {
		this.postType = postType;
		return this;
	}

	public String getPostingType() {
		return this.postType;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ( ( belongingThread == null ) ? 0 : belongingThread
						.hashCode() );
		result = prime * result
				+ ( ( content == null ) ? 0 : content.hashCode() );
		result = prime * result + parentPostingId;
		result = prime * result
				+ ( ( postType == null ) ? 0 : postType.hashCode() );
		result = prime * result
				+ ( ( taggedContent == null ) ? 0 : taggedContent.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		if( this == obj )
			return true;
		if( !super.equals( obj ) )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		Posting other = ( Posting ) obj;
		if( belongingThread == null ) {
			if( other.belongingThread != null )
				return false;
		} else if( !belongingThread.equals( other.belongingThread ) )
			return false;
		if( content == null ) {
			if( other.content != null )
				return false;
		} else if( !content.equals( other.content ) )
			return false;
		if( parentPostingId != other.parentPostingId )
			return false;
		if( postType == null ) {
			if( other.postType != null )
				return false;
		} else if( !postType.equals( other.postType ) )
			return false;
		if( taggedContent == null ) {
			if( other.taggedContent != null )
				return false;
		} else if( !taggedContent.equals( other.taggedContent ) )
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "Posting [getBelongingThread()=" );
		builder.append( getBelongingThread() );
		builder.append( ", getParentPostingId()=" );
		builder.append( getParentPostingId() );
		builder.append( ", getContent()=" );
		builder.append( getContent() );
		builder.append( ", getTaggedContent()=" );
		builder.append( getTaggedContent() );
		builder.append( ", getPostingType()=" );
		builder.append( getPostingType() );
		builder.append( ", toString()=" );
		builder.append( super.toString() );
		builder.append( "]" );
		return builder.toString();
	}	
	
}
