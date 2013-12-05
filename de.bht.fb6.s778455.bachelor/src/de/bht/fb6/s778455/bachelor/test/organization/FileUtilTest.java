/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.organization;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.organization.FileUtil;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class realizes tests of the {@link FileUtil} class.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.12.2013
 *
 */
public class FileUtilTest {

	private static final String TEST_FILE = "./data/unittests/fileLineBased.txt";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFileLineBased() throws GeneralLoggingException {
		File testFile = new File( TEST_FILE );
		List< String > lines = FileUtil.readFileLineBased( testFile );
		
		assertEquals( 3, lines.size() );
		
		assertEquals( "First line", lines.get( 0 ));
		assertEquals( "Second line", lines.get( 1 ));
		assertEquals( "Third line", lines.get( 2 ));
	}

}
