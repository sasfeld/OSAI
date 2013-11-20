/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

/**
 * 
 * <p>This class defines a model for an (e learning) course, e.g. 'Moodle'.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class Course {
	protected String title;
	protected int id;
	protected User docent;
	protected Board board;
	
	/**
	 * Create a course for which only a title is given.
	 * @param courseTitle
	 */
	public Course ( String courseTitle) {
		this.setTitle( courseTitle );
		
		this.board = new Board(this);
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle( String title ) {
		this.title = title;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId( int id ) {
		this.id = id;
	}
	
	/**
	 * @return the docent
	 */
	public User getDocent() {
		return docent;
	}
	
	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * @param docent the docent to set
	 */
	public void setDocent( User docent ) {
		this.docent = docent;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( docent == null ) ? 0 : docent.hashCode() );
		result = prime * result + id;
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
		Course other = ( Course ) obj;
		if( docent == null ) {
			if( other.docent != null )
				return false;
		} else if( !docent.equals( other.docent ) )
			return false;
		if( id != other.id )
			return false;
		if( title == null ) {
			if( other.title != null )
				return false;
		} else if( !title.equals( other.title ) )
			return false;
		return true;
	}
}
