/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.luebeck;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.ner.GermanNerAnonymizationStrategy;
import de.bht.fb6.s778455.bachelor.importer.luebeck.LuebeckXmlImporter;
import de.bht.fb6.s778455.bachelor.model.Course;

/**
 * <p>This class contains tests of the {@link LuebeckXmlImporter}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 *
 */
public class LuebeckXmlImporterStrategyTest {
	private LuebeckXmlImporter strategy;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.strategy = new LuebeckXmlImporter();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		strategy = null;
	}

	@Test
	/**
	 * Test protected method.
	 */
	public void testImportCourses() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = LuebeckXmlImporter.class
				.getDeclaredMethod( "importCourses", File.class );
		method.setAccessible( true );
		
		Map< Integer, Course > courseMap = ( Map< Integer, Course > ) method.invoke( this.strategy, new File("./data/importer/luebeck" ));
		assertEquals( 1, courseMap.size() );
		
		System.out.println(courseMap);
	}

}
