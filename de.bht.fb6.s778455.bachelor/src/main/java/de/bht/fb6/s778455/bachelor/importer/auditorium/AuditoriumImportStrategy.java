/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.auditorium;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>
 * This class realizes an {@link AImportStrategy} for the data recieved by the
 * Auditorium of the University of Dresden.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 * 
 */
public class AuditoriumImportStrategy extends AImportStrategy {
    /**
     * Name of the course set defined by this import strategy. Make sure to
     * chosse a name which is valid in a URI.
     */
    public static final String COURSE_SET_NAME = "auditorium_dresden";
    
    protected final String host;
    protected final String user;
    protected final String pw;
    protected final String dbName;
    
    /**
     * Create a new strategy instance.
     * 
     * Hand in the SQL DB connectivity data.
     * 
     * @param host
     * @param user
     * @param pw
     * @param dbName
     */
    public AuditoriumImportStrategy(final String host, final String user, final String pw, final String dbName)
    {
        this.host = host;
        this.user = user;
        this.pw = pw;
        this.dbName = dbName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromStream
     * (java.io.InputStream)
     */
    @Override
    public Set<Course> importBoardFromStream(final InputStream inputStream) {
        throw new UnsupportedOperationException(this.getClass()
                + " doesn't support an import from stream yet.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#importBoardFromFile
     * (java.io.File)
     */
    @Override
    public LmsCourseSet importBoardFromFile(final File inputFile)
            throws GeneralLoggingException {
        final LmsCourseSet auditoriumCourseSet = new LmsCourseSet(
                super.removeIllegalChars(COURSE_SET_NAME));
        
        // ignore input file. DB connection configured in the
        // importer.properties will be used
        final AuditoriumDbQuerier querier = new AuditoriumDbQuerier(this.host, this.user,
                this.pw, this.dbName);

        final Map<Integer, Course> courseMap = querier
                .fetchCourses(auditoriumCourseSet);
        final Map<Integer, Board> boardMap = querier.fetchBoards(courseMap);
        final Map<Integer, BoardThread> threadMap = querier
                .fetchBoardThreads(boardMap);
        querier.fetchPostings(threadMap);

        // set singleton corpus on each course
        final PersonNameCorpus corpus = ServiceFactory
                .getPersonNameCorpusSingleton();
        for (final Course course : courseMap.values()) {
            course.setPersonNameCorpus(corpus);
        }

        this.fillFromFile(null, corpus, null);

        auditoriumCourseSet.addAll(courseMap.values());
        return auditoriumCourseSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.importer.AImportStrategy#fillFromFile(java
     * .io.File, de.bht.fb6.s778455.bachelor.model.PersonNameCorpus,
     * de.bht.fb6.s778455.bachelor.model.PersonNameCorpus.PersonNameType)
     */
    @Override
    public PersonNameCorpus fillFromFile(final File personCorpus,
            final PersonNameCorpus corpusInstance, final PersonNameType nameType)
            throws GeneralLoggingException {        
        // ignore input file. DB connection configured in the
        // importer.properties will be used
        final AuditoriumDbQuerier querier = new AuditoriumDbQuerier(this.host, this.user,
                this.pw, this.dbName);

        // fill prenames
        final Set<String> fetchedPrenames = querier.fetchPrenames();
        for (final String prename : fetchedPrenames) {
            corpusInstance.fillPrename(prename);
        }

        // fill lastnames
        final Set<String> fetchedLastnames = querier.fetchLastnames();
        for (final String lastname : fetchedLastnames) {
            corpusInstance.fillLastname(lastname);
        }

        return corpusInstance;
    }

}
