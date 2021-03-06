/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization.service;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.auditorium.AuditoriumImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.experimental.DirectoryImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodlePostgreSqlImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.MoodleXmlImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.moodle.OliverLuebeckStrategy;
import de.bht.fb6.s778455.bachelor.importer.organization.ConfigReader;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.PersonNameCorpus;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.tools.IdComparator;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.xml.XmlExtractor;

/**
 * <p>This class realizes a ServiceFactory for the importer module.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class ServiceFactory {
    protected static ServiceFactory instance;
    
	protected static IConfigReader configReader;
	private static PersonNameCorpus personNameCorpus;
	protected static AImportStrategy dirImportStrat;
	
	/**
	 * Get the singleton config reader.
	 * @return
	 */
	public static IConfigReader getConfigReader() {
		if(null == configReader) {
			configReader = new ConfigReader();
		}
		return configReader;
	}

	/**
	 * Get the {@link PersonNameCorpus} singleton instance.
	 * @return
	 */
	public static PersonNameCorpus getPersonNameCorpusSingleton() {
		if(null == personNameCorpus) {
			personNameCorpus = new PersonNameCorpus();
		}
		return personNameCorpus;
	}
	
	/**
	 * Get the {@link DirectoryImportStrategy}.
	 * @return
	 */
	public static AImportStrategy getDirectoryImportStrategy() {
	    if(null == dirImportStrat) {
	        dirImportStrat = newDirectoryImportStrategy();
	    }
	    
	    return dirImportStrat;
	}

	/**
	 * Get the singleton ServiceFactory.
	 * @return
	 */
    public static ServiceFactory getInstance() {
       if ( null == instance ) {
           instance = new ServiceFactory();
       }
       
       return instance;
    }

	/**
	 * Let the fabric method create a new instance of {@link Course}.
	 * 
	 * @param courseId
	 * @param courseName must not be null
	 * @param language 
	 * @return
	 * @throws NullPointerException if one of the arguments is null
	 */
	public static Course newCourse(int courseId, String courseName, LmsCourseSet lmsCourseSet, Language language) {
		if (null == courseName) {
			throw new NullPointerException("Null is not allowed for parameter 2");
		}
		if (null == lmsCourseSet) {
			throw new NullPointerException("Null is not allowed for parameter 3");
		}
		
		Course newCourse = new Course(courseName, lmsCourseSet);
		newCourse.setId(courseId);
		newCourse.setLanguage(language);
		// add further logic for extensions
		
		return newCourse;
	}

	/**
	 * Let the fabric method create a new instance of {@link Board}.
	 * @param boardId
	 * @param boardName
	 * @param boardCourse
	 * @param boardLanguage
	 * @return
	 * @throws NullPointerException if one of the arguments is null
	 */
	public static Board newBoard(int boardId, String boardName, Course boardCourse,
			Language boardLanguage) {
		if (null == boardName) {
			throw new NullPointerException("Null is not allowed for parameter 2");
		}
		if (null == boardCourse) {
			throw new NullPointerException("Null is not allowed for parameter 3");
		}
		if (null == boardLanguage) {
			throw new NullPointerException("Null is not allowed for parameter 4");
		}
		
		Board newBoard = new Board(boardCourse, boardName);
		newBoard.setId(boardId);
		newBoard.setLang(boardLanguage);
		
		return newBoard;
	}

	/**
	 * Get an default {@link XmlExtractor} without any specific namespace configuration (for XML document where tags don't have namespaces, such as 'rdf:class' etc.)
	 * @return
	 * @throws NullPointerException if one of the arguments is null
	 * @throws GeneralLoggingException if the {@link XmlExtractor} couldn't be initialized
	 */
	public static XmlExtractor getXmlExtractor(File xmlFile) throws GeneralLoggingException {
		if (null == xmlFile) {
			throw new NullPointerException("Null is not allowed for parameter 1");
		}
		
		XmlExtractor newExtractor = new XmlExtractor(xmlFile.getAbsolutePath(), Collections.<String, String> emptyMap());
		return newExtractor;
	}

	/**
	 * Let the factory method create a new instance of {@link BoardThread}.
	 * @param threadNumber
	 * @param threadTitle
	 * @param threadBoard
	 * @param boardLanguage
	 * @return
	 * @throws NullPointerException if one of the arguments is null
	 */
	public static BoardThread newBoardThread(int threadNumber,
			String threadTitle, Board threadBoard, Language boardLanguage) {
		if (null == threadTitle) {
			throw new NullPointerException("Null is not allowed for parameter 1");
		}
		if (null == threadBoard) {
			throw new NullPointerException("Null is not allowed for parameter 2");
		}
		
		BoardThread newBoardThread = new BoardThread(threadBoard);
		newBoardThread.setTitle(threadTitle);
		newBoardThread.setId(threadNumber);
		newBoardThread.setLang(boardLanguage);
		
		return newBoardThread;
	}

	/**
	 * Let the factory method create a new instance of {@link Posting}.
	 * @param postId
	 * @param content
	 * @param newThread
	 * @param postingLanguage
	 * @return
	 * @throws NullPointerException if one of the arguments is null
	 */
	public static Posting newPosting(int postId, String content,
			BoardThread newThread, Language postingLanguage) {
		if (null == content) {
			throw new NullPointerException("Null is not allowed for parameter 2");
		}
		if (null == newThread) {
			throw new NullPointerException("Null is not allowed for parameter 3");
		}
		if (null == postingLanguage) {
			throw new NullPointerException("Null is not allowed for parameter 4");
		}
		
		Posting newPosting = new Posting(newThread);
		newPosting.setId(postId);
		newPosting.setContent(content);
		newPosting.setLang(postingLanguage);
		
		return newPosting;
	}

	/**
	 * Create a new {@link IdComparator} instance.
	 * @return
	 */
	public static Comparator<Course> newIdComparator() {
		IdComparator newComparator = new IdComparator();
		return newComparator;
	}
	
	/**
	 * Return an instance of the auditorium db strategy. 
	 * 
	 * Inject dependencies and configuration values.
	 * 
	 * @return
	 */
	public static AImportStrategy newAuditoriumDbStrategy()
	{
	    // read DB connectivity from config
        String host = ((ConfigReader) getConfigReader())
                .getAuditoriumHost();
        String user = ((ConfigReader) getConfigReader())
                .getAuditoriumUser();
        String pw = ((ConfigReader) getConfigReader())
                .getAuditoriumPw();
        String dbName = ((ConfigReader) getConfigReader())
                .getAuditoriumDbName();        
        
	   AuditoriumImportStrategy auditoriumImportStrategy = new AuditoriumImportStrategy(
	           host, user, pw, dbName);
	   
	   return auditoriumImportStrategy;
	}
	
	/**
	 * Return a new instance of DirectoryImportStrategy.
	 * 
	 * Inject all dependencies from config and the service factory.
	 * 
	 * @return
	 */
	public static AImportStrategy newDirectoryImportStrategy()
	{
	    DirectoryImportStrategy directoryImportStrategy = new DirectoryImportStrategy();
	    
	    // inject dependencies
	    directoryImportStrategy.setBoardSpecificImport( getConfigReader().fetchValue(
                IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC ));
	    directoryImportStrategy.setEncoding( getConfigReader().fetchValue(
                IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_ENCODING ));
	    directoryImportStrategy.setPrenamesFile(getConfigReader()
                    .fetchValue(
                            IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_NAMECORPUS_PRENAMES ));
	    directoryImportStrategy.setLastnamesFile(getConfigReader().fetchValue(
                            IConfigKeys.IMPORT_STRATEGY_DIRECTORYIMPORT_NAMECORPUS_LASTNAMES ));
	    directoryImportStrategy.setPersonCorpus(getPersonNameCorpusSingleton());
	    
	    
        return directoryImportStrategy;
	}
	
	 /**
     * This fabric method creates a new instance of {@link OliverLuebeckStrategy}.
     * Define additional creation logic here.
     * @return
     */
    public static AImportStrategy newOliverLuebeckStrategy() {
        OliverLuebeckStrategy strategy = new OliverLuebeckStrategy();
        return strategy;
    }
    
    /**
     * Return a new instance of MoodleXmlImporterStrategy.
     * 
     * Inject all dependencies from config and the service factory.
     */
    public static AImportStrategy newMoodleXmlStrategy()
    {
        MoodleXmlImportStrategy strategy = new MoodleXmlImportStrategy();
        return strategy;        
    }
    
    /**
     * Return a new instance of MoodlePostgreSqlImportStrategy.
     * 
     * Inject all dependencies from config and the service factory.
     * 
     * @return
     */
    public static AImportStrategy newPostgreDumpStrategy()
    {
        MoodlePostgreSqlImportStrategy strategy = new MoodlePostgreSqlImportStrategy();
        
        // set config values
        strategy.setBoardSpecificPersonCorpus(getConfigReader()
                .fetchValue(
                        IConfigKeys.IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC )
                .trim().toLowerCase());
        
        return strategy;
    }
}
