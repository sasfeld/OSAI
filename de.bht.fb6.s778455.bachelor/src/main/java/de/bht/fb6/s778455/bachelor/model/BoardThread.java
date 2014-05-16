/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;
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
				+ ( ( this.endDate == null ) ? 0 : this.endDate.hashCode() );
		result = prime * result + this.firstPostingId;
		result = prime * result
				+ ( ( this.postings == null ) ? 0 : this.postings.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( this == obj )
			return true;
		if( !super.equals( obj ) )
			return false;
		if( this.getClass() != obj.getClass() )
			return false;
		final BoardThread other = ( BoardThread ) obj;
		if( this.belongingBoard == null ) {
			if( other.belongingBoard != null )
				return false;
		} 
		if( this.endDate == null ) {
			if( other.endDate != null )
				return false;
		} else if( !this.endDate.equals( other.endDate ) )
			return false;
		if( this.firstPostingId != other.firstPostingId )
			return false;
		if( this.postings == null ) {
			if( other.postings != null )
				return false;
		} else if( !this.postings.equals( other.postings ) )
			return false;
		return true;
	}

	/**
	 * Create a new bare BoardThread.
	 */
	public BoardThread() {
		super();
		
		this.postings = new ArrayList< Posting >(); 
	}
	
	/**
	 * Create a new thread with a link to the parent {@link Board}.
	 * @param belongingBoard
	 */
	public BoardThread( final Board belongingBoard ) {
		super( belongingBoard.getBelongingCourse() );
		
		this.belongingBoard = belongingBoard;
		
		this.postings = new ArrayList< Posting >(); 
	}
	
	/**
	 * @return the belongingBoard
	 */
	public Board getBelongingBoard() {
		return this.belongingBoard;
	}

	/**
	 * Add a posting to the board thread.
	 * The Posting will be sorted immediatly.
	 * @param p
	 */
	public void addPosting(final Posting p) {
		final Comparator< AUserContribution > dateComparator = new DateComparator();
		
		this.postings.add( p );
		// sort ascending
		Collections.sort( this.postings, dateComparator );
	}
	
	 @Override
	    /*
	     * (non-Javadoc)
	     * 
	     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
	     */
	    public URI getRdfUri() throws URISyntaxException {
	        final URI uri = new URI( super.prepareRdfUri() + "boardthread" + "/"
	                + this.getId() );

	        return uri;
	    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append( "BoardThread [" );
		builder.append( "getPostings()=" );
		builder.append( this.getPostings() );
		builder.append( ", getFirstPostingId()=" );
		builder.append( this.getFirstPostingId() );
		builder.append( ", getEndDate()=" );
		builder.append( this.getEndDate() );
		builder.append( ", exportToTxt()=" );
		builder.append( this.exportToTxt() );
		builder.append( ", getId()=" );
		builder.append( this.getId() );
		builder.append( ", getModificationDate()=" );
		builder.append( this.getModificationDate() );
		builder.append( ", getCreationDate()=" );
		builder.append( this.getCreationDate() );
		builder.append( ", getTitle()=" );
		builder.append( this.getTitle() );
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
	public BoardThread setFirstPostingId( final int postingId ) {
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
	public BoardThread setEndDate( final Date date ) {
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
	@Override
    public String exportToTxt() {
		final StringBuilder txtExport = new StringBuilder();
		
		txtExport.append( super.exportToTxt() );
		txtExport.append( "FIRST_POSTING_ID: " + this.getFirstPostingId() + "\n");
		
		return txtExport.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#importFromTxt(java.lang.String, java.lang.String)
	 */
	@Override
    public void importFromTxt( final String key, final String value ) {
		super.importFromTxt( key, value );
		
		if( key.equals( "FIRST_POSTING_ID" ) ) {
			try {
				this.setFirstPostingId( Integer.parseInt( value ) );
			} catch( final NumberFormatException e ) {
				Application
						.log( this.getClass()
								+ ":importFromTxt: couldn't parse given value to an integer firstPostingId. Given value: "
								+ value, LogType.ERROR );
			}
		} 
	}
}
