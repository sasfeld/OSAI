/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.semantic.extraction.topiczoom;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.model.Tag;
import de.bht.fb6.s778455.bachelor.organization.FileUtil;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.topiczoom.TopicZoomResponseHandler;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>This class contains tests of the {@link TopicZoomResponseHandler}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 *
 */
public class TopicZoomResponseHandlerTest extends NoLoggingTest {
	protected static final String PATH_SAMPLE_FILE = "./data/unittests/topcizoomsample.xml";
	protected TopicZoomResponseHandler handler;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.handler = new TopicZoomResponseHandler();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.handler = null;
	}

	@Test
	public void testFetchTags() throws GeneralLoggingException {
		File testFile = new File( PATH_SAMPLE_FILE );
		String fileContent = FileUtil.readFile( testFile, "false" );
		
		List< Tag > fetchedTags = this.handler.fetchTags( fileContent );
		
		assertTrue( null != fetchedTags );
	}

}
