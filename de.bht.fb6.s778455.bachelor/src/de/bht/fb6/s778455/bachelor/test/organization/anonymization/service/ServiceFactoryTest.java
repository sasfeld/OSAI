/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.test.organization.anonymization.service;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bht.fb6.s778455.bachelor.organization.IConfigReader;
import de.bht.fb6.s778455.bachelor.organization.anonymization.service.ServiceFactory;

/**
 * <p>This class contains tests of the {@link ServiceFactory} class.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 15.11.2013
 *
 */
public class ServiceFactoryTest {

	@Test
	/**
	 * Test of the method:
	 * @see de.bht.fb6.s778455.bachelor.organization.anonymization.service.ServiceFactory#getConfigReader()
	 */
	public void test() {
		// test whether the correct instance is returned
		assertTrue( ServiceFactory.getConfigReader() instanceof IConfigReader );
	}

}
