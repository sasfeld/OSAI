/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <p>This class defines a model for a Board. A Board is included in a {@link Course}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class Board extends AUserContribution {
	public enum LearnedWordTypes {
		/**
		 * A single person name.
		 */
		PERSON_NAME,
	};
	
	protected Course course;
	protected List< BoardThread > boardThreads;
	protected Map< LearnedWordTypes, Set< String > > learnedWords;
	
	/**
	 * Create a new Board. A Board is included in the given course.
	 * @param course
	 */
	public Board( Course course) {
		this.course = course;
		
		this.boardThreads = new ArrayList<BoardThread>();
		
		this.learnedWords = new HashMap< LearnedWordTypes,  Set< String >  >();
	}

	/**
	 * Add a {@link BoardThread}. Sort it immediatly.
	 * @param boardThread
	 */
	public void addThread( BoardThread boardThread ) {
		Comparator< BoardThread > dateComparator = new Comparator< BoardThread >() {			
			@Override
			/**
			 * Compare the two board threads. A board thread is less than another, if the creation date is less than the other.
			 * @param o1
			 * @param o2
			 * @return 
			 */
			public int compare( BoardThread o1, BoardThread o2 ) {
				if (o1.getCreationDate().getTime() < o2.getCreationDate().getTime()) {
					return -1;
				}
				if (o1.getCreationDate().getTime() > o2.getCreationDate().getTime()) {
					return 1;
				}
				return 0; // o1 == o2
			}
		};
		this.boardThreads.add( boardThread );
		// sort ascending
		Collections.sort( this.boardThreads, dateComparator );		
	}
	
	/**
	 * Get a sorted list of {@link BoardThread}.
	 * {@link BoardThread} are sorted by the creation date.
	 * @return
	 */
	public List< BoardThread > getBoardThreads() {
		return this.boardThreads;
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
}
