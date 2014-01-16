/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class defines a model for an (e learning) course, e.g. 'Moodle'.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class Course implements Serializable, IDirectoryPortable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -241430860036615503L;

	/**
	 * 
	 * <p>
	 * The literals in this enumeration specifiy the types of learned words.<br />
	 * A type might be the name of a person for example.
	 * </p>
	 * 
	 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
	 * @since 05.12.2013
	 * 
	 */
	public enum LearnedWordTypes {
		/**
		 * A single person name.
		 */
		PERSON_NAME,
	};

	protected String title;
	protected int id;
	protected String shortName;
	protected String summary;
	protected String lang;
	protected Date creationDate;
	protected Date modificationDate;
	protected List< Board > boards;
	protected PersonNameCorpus personNameCorpus;
	protected Map< LearnedWordTypes, Set< String > > learnedWords;
	protected String url;
	protected Map< TagType, List< Tag > > tagMap;

	/**
	 * Create a course for which only a title is given.
	 * 
	 * @param courseTitle
	 */
	public Course( String courseTitle ) {
		this.setTitle( courseTitle );
		this.boards = new ArrayList< Board >();
		this.learnedWords = new HashMap< LearnedWordTypes, Set< String > >();

		this._initialize();
	}

	private void _initialize() {
		this.tagMap = new HashMap< TagType, List< Tag > >();
	}

	/**
	 * @return the personNameCorpus
	 */
	public PersonNameCorpus getPersonNameCorpus() {
		return personNameCorpus;
	}

	/**
	 * Set a completely new {@link PersonNameCorpus} instance.
	 * 
	 * @param personNameCorpus
	 *            the personNameCorpus to set
	 * @return
	 */
	public Course setPersonNameCorpus( PersonNameCorpus personNameCorpus ) {
		if( null == personNameCorpus ) {
			throw new IllegalArgumentException(
					getClass()
							+ "setPersonNameCorpus(): null value is not allowed for personnameCorpus" );
		}
		this.personNameCorpus = personNameCorpus;
		return this;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 * @return
	 */
	public Course setLang( String lang ) {
		this.lang = lang;
		return this;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 * @return
	 */
	public Course setTitle( String title ) {
		this.title = title;
		return this;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 * @return
	 */
	public Course setCreationDate( Date creationDate ) {
		this.creationDate = creationDate;
		return this;
	}

	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate
	 *            the modificationDate to set
	 * @return
	 */
	public Course setModificationDate( Date modificationDate ) {
		this.modificationDate = modificationDate;
		return this;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 * @return
	 */
	public Course setId( int id ) {
		this.id = id;
		return this;
	}


	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 * @return
	 */
	public Course setShortName( String shortName ) {
		this.shortName = shortName;
		return this;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 * @return
	 */
	public Course setSummary( String summary ) {
		this.summary = summary;
		return this;
	}

	/**
	 * Add another board to this course.
	 * 
	 * @param board
	 *            mustn't be null.
	 */
	public void addBoard( Board board ) {
		if( null == board ) {
			throw new IllegalArgumentException( getClass()
					+ "addBoard(): null given for board parameter." );
		}
		this.boards.add( board );
	}

	/**
	 * @return the board
	 */
	public List< Board > getBoards() {
		return this.boards;
	}

	/**
	 * Get a single board with the given name.
	 * 
	 * @param boardName
	 *            consider the method works case-insensitiv
	 * @return the matched {@link Board} or null if it wasn't found
	 */
	public Board getBoard( String boardName ) {
		if( null == boardName ) {
			throw new IllegalArgumentException( getClass()
					+ "getBoard(): null given for boardName parameter." );
		}

		for( Board board : this.boards ) {
			if( board.getTitle().trim().toLowerCase()
					.equals( boardName.trim().toLowerCase() ) ) {
				return board;
			}
		}

		return null; // not found
	}


	/**
	 * <p>
	 * Add a "learned word" defined by the given type.<br />
	 * Example: a learned word can be a person's name.
	 * </p>
	 * 
	 * @param singleWord
	 * @param wordType
	 *            the given type (defined in {@link LearnedWordTypes}).
	 */
	public void addLearnedWord( String singleWord, LearnedWordTypes wordType ) {
		// create word set first if this is the first word for the given type
		if( !this.learnedWords.containsKey( wordType ) ) {
			Set< String > wordSet = new HashSet< String >();
			this.learnedWords.put( wordType, wordSet );
		}

		// add the word to the existing set
		this.learnedWords.get( wordType ).add( singleWord );
	}

	/**
	 * Get the "learned words" for the given {@link LearnedWordTypes}.
	 * 
	 * @param wordType
	 * @return a Set of {@link String} or null if there are no words available.
	 */
	public Set< String > getLearnedWords( LearnedWordTypes wordType ) {
		return this.learnedWords.get( wordType );
	}

	/**
	 * Set the whole {@link Tag} list for the given {@link TagType}.
	 * 
	 * @param fetchedTags
	 * @param tagType
	 */
	public void setTags( List< Tag > fetchedTags, TagType tagType ) {
		this.tagMap.put( tagType, fetchedTags );
	}

	/**
	 * Add a single tag.
	 * 
	 * @param newTag
	 * @param tagType
	 */
	public void addTag( Tag newTag, TagType tagType ) {
		// create list if neccessary
		if( null == this.getTags( tagType ) ) {
			this.tagMap.put( tagType, new ArrayList< Tag >() );
		}

		// add to map
		this.getTags( tagType ).add( newTag );
	}

	/**
	 * Get the whole {@link Tag} list for the given {@link TagType}.
	 * 
	 * @param tagType
	 * @return
	 * @return might return null.
	 */
	public List< Tag > getTags( TagType tagType ) {
		return this.tagMap.get( tagType );
	}

	/**
	 * Return a boolean whether this Posting is tagged by TopicZoom Web Tagging.<br />
	 * The condition for a posting to be tagged is the existence of at least one
	 * tag.
	 * 
	 * @return
	 */
	public boolean isTopicZoomTagged() {
		if( null == this.getTags( TagType.TOPIC_ZOOM ) ) {
			return false;
		}

		return this.getTags( TagType.TOPIC_ZOOM ).size() > 0 ? true : false;
	}

	/**
	 * Return a boolean whether this Posting is tagged.<br />
	 * The condition for a posting to be tagged is the existence of at least one
	 * tag.
	 * 
	 * @return
	 */
	public boolean isTagged() {
		if( this.isTopicZoomTagged() ) {
			return true;
		}
		if( this.isNerTagged() ) {
			return true;
		}

		return false;
	}

	/**
	 * Get the number of all tags which enrich this {@link Posting}.
	 * 
	 * @return
	 */
	public int getNumberTags() {
		int numberTags = 0;

		for( List< Tag > tags : this.tagMap.values() ) {
			numberTags += tags.size();
		}
		return numberTags;
	}

	/**
	 * Return true if this posting is tagged by Named Entity Recognition (NER)
	 * tags
	 * 
	 * @return
	 */
	public boolean isNerTagged() {
		if( null == this.getTags( TagType.NER_TAG ) ) {
			return false;
		}

		return this.getTags( TagType.NER_TAG ).size() > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
	 */
	public String exportToTxt() {
		StringBuilder txtExport = new StringBuilder();

		txtExport.append( "ID: " + this.getId() + "\n" );
		if( null != this.getCreationDate() ) {
			txtExport.append( "CREATION_DATETIME: "
					+ this.getCreationDate().getTime() + "\n" );
		}
		if( null != this.getModificationDate() ) {
			txtExport.append( "MODIFICATION_DATETIME: "
					+ this.getModificationDate().getTime() + "\n" );
		}
		txtExport.append( "LANG: " + this.getLang() + "\n" );
		txtExport.append( "SHORT_NAME: " + this.getShortName() + "\n" );
		txtExport.append( "TITLE: " + this.getTitle() + "\n" );
		txtExport.append( "SUMMARY: " + this.getSummary() + "\n" );
		String url = this.getUrl();

		if( null != url ) {
			txtExport.append( "URL: " + url + "\n" );
		}

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

		return txtExport.toString();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.model.IDirectoryPortable#importFromTxt(java
	 * .lang.String, java.lang.String)
	 */
	public void importFromTxt( String key, String value ) {
		if( null == key || 0 == key.length() || null == value
				|| 0 == value.length() ) {
			throw new IllegalArgumentException(
					"Illegal value for key or value! Key: " + key + "; value: "
							+ value );
		}

		if( key.equals( "ID" ) ) {
			try {
				this.setId( Integer.parseInt( value ) );
			} catch( NumberFormatException e ) {
				Application
						.log( getClass()
								+ ":importFromTxt: couldn't parse given value to an integer ID. Given value: "
								+ value, LogType.ERROR );
			}
		} else if( key.equals( "CREATION_DATETIME" ) ) {
			try {
				this.setCreationDate( new Date( Long.parseLong( value ) ) );
			} catch( NumberFormatException e ) {
				Application
						.log( getClass()
								+ ":importFromTxt: couldn't parse given creation date to an Date . Given value: "
								+ value, LogType.ERROR );
			}
		} else if( key.equals( "MODIFICATION_DATETIME" ) ) {
			try {
				this.setModificationDate( new Date( Long.parseLong( value ) ) );
			} catch( NumberFormatException e ) {
				Application
						.log( getClass()
								+ ":importFromTxt: couldn't parse given modification date to an Date . Given value: "
								+ value, LogType.ERROR );
			}
		} else if( key.equals( "TITLE" ) ) {
			this.setTitle( value );
		} else if( key.equals( "LANG" ) ) {
			this.setLang( value );
		} else if( key.equals( "SHORT_NAME" ) ) {
			this.setShortName( value );
		} else if( key.equals( "SUMMARY" ) ) {
			this.setSummary( value );
		} else if( key.equals( "URL" ) ) {
			this.setUrl( value );
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
	protected void parseTopicZoomValue( final String value ) {
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
		
		
		Tag newTag = new TopicZoomTag( significance, degreegeneralization, weight, name, uri);
		this.addTag(newTag, TagType.TOPIC_ZOOM);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( boards == null ) ? 0 : boards.hashCode() );
		result = prime * result
				+ ( ( creationDate == null ) ? 0 : creationDate.hashCode() );
		result = prime * result + id;
		result = prime * result + ( ( lang == null ) ? 0 : lang.hashCode() );
		result = prime * result
				+ ( ( learnedWords == null ) ? 0 : learnedWords.hashCode() );
		result = prime
				* result
				+ ( ( modificationDate == null ) ? 0 : modificationDate
						.hashCode() );
		result = prime
				* result
				+ ( ( personNameCorpus == null ) ? 0 : personNameCorpus
						.hashCode() );
		result = prime * result
				+ ( ( shortName == null ) ? 0 : shortName.hashCode() );
		result = prime * result
				+ ( ( summary == null ) ? 0 : summary.hashCode() );
		result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
		result = prime * result + ( ( url == null ) ? 0 : url.hashCode() );
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		Course other = ( Course ) obj;
		if( boards == null ) {
			if( other.boards != null )
				return false;
		} else if( !boards.equals( other.boards ) )
			return false;
		if( creationDate == null ) {
			if( other.creationDate != null )
				return false;
		} else if( !creationDate.equals( other.creationDate ) )
			return false;
		if( id != other.id )
			return false;
		if( lang == null ) {
			if( other.lang != null )
				return false;
		} else if( !lang.equals( other.lang ) )
			return false;
		if( learnedWords == null ) {
			if( other.learnedWords != null )
				return false;
		} else if( !learnedWords.equals( other.learnedWords ) )
			return false;
		if( modificationDate == null ) {
			if( other.modificationDate != null )
				return false;
		} else if( !modificationDate.equals( other.modificationDate ) )
			return false;
		if( personNameCorpus == null ) {
			if( other.personNameCorpus != null )
				return false;
		} else if( !personNameCorpus.equals( other.personNameCorpus ) )
			return false;
		if( shortName == null ) {
			if( other.shortName != null )
				return false;
		} else if( !shortName.equals( other.shortName ) )
			return false;
		if( summary == null ) {
			if( other.summary != null )
				return false;
		} else if( !summary.equals( other.summary ) )
			return false;
		if( title == null ) {
			if( other.title != null )
				return false;
		} else if( !title.equals( other.title ) )
			return false;
		if( url == null ) {
			if( other.url != null )
				return false;
		} else if( !url.equals( other.url ) )
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "Course [getPersonNameCorpus()=" );
		builder.append( getPersonNameCorpus() );
		builder.append( ", getLang()=" );
		builder.append( getLang() );
		builder.append( ", getTitle()=" );
		builder.append( getTitle() );
		builder.append( ", getCreationDate()=" );
		builder.append( getCreationDate() );
		builder.append( ", getModificationDate()=" );
		builder.append( getModificationDate() );
		builder.append( ", getId()=" );
		builder.append( getId() );
		builder.append( ", getShortName()=" );
		builder.append( getShortName() );
		builder.append( ", getSummary()=" );
		builder.append( getSummary() );
		builder.append( ", getBoards()=" );
		builder.append( getBoards() );
		builder.append( ", exportToTxt()=" );
		builder.append( exportToTxt() );
		builder.append( ", hashCode()=" );
		builder.append( hashCode() );
		builder.append( ", getUrl()=" );
		builder.append( getUrl() );
		builder.append( "]" );
		return builder.toString();
	}

	/**
	 * Set the url String of this course.
	 * 
	 * @param url
	 * @return
	 */
	public Course setUrl( String url ) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		return this.url;
	}

}
