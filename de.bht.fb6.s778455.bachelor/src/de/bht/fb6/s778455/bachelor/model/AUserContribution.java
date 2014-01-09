/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.model.Posting.TagType;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class describes any content that is made by a user.
 * </p>
 * <p>
 * Those can be: a {@link Board}, a {@link BoardThread} and a {@link Posting} of
 * course.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public abstract class AUserContribution implements IDirectoryPortable {
	protected Date creationDate;
	protected Date modificationDate;
	protected Creator creator;
	protected String title;
	protected int id;
	
	protected Map< TagType, List< Tag > > tagMap;


	

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
	public AUserContribution setId( int id ) {
		this.id = id;
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
	public AUserContribution setModificationDate( Date modificationDate ) {
		this.modificationDate = modificationDate;
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
	public AUserContribution setCreationDate( Date creationDate ) {
		this.creationDate = creationDate;
		return this;
	}

	/**
	 * @return the creator
	 */
	public Creator getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 * @return 
	 */
	public AUserContribution setCreator( Creator creator ) {
		this.creator = creator;
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
	public AUserContribution setTitle( String title ) {
		this.title = title;
		return this;
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
	 * @param newTag
	 * @param tagType
	 */
	public void addTag( Tag newTag, TagType tagType ) {
		// create list if neccessary
		if ( null == this.getTags( tagType ) ) {
			this.tagMap.put( tagType, new ArrayList<Tag>() );
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
	 * The condition for a posting to be tagged is the existence of at least one tag.
	 * @return
	 */
	public boolean isTopicZoomTagged() {
		if ( null == this.getTags( TagType.TOPIC_ZOOM )) {
			return false;
		}
		
		return this.getTags( TagType.TOPIC_ZOOM ).size() > 0 ? true : false;
	}

	/**
	 * Return a boolean whether this Posting is tagged.<br />
	 * The condition for a posting to be tagged is the existence of at least one tag.
	 * @return
	 */
	public boolean isTagged() {
		if ( this.isTopicZoomTagged() ) {
			return true;
		}
		if ( this.isNerTagged() ) {
			return true;
		}
		
		return false;
	}

	/**
	 * Get the number of all tags which enrich this {@link Posting}.
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
	 * Return true if this posting is tagged by Named Entity Recognition (NER) tags
	 * @return
	 */
	public boolean isNerTagged() {
		if ( null == this.getTags( TagType.NER_TAGS )) {
			return false;
		}
		
		return this.getTags( TagType.NER_TAGS ).size() > 0 ? true : false;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ( ( creationDate == null ) ? 0 : creationDate.hashCode() );
		result = prime * result
				+ ( ( creator == null ) ? 0 : creator.hashCode() );
		result = prime * result + id;
		result = prime
				* result
				+ ( ( modificationDate == null ) ? 0 : modificationDate
						.hashCode() );
		result = prime * result + ( ( tagMap == null ) ? 0 : tagMap.hashCode() );
		result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
		return result;
	}

	/* (non-Javadoc)
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
		AUserContribution other = ( AUserContribution ) obj;
		if( creationDate == null ) {
			if( other.creationDate != null )
				return false;
		} else if( !creationDate.equals( other.creationDate ) )
			return false;
		if( creator == null ) {
			if( other.creator != null )
				return false;
		} else if( !creator.equals( other.creator ) )
			return false;
		if( id != other.id )
			return false;
		if( modificationDate == null ) {
			if( other.modificationDate != null )
				return false;
		} else if( !modificationDate.equals( other.modificationDate ) )
			return false;
		if( tagMap == null ) {
			if( other.tagMap != null )
				return false;
		} else if( !tagMap.equals( other.tagMap ) )
			return false;
		if( title == null ) {
			if( other.title != null )
				return false;
		} else if( !title.equals( other.title ) )
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "AUserContribution [getId()=" );
		builder.append( getId() );
		builder.append( ", getModificationDate()=" );
		builder.append( getModificationDate() );
		builder.append( ", getCreationDate()=" );
		builder.append( getCreationDate() );
		builder.append( ", getCreator()=" );
		builder.append( getCreator() );
		builder.append( ", getTitle()=" );
		builder.append( getTitle() );
		builder.append( ", isTopicZoomTagged()=" );
		builder.append( isTopicZoomTagged() );
		builder.append( ", isTagged()=" );
		builder.append( isTagged() );
		builder.append( ", getNumberTags()=" );
		builder.append( getNumberTags() );
		builder.append( ", isNerTagged()=" );
		builder.append( isNerTagged() );
		builder.append( "]" );
		return builder.toString();
	}
	
	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.IDirectoryPortable#exportToTxt()
	 */
	@Override
	public String exportToTxt() {
		StringBuilder exportStr = new StringBuilder();

		exportStr.append( "ID: " + this.getId() + "\n" );
		if( null != this.getCreationDate() ) {
			exportStr.append( "CREATION_DATETIME: "
					+ this.getCreationDate().getTime() + "\n" );
		}
		if( null != this.getModificationDate() ) {
			exportStr.append( "MODIFICATION_DATETIME: "
					+ this.getModificationDate().getTime() + "\n" );
		}
		exportStr.append( "TITLE: " + this.getTitle() + "\n" );

		return exportStr.toString();
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.IDirectoryPortable#importFromTxt(java.lang.String, java.lang.String)
	 */
	@Override
	public void importFromTxt( String key, String value ) {
		if( null == key || 0 == key.length() || null == value
				|| 0 == value.length() ) {
			throw new IllegalArgumentException(
					"Illegal value for key or value! Key: " + key +"; value: " + value  );
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
		} else if ( key.equals( "TITLE" ) ) {
			this.setTitle( value );
		}

	}
	
	
}
