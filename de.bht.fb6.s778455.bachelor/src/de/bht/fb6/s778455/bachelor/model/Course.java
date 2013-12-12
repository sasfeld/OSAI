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
public class Course implements Serializable {
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
	protected User docent;
	protected List< Board > boards;
	protected PersonNameCorpus personNameCorpus;
	protected Map< LearnedWordTypes, Set< String > > learnedWords;

	/**
	 * Create a course for which only a title is given.
	 * 
	 * @param courseTitle
	 */
	public Course( String courseTitle ) {
		this.setTitle( courseTitle );
		this.boards = new ArrayList< Board >();
		this.personNameCorpus = new PersonNameCorpus();
		this.learnedWords = new HashMap< LearnedWordTypes, Set< String > >();
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
	 */
	public void setPersonNameCorpus( PersonNameCorpus personNameCorpus ) {
		if( null == personNameCorpus ) {
			throw new IllegalArgumentException(
					getClass()
							+ "setPersonNameCorpus(): null value is not allowed for personnameCorpus" );
		}
		this.personNameCorpus = personNameCorpus;
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
	 */
	public void setLang( String lang ) {
		this.lang = lang;
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
	 */
	public void setTitle( String title ) {
		this.title = title;
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
	 */
	public void setCreationDate( Date creationDate ) {
		this.creationDate = creationDate;
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
	 */
	public void setModificationDate( Date modificationDate ) {
		this.modificationDate = modificationDate;
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
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName( String shortName ) {
		this.shortName = shortName;
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
	 */
	public void setSummary( String summary ) {
		this.summary = summary;
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
	 * @param docent
	 *            the docent to set
	 */
	public void setDocent( User docent ) {
		this.docent = docent;
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

		return txtExport.toString();
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
		result = prime * result + ( ( docent == null ) ? 0 : docent.hashCode() );
		result = prime * result + id;
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
		if( docent == null ) {
			if( other.docent != null )
				return false;
		} else if( !docent.equals( other.docent ) )
			return false;
		if( id != other.id )
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
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Course [getPersonNameCorpus()=" + getPersonNameCorpus()
				+ ", getTitle()=" + getTitle() + ", getCreationDate()="
				+ getCreationDate() + ", getModificationDate()="
				+ getModificationDate() + ", getId()=" + getId()
				+ ", getDocent()=" + getDocent() + ", getShortName()="
				+ getShortName() + ", getSummary()=" + getSummary()
				+ ", getBoards()=" + getBoards() + "]";
	}
}
