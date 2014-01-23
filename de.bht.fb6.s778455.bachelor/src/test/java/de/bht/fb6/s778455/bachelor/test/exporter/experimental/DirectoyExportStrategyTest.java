/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.exporter.experimental;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import de.bht.fb6.s778455.bachelor.model.Language;
import de.bht.fb6.s778455.bachelor.model.NerTag;
import de.bht.fb6.s778455.bachelor.model.PosTag;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.model.TopicZoomTag;
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
		final Course course = new Course( "Sample course" );
		course.setUrl( "http://test.de" );
		course.setLanguage( Language.GERMAN );
		course.addTag( new PosTag( "NN", 0.0, "Beispiel", "beispiel.xad" ), TagType.POS_TAG );
		course.addTag( new NerTag( "I-LOC", 0.0, "Berlin", "berlin.xad" ), TagType.NER_TAG );
		course.addTag( new TopicZoomTag( 5, 1, 3, "Berlin", "214152" ), TagType.TOPIC_ZOOM );
		
		// input boards
		final Board sampleBoard1 = new Board( course, "Sample board" );
		
		final BoardThread sampleThread1 = new BoardThread();
		sampleThread1.setTitle( "sampleThread1" );
		sampleThread1.setCreationDate( new Date());
		
		final Posting samplePosting1 = new Posting();
		samplePosting1.setContent( "This is a sample posting.\nAnd a newline." );
		samplePosting1.setCreationDate( new Date() );		
		samplePosting1.setTaggedContent( "This content is tagged by <I-PERS>Max Mustermann</I-PERS>." );
		samplePosting1.setPostingType( "question" );
		samplePosting1.setLang( Language.GERMAN );
		sampleThread1.addPosting( samplePosting1  );
		
		final ArrayList< Tag > tagList = new ArrayList<Tag>();
		tagList.add( new TopicZoomTag( 6, 4, 3, "test", "testuri" ) );
		tagList.add( new TopicZoomTag( 3, 2, 1, "test", "testuri2" ) );	
		
		samplePosting1.setTags( tagList, TagType.TOPIC_ZOOM );
		samplePosting1.addTag( new NerTag( "I-LOC", -1.0, "Berlin", "berlin" ), TagType.NER_TAG );
		samplePosting1.addTag( new PosTag( "NN", 0.0, "Variable", "" ), TagType.POS_TAG );
		
		final Posting samplePosting2 = new Posting();
		samplePosting2.setContent( "This is a another posting" );
		samplePosting2.setCreationDate( new Date(samplePosting1.getCreationDate().getTime() + 1000 * 60 * 60 * 24)); // + 1 day
		samplePosting2.setTaggedContent( "This content is tagged by <I-PERS>Max Mustermann</I-PERS>." );
		sampleThread1.addPosting( samplePosting2  );
		
		sampleBoard1.addThread( sampleThread1  );
		
		final BoardThread sampleThread2 = new BoardThread();
		sampleThread2.setTitle( "sampleThread2" );
		sampleThread2.setCreationDate( new Date(sampleThread1.getCreationDate().getTime() + 1000 * 60 * 60 * 24));
		sampleThread2.addPosting( samplePosting2 );
		sampleThread2.addPosting( samplePosting1 );
		
		sampleBoard1.addThread( sampleThread2 );
		
		course.addBoard( sampleBoard1 );
		
		final Set< Course > courseSet = new HashSet<Course>();	
		courseSet.add( course );
		
		this.exportStrategy.exportToFile( courseSet, this.testFolder );
		
		// assert file structure
		final File course1File = new File( this.testFolder, "Sample_course");
		
		final File board1File = new File( course1File, "Sample_board" );
		assertTrue( board1File.exists() );
		assertTrue( board1File.isDirectory() );
		
		final File thread1File = new File( board1File, sampleThread1.getTitle() );
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
		
		final File thread2File = new File( board1File, sampleThread2.getTitle() );
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
	
	@Test
	public void testRemoveIllegalChars() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Method method = DirectoryExportStrategy.class
				.getDeclaredMethod( "removeIllegalChars", String.class );
		method.setAccessible( true );
		
		final String input = "ein..nichterlaubter..filename !!!";
		final String expected = "ein_nichterlaubter_filename____";
		
		assertEquals(expected, (String) method.invoke( this.exportStrategy, input ));
	}

}
