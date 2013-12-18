/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.auditorium;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class realizes an {@link AImportStrategy} for the data recieved by the Auditorium of the University of Dresden.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 *
 */
public class AuditoriumImportStrategy extends AImportStrategy {

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream(java.io.InputStream)
	 */
	@Override
	public Set< Course > importBoardFromStream( InputStream inputStream ) {
		throw new UnsupportedOperationException( getClass() + " doesn't support an import from stream yet." );
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile(java.io.File)
	 */
	@Override
	public Collection< Course > importBoardFromFile( File inputFile )
			throws GeneralLoggingException {
		// ignore input file. DB connection configured in the importer.properties will be used
		AuditoriumDbQuerier querier = new AuditoriumDbQuerier();
		
		Map< Integer, Course > courseMap = querier.fetchCourses();
		Map< Integer, Board > boardMap = querier.fetchBoards( courseMap );
		Map< Integer, BoardThread > threadMap = querier.fetchBoardThreads( boardMap );
		querier.fetchPostings( threadMap );
		
		// set singleton corpus on each course
		PersonNameCorpus corpus = ServiceFactory.getPersonNameCorpusSingleton();
		for( Course course : courseMap.values() ) {
			course.setPersonNameCorpus( corpus );
		}
		
		this.fillFromFile( null, corpus, null );
		
		return courseMap.values();
	}

	/* (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.importer.AImportStrategy#fillFromFile(java.io.File, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType)
	 */
	@Override
	public PersonNameCorpus fillFromFile( File personCorpus,
			PersonNameCorpus corpusInstance, PersonNameType nameType )
			throws GeneralLoggingException {
		AuditoriumDbQuerier querier = new AuditoriumDbQuerier();
		
		
		// fill prenames
		Set< String > fetchedPrenames = querier.fetchPrenames(); 
		for( String prename : fetchedPrenames ) {
			corpusInstance.fillPrename( prename );
		}
		
		// fill lastnames
		Set< String > fetchedLastnames = querier.fetchLastnames();
		for( String lastname : fetchedLastnames ) {
			corpusInstance.fillLastname( lastname );
		}	
	
		
		return corpusInstance;
	}

}
