/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.tools.DateComparator;

/**
 * 
 * <p>
 * This class defines a model for a Board. A Board is included in a
 * {@link Course}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class Board extends AUserContribution {
	protected Course course;
	protected List< BoardThread > boardThreads;
	protected String type;
	protected String intro;

	/**
	 * Create a new Board. A Board is included in the given course.
	 * 
	 * @param course
	 */
	public Board( Course course ) {
		super();
		
		if( null == course ) {
			throw new IllegalArgumentException( getClass()
					+ ": null argument in constructor!" );
		}
		this.course = course;

		this.boardThreads = new ArrayList< BoardThread >();

	}

	/**
	 * Create a new Board isntance using a given course and a specific title.
	 * 
	 * @param course
	 * @param boardTitle
	 */
	public Board( Course course, String boardTitle ) {
		super();
		
		if( null == course || null == boardTitle ) {
			throw new IllegalArgumentException( getClass()
					+ ": null argument in constructor!" );
		}

		this.course = course;
		this.title = boardTitle;

		this.boardThreads = new ArrayList< BoardThread >();
	}

	/**
	 * @return the intro text
	 */
	public String getIntro() {
		return intro;
	}

	/**
	 * @param intro
	 *            the intro text to set
	 */
	public void setIntro( String intro ) {
		this.intro = intro;
	}

	/**
	 * Add a {@link BoardThread}. Sort it immediatly.
	 * 
	 * @param boardThread
	 */
	public void addThread( BoardThread boardThread ) {
		Comparator< AUserContribution > dateComparator = new DateComparator();
		
		this.boardThreads.add( boardThread );
		// sort ascending
		Collections.sort( this.boardThreads, dateComparator );
	}

	/**
	 * Get a sorted list of {@link BoardThread}. {@link BoardThread} are sorted
	 * by the creation date.
	 * 
	 * @return
	 */
	public List< BoardThread > getBoardThreads() {
		return this.boardThreads;
	}

	/**
	 * Get the course to which the board instance belongs.
	 * 
	 * @return
	 */
	public Course getBelongingCourse() {
		return this.course;
	}

	/**
	 * Set the board's type (moodle specific key: e.g. "news" for a news board
	 * or "general")
	 * 
	 * @param type
	 */
	public void setType( String type ) {
		this.type = type;
	}

	/**
	 * Get the board's type (moodle specific key: e.g. "news" for a news board
	 * or "general")
	 * 
	 * @return
	 */
	public String getType() {
		return this.type;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
	 */
	public String exportToTxt() {
		StringBuilder txtExport = new StringBuilder();
		
		txtExport.append( super.exportToTxt() );
		txtExport.append( "INTRO: " + this.getIntro() + "\n" );
		txtExport.append( "TYPE: " + this.getType() + "\n" );
		
		return txtExport.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#importFromTxt(java.lang.String, java.lang.String)
	 */
	public void importFromTxt( String key, String value ) {
		super.importFromTxt( key, value );
		
		if( key.equals( "INTRO" ) ) {
			this.setIntro( value );
		} else if( key.equals( "TYPE" ) ) {
			this.setType( value );
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "Board [getIntro()=" );
		builder.append( getIntro() );
		builder.append( ", getBoardThreads()=" );
		builder.append( getBoardThreads() );
		builder.append( ", getType()=" );
		builder.append( getType() );
		builder.append( ", exportToTxt()=" );
		builder.append( exportToTxt() );
		builder.append( ", getId()=" );
		builder.append( getId() );
		builder.append( ", getModificationDate()=" );
		builder.append( getModificationDate() );
		builder.append( ", getCreationDate()=" );
		builder.append( getCreationDate() );
		builder.append( ", getCreator()=" );
		builder.append( getCreator() );
		builder.append( ", getTitle()=" );
		builder.append( getTitle() );
		builder.append( "]" );
		return builder.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ( ( boardThreads == null ) ? 0 : boardThreads.hashCode() );
		result = prime * result + ( ( intro == null ) ? 0 : intro.hashCode() );
		result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
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
		Board other = ( Board ) obj;
		if( boardThreads == null ) {
			if( other.boardThreads != null )
				return false;
		} else if( !boardThreads.equals( other.boardThreads ) )
			return false;
		if( course == null ) {
			if( other.course != null )
				return false;
		}
		if( intro == null ) {
			if( other.intro != null )
				return false;
		} else if( !intro.equals( other.intro ) )
			return false;
		if( type == null ) {
			if( other.type != null )
				return false;
		} else if( !type.equals( other.type ) )
			return false;
		return true;
	}

}
