/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.util.Date;

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

	/*
	 * (non-Javadoc)
	 * 
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
		result = prime * result + ( ( title == null ) ? 0 : title.hashCode() );
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
		if( title == null ) {
			if( other.title != null )
				return false;
		} else if( !title.equals( other.title ) )
			return false;
		return true;
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
