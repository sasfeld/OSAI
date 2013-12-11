/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <p>This class defines a model for an (e learning) course, e.g. 'Moodle'.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class Course {
	/**
	 * 
	 * <p>The literals in this enumeration specifiy the types of learned words.<br />
	 * A type might be the name of a person for example.</p>
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
	protected User docent;
	protected List< Board > boards;
	protected PersonNameCorpus personNameCorpus;
	protected Map< LearnedWordTypes, Set< String > > learnedWords;
	
	/**
	 * Create a course for which only a title is given.
	 * @param courseTitle
	 */
	public Course ( String courseTitle) {
		this.setTitle( courseTitle );
		
		this.boards = new ArrayList<Board>();
		
		this.personNameCorpus = new PersonNameCorpus();
	
		this.learnedWords = new HashMap< LearnedWordTypes,  Set< String >  >();
	}
	
	/**
	 * @return the personNameCorpus
	 */
	public PersonNameCorpus getPersonNameCorpus() {
		return personNameCorpus;
	}
	
	/**
	 * Set a completely new {@link PersonNameCorpus} instance.
	 * @param personNameCorpus the personNameCorpus to set
	 */
	public void setPersonNameCorpus( PersonNameCorpus personNameCorpus ) {
		if (null == personNameCorpus) {
			throw new IllegalArgumentException( getClass() + "setPersonNameCorpus(): null value is not allowed for personnameCorpus" );
		}
		this.personNameCorpus = personNameCorpus;
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
	 * Add another board to this course.
	 * @param board mustn't be null.
	 */
	public void addBoard(Board board) {
		if (null == board) {
			throw new IllegalArgumentException(getClass() + "addBoard(): null given for board parameter.");
		}
		this.boards.add( board );
	}
	/**
	 * @return the board
	 */
	public List<Board> getBoards() {
		return this.boards;
	}
	
	/**
	 * Get a single board with the given name.
	 * @param boardName consider the method works case-insensitiv
	 * @return the matched {@link Board} or null if it wasn't found
	 */
	public Board getBoard(String boardName) {
		if (null == boardName) {
			throw new IllegalArgumentException(getClass() + "getBoard(): null given for boardName parameter.");
		}
		
		for( Board board : this.boards ) {
			if (board.getTitle().trim().toLowerCase().equals( boardName.trim().toLowerCase() )) {
				return board;
			}
		}
		
		return null; // not found
	}
	
	/**
	 * @param docent the docent to set
	 */
	public void setDocent( User docent ) {
		this.docent = docent;
	}
	
	/**
	 * <p>Add a "learned word" defined by the given type.<br />
	 * Example: a learned word can be a person's name.
	 * </p>
	 * @param singleWord
	 * @param wordType the given type (defined in {@link LearnedWordTypes}).
	 */
	public void addLearnedWord( String singleWord, LearnedWordTypes wordType ) {
		// create word set first if this is the first word for the given type
		if ( ! this.learnedWords.containsKey( wordType )) {
			Set< String > wordSet = new HashSet<String>();
			this.learnedWords.put( wordType, wordSet );
		}
		
		// add the word to the existing set
		this.learnedWords.get( wordType ).add( singleWord );
	}
	
	/**
	 * Get the "learned words" for the given {@link LearnedWordTypes}.
	 * @param wordType
	 * @return a Set of {@link String} or null if there are no words available.
	 */
	public Set< String > getLearnedWords( LearnedWordTypes wordType ) {
		return this.learnedWords.get( wordType );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( boards == null ) ? 0 : boards.hashCode() );
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
		if( boards == null ) {
			if( other.boards != null )
				return false;
		} else if( !boards.equals( other.boards ) )
			return false;
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
