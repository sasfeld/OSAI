/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.util.HashSet;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * <p>This class offers functions to work with personal name corpora.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.12.2013
 *
 */
public class PersonNameCorpus {	
	/**
	 * 
	 * <p>This enumeration defines literals to be used for exchanging the types of given names.</p>
	 *
	 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
	 * @since 05.12.2013
	 *
	 */
	public enum PersonNameType {
		/**
		 * A prename can be either a first name or a middle name.
		 */
		PRENAME,
		LASTNAME
	};
	protected Set< String > prenames;
	protected Set< String > lastnames;

	/**
	 * Create a new corpus by handing in sets of prenames (meaning first and middle names) and lastnames.
	 * @param prenames
	 * @param lastnames
	 * @throws IllegalArgumentException if one of the arguments is null
	 */
	public PersonNameCorpus ( Set< String > prenames, Set< String > lastnames) {
		if ( null == prenames ) {
			throw new IllegalArgumentException("Null is not allowed for prenames");
		}
		if ( null == lastnames ) {
			throw new IllegalArgumentException("Null is not allowed for lastnames");
		}
		
		this.prenames = prenames;
		this.lastnames = lastnames;
	}
	

	/**
	 * Create a new bare corpus.
	 */
	public PersonNameCorpus() {
		this.prenames = new HashSet<String>();
		this.lastnames = new HashSet<String>();
	}
	
	/**
	 * Add a single prename (first or middle name). In the case sensitive mode a lower cased string will be added as well.
	 * @param prename
	 * @param caseSensitive
	 */
	public void fillPrename(String prename, boolean caseSensitive) {
		if (!caseSensitive) {
			this.prenames.add( prename.toLowerCase() );
		}
		
		this.prenames.add( prename );
	}
	
	/**
	 * Add a single lastname. In the case sensitive mode a lower-cased string will be added too.
	 * @param prename
	 * @param caseSensitive
	 */
	public void fillLastname(String prename, boolean caseSensitive) {
		if (!caseSensitive) {
			this.lastnames.add( prename.toLowerCase() );
		}
		
		this.lastnames.add( prename );
	}
	
	/**
	 * Get a list of prenames related to this corpus.
	 * @return
	 */
	public Set<String> getPrenames() {
		return this.prenames;
	}
	
	/**
	 * Get a list of lastnames related to this corpus.
	 * @return
	 */
	public Set<String> getLastnames() {
		return this.lastnames;
	}
	
	/**
	 * Check whether a given expected word is a prename.
	 * @param expectedWord which must really be a word. If several words are included, an exception is thrown.
	 * @return boolean true if the given word is a prename
	 */
	public boolean isPrename(String expectedWord, boolean caseSensitive ) {
		String workingWord = expectedWord.trim();
		
		boolean matched = this.prenames.contains( workingWord );
		if ( !matched && ! caseSensitive ) {
			matched = this.prenames.contains( workingWord.toLowerCase() );
		}
		
		return matched;
	}
	
	/**
	 * Check whether a given expected word is a lastname.
	 * @param expectedWord which must really be a word. If several words are included, an exception is thrown.
	 * @return boolean true if the given word is a prename
	 */
	public boolean isLastame(String expectedWord, boolean caseSensitive ) {
		String workingWord = expectedWord.trim();
		
		boolean matched = this.lastnames.contains( workingWord );
		if ( !matched && ! caseSensitive ) {
			matched = this.lastnames.contains( workingWord.toLowerCase() );
		}
		
		return matched;
	}

	/**
	 * Fill a name using the given type.
	 * @param nameType
	 * @param name
	 * @param caseSensitive
	 */
	public void fillName( PersonNameType nameType, String name, boolean caseSensitive ) {
		if (nameType.equals( PersonNameType.PRENAME )) {
			this.fillPrename( name, caseSensitive );
		}
		else if (nameType.equals( PersonNameType.LASTNAME )) {
			this.fillLastname( name, caseSensitive );
		}
		
		else { // log because this method needs to be extended
			Application.log( getClass()+"fillName(): the given PersonNameType ("+nameType+") isn't supported yet.", LogType.WARNING );
		}
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PersonNameCorpus [getPrenames()=" + getPrenames()
				+ ", getLastnames()=" + getLastnames() + "]";
	}

}
