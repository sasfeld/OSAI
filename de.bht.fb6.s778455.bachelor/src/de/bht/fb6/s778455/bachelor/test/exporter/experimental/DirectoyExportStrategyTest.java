/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.exporter.experimental;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.experimental.DirectoryExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p>This class realizes tests of the {@link DirectoryExportStrategy}.</p>
 * 
 * <p>The idea behind this test is to create a sample configuration of models and to export it to the local file system.</p>
 * 
 * <p>The test checks the new folder for correct syntax and structure.</p>
 * 
 * <p>You can configure the testdata folder in the exporter.properties file.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 24.11.2013
 *
 */
public class DirectoyExportStrategyTest {
	protected AExportStrategy exportStrategy;
	private File testFolder;

	/*
	 * ##################################
	 * #
	 * # test preparation
	 * #
	 * ##################################
	 */
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.exportStrategy = new DirectoryExportStrategy();
		
		this.testFolder = new File(ServiceFactory.getConfigReader().fetchValue( IConfigKeys.EXPORT_STRATEGY_DIRECTORYEXPORT_TESTFOLDER ));
		if ( !this.testFolder.exists() ) {
			this.testFolder.mkdir();
		}
		this.testFolder.setWritable( true );
		this.testFolder.setReadable( true );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.exportStrategy = null;
		
		this.testFolder = null;
	}
	
	/*
	 * ##################################
	 * #
	 * # tests
	 * #
	 * ##################################
	 */
	@Test
	public void testExport() throws GeneralLoggingException {			
		Course course = new Course( "Sample course" );
		// input boards
		Board sampleBoard1 = new Board( course, "Sample board" );
		
		BoardThread sampleThread1 = new BoardThread();
		sampleThread1.setTitle( "sampleThread1" );
		sampleThread1.setCreationDate( new Date());
		
		Posting samplePosting1 = new Posting();
		samplePosting1.setContent( "This is a sample posting.\nAnd a newline." );
		samplePosting1.setCreationDate( new Date() );		
		samplePosting1.setTaggedContent( "This content is tagged by <I-PERS>Max Mustermann</I-PERS>." );
		sampleThread1.addPosting( samplePosting1  );
		
		Posting samplePosting2 = new Posting();
		samplePosting2.setContent( "This is a another posting" );
		samplePosting2.setCreationDate( new Date(samplePosting1.getCreationDate().getTime() + 1000 * 60 * 60 * 24)); // + 1 day
		samplePosting2.setTaggedContent( "This content is tagged by <I-PERS>Max Mustermann</I-PERS>." );
		sampleThread1.addPosting( samplePosting2  );
		
		sampleBoard1.addThread( sampleThread1  );
		
		BoardThread sampleThread2 = new BoardThread();
		sampleThread2.setTitle( "sampleThread2" );
		sampleThread2.setCreationDate( new Date(sampleThread1.getCreationDate().getTime() + 1000 * 60 * 60 * 24));
		sampleThread2.addPosting( samplePosting2 );
		sampleThread2.addPosting( samplePosting1 );
		
		sampleBoard1.addThread( sampleThread2 );
		
		course.addBoard( sampleBoard1 );
		
		Set< Course > courseSet = new HashSet<Course>();	
		courseSet.add( course );
		
		this.exportStrategy.exportToFile( courseSet, this.testFolder );
		
		// assert file structure
		File course1File = new File( this.testFolder, course.getTitle());
		
		File board1File = new File( course1File, sampleBoard1.getTitle() );
		assertTrue( board1File.exists() );
		assertTrue( board1File.isDirectory() );
		
		File thread1File = new File( board1File, sampleThread1.getTitle() );
		assertTrue( thread1File.exists() );
		assertTrue( thread1File.isDirectory() );
		
		File samplePosting1File = new File( thread1File, "posting1.txt" );
		assertTrue( samplePosting1File.exists() );
		assertTrue( samplePosting1File.isFile() );
		assertTrue( samplePosting1File.getAbsolutePath().endsWith( ".txt" ));
		
		File samplePosting2File = new File( thread1File, "posting2.txt" );
		assertTrue( samplePosting2File.exists() );
		assertTrue( samplePosting2File.isFile() );
		assertTrue( samplePosting2File.getAbsolutePath().endsWith( ".txt" ));
		
		File thread2File = new File( board1File, sampleThread2.getTitle() );
		assertTrue( thread2File.exists() );
		assertTrue( thread2File.isDirectory() );
		
		samplePosting1File = new File( thread2File, "posting1.txt" );
		assertTrue( samplePosting1File.exists() );
		assertTrue( samplePosting1File.isFile() );
		assertTrue( samplePosting1File.getAbsolutePath().endsWith( ".txt" ));
		
		samplePosting2File = new File( thread2File, "posting2.txt" );
		assertTrue( samplePosting2File.exists() );
		assertTrue( samplePosting2File.isFile() );
		assertTrue( samplePosting2File.getAbsolutePath().endsWith( ".txt" ));		
	}

}
