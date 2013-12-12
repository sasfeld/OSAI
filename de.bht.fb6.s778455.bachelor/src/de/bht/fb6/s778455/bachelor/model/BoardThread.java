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
		return "BoardThread [creationDate="
				+ creationDate + ", creator=" + creator + ", title=" + title
				+ "]";
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
	 */
	public void setFirstPostingId( int postingId ) {
		this.firstPostingId = postingId;
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
	 */
	public void setEndDate( Date date ) {
		this.endDate = date;		
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
}
