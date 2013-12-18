/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.tools.DateComparator;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>This class represents a Thread of a board, e.g. a 'Moodle board'.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class BoardThread extends AUserContribution {
	protected List< Posting > postings;
	protected Board belongingBoard;
	private int firstPostingId;
	private Date endDate;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ( ( endDate == null ) ? 0 : endDate.hashCode() );
		result = prime * result + firstPostingId;
		result = prime * result
				+ ( ( postings == null ) ? 0 : postings.hashCode() );
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
		BoardThread other = ( BoardThread ) obj;
		if( belongingBoard == null ) {
			if( other.belongingBoard != null )
				return false;
		} 
		if( endDate == null ) {
			if( other.endDate != null )
				return false;
		} else if( !endDate.equals( other.endDate ) )
			return false;
		if( firstPostingId != other.firstPostingId )
			return false;
		if( postings == null ) {
			if( other.postings != null )
				return false;
		} else if( !postings.equals( other.postings ) )
			return false;
		return true;
	}

	/**
	 * Create a new bare BoardThread.
	 */
	public BoardThread() {
		this.postings = new ArrayList< Posting >(); 
	}
	
	/**
	 * Create a new thread with a link to the parent {@link Board}.
	 * @param belongingBoard
	 */
	public BoardThread( Board belongingBoard ) {
		this.belongingBoard = belongingBoard;
		
		this.postings = new ArrayList< Posting >(); 
	}
	
	/**
	 * @return the belongingBoard
	 */
	public Board getBelongingBoard() {
		return belongingBoard;
	}

	/**
	 * Add a posting to the board thread.
	 * The Posting will be sorted immediatly.
	 * @param p
	 */
	public void addPosting(Posting p) {
		Comparator< AUserContribution > dateComparator = new DateComparator();
		
		postings.add( p );
		// sort ascending
		Collections.sort( this.postings, dateComparator );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "BoardThread [" );
		builder.append( "getPostings()=" );
		builder.append( getPostings() );
		builder.append( ", getFirstPostingId()=" );
		builder.append( getFirstPostingId() );
		builder.append( ", getEndDate()=" );
		builder.append( getEndDate() );
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

	/**
	 * Get a sorted list of {@link Posting}.
	 * {@link Posting} are sorted by the creation date.
	 * @return
	 */
	public List< Posting > getPostings() {
		return this.postings;
	}

	/**
	 * Set the id of the first posting. 
	 * @param postingId
	 * @return 
	 */
	public BoardThread setFirstPostingId( int postingId ) {
		this.firstPostingId = postingId;
		
		return this;
	}
	
	/**
	 * Get the id of the first posting.
	 * @return
	 */
	public int getFirstPostingId() {
		return this.firstPostingId;
	}

	/**
	 * Set the end date, e.g. if the thread was closed (set in moodle).
	 * @param date
	 * @return 
	 */
	public BoardThread setEndDate( Date date ) {
		this.endDate = date;		
		
		return this;
	}
	
	/**
	 * Get the end date.
	 * @return
	 */
	public Date getEndDate() {
		return this.endDate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
	 */
	public String exportToTxt() {
		StringBuilder txtExport = new StringBuilder();
		
		txtExport.append( super.exportToTxt() );
		txtExport.append( "FIRST_POSTING_ID: " + this.getFirstPostingId() + "\n");
		
		return txtExport.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#importFromTxt(java.lang.String, java.lang.String)
	 */
	public void importFromTxt( String key, String value ) {
		super.importFromTxt( key, value );
		
		if( key.equals( "FIRST_POSTING_ID" ) ) {
			try {
				this.setFirstPostingId( Integer.parseInt( value ) );
			} catch( NumberFormatException e ) {
				Application
						.log( getClass()
								+ ":importFromTxt: couldn't parse given value to an integer firstPostingId. Given value: "
								+ value, LogType.ERROR );
			}
		} 
	}
}
