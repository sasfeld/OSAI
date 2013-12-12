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
}
