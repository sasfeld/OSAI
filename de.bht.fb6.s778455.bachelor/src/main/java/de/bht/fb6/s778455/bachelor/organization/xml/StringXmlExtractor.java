/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization.xml;

import java.io.StringReader;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;

/**
 * <p>This {@link XmlExtractor} is designed to query a simple {@link String} (not a whole file).</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 *
 */
public class StringXmlExtractor extends XmlExtractor {

	/**
	 * Create a new StringXmlExtract which is designed to query the given string.
	 * @param stringOfInterest
	 * @param namespaces
	 * @throws GeneralLoggingException
	 */
	public StringXmlExtractor(final String stringOfInterest, final Map< String, String > namespaces) throws GeneralLoggingException {
		Processor processor = super._createProcessor( namespaces );
		
		DocumentBuilder builder = processor.newDocumentBuilder();
		try {
			super.contextItem = builder.build( new StreamSource( new StringReader( stringOfInterest ) ) );
		} catch( SaxonApiException e ) {
			throw new GeneralLoggingException( getClass() + "StringXmlExtractor(): expcetion: " + e, "Internal error in the StringXmlExtractor. See the logs for details." );
		}
		
	}
}
