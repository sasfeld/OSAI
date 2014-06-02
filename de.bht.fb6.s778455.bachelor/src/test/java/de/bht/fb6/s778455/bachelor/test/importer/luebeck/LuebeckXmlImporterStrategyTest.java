/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.luebeck;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.moodle.MoodleXmlImporterStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>This class contains tests of the {@link MoodleXmlImporterStrategy}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 *
 */
public class LuebeckXmlImporterStrategyTest extends NoLoggingTest {
	private MoodleXmlImporterStrategy strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.strategy = new MoodleXmlImporterStrategy();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.strategy = null;
	}

	@Test
	/**
	 * Test protected method.
	 */
	public void testImportCourses() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Method method = MoodleXmlImporterStrategy.class
				.getDeclaredMethod( "importCourses", File.class, LmsCourseSet.class );
		method.setAccessible( true );
		
		final LmsCourseSet courseSet = new LmsCourseSet( "test course set" );
		@SuppressWarnings( "unchecked" )
        final
		Map< Integer, Course > courseMap = ( Map< Integer, Course > ) method.invoke( this.strategy, new File("./data/importer/luebeck"), courseSet );
		assertEquals( 12, courseMap.size() );
		
		for( final Course c : courseMap.values() ) {
			System.out.println("++++++++++++++++++");
			System.out.println("Course: ");
			System.out.println(c);
			
			for( final Board board : c.getBoards() ) {
				System.out.println("----------------------");
				System.out.println("Board: ");
				System.out.println(board);
				System.out.println("----------------------");
				
				for( final BoardThread thread : board.getBoardThreads() ) {
					System.out.println("########################");
					System.out.println("Thread: ");
					System.out.println(thread);
					for( final Posting p : thread.getPostings() ) {
						System.out.println("////////////////////");
						System.out.println("Posting: ");
						System.out.println(p);
						System.out.println("////////////////////");
					}
					System.out.println("########################");
				}
			}
			System.out.println("++++++++++++++++++");
		}
	}

}
