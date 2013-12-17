/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.importer.auditorium;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.importer.auditorium.AuditoriumDbQuerier;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class contains tests of the {@link AuditoriumDbQuerier}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 17.12.2013
 *
 */
public class AuditoriumDbQuerierTest {

	protected AuditoriumDbQuerier dbQuerier;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.dbQuerier = new AuditoriumDbQuerier();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.dbQuerier = null;
	}

	@Test
	public void testFetchCourses() throws GeneralLoggingException {
		Collection< Course > fetchedCourses = this.dbQuerier.fetchCourses();
		
		assertTrue ( null != fetchedCourses );
		
		assertTrue( 0 < fetchedCourses.size() );
		
		for( Course course : fetchedCourses ) {
			System.out.println(course);
		}
	}

}
