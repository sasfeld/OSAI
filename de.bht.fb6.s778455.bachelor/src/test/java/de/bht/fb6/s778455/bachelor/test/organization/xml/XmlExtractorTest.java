/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.organization.xml;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bht.fb6.s778455.bachelor.organization.xml.XmlExtractor;
import de.bht.fb6.s778455.bachelor.test.framework.NoLoggingTest;

/**
 * <p>
 * This class contains tests of the {@link XmlExtractor}
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 * 
 */
public class XmlExtractorTest extends NoLoggingTest {

	private XmlExtractor exctractor;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.exctractor = new XmlExtractor( "./data/unittests/xmlSample.xml",
				new HashMap< String, String >() );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.exctractor = null;
	}

	@Test
	public void testBuildXpath() {
		// query matching single node
		String query;

		query = "//user[1]/username/text()";

		String result;
		result = ( String ) this.exctractor.buildXPath( query, false );
		assertEquals( "anon1", result );

		// query matching multiple nodes
		query = "//user/username/text()";
		String[] results;
		results = ( String[] ) this.exctractor.buildXPath( query, true );

		assertEquals( 24, results.length );
	}
}
