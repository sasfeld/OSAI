/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization.xml;

import java.io.File;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.trans.XPathException;

/**
 * <p>
 * This class defines the API for simple queries on XML files. The queries are
 * based on XPath expressions.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 03.01.2014
 * 
 */
public class XmlExtractor {
	/**
	 * The uri to be read by the saxon compiler.
	 */
	protected String uri;
	protected XPathCompiler xPathCompiler;
	protected XdmNode contextItem;

	/**
	 * Create a new {@link MetadataParser} instance.
	 * 
	 * @param uri
	 *            - the URI of the xml document to be parsed.
	 * @throws GeneralLoggingException
	 *             if the ressource cannot be validated by Saxon.
	 * @throws IllegalArgumentException
	 *             if the uri is null, empty or doesn't refer to an existing
	 *             file.
	 */
	public XmlExtractor( final String uri,
			final Map< String, String > namespaces )
			throws GeneralLoggingException {
		if( uri == null || uri.isEmpty() ) {
			throw new IllegalArgumentException(
					"The value for the parameter uri in the constructor of ModsMetadataParser mustn't be empty." );
		}
		this.uri = uri;

		Processor processor = _createProcessor( namespaces );
		DocumentBuilder builder = processor.newDocumentBuilder();
		try {
			contextItem = builder.build( new File( uri ) );
		} catch( SaxonApiException e ) {
			throw new GeneralLoggingException(
					getClass()
							+ ": error while trying to access file using Saxon: "
							+ uri,
					"Internal error in the XMLExtractory. Please see the logs." );
		}
	}

	/**
	 * Create and return the {@link Processor} and also assign the xPathCompiler field.
	 * @param namespaces
	 * @return
	 */
	protected Processor _createProcessor( final Map< String, String > namespaces ) {
		// define the Saxon processor
		Processor processor = new Processor( false );
		xPathCompiler = processor.newXPathCompiler();
		// declare each namespace
		for( String namespace : namespaces.keySet() ) {
			xPathCompiler.declareNamespace( namespace,
					namespaces.get( namespace ) );
		}
		return processor;
	}
	
	/**
	 * This constructor should be called by subclasses.
	 */
	protected XmlExtractor() {
		// nothing to do: just for subclasses to avoid compiler errors
	}

	/**
	 * Compile and execute and XPath query.
	 * 
	 * @param query
	 *            - the {@link XPath} expression.
	 * @param moreNodes
	 *            - set true, if you want to fetch more nodes in a {@link List}
	 *            of {@link String}.
	 * @return an {@link Object} (String if moreNodes is false, String[] if
	 *         moreNodes is set true)
	 */
	public Object buildXPath( final String query, final boolean moreNodes ) {
		try {
			XPathExecutable x = xPathCompiler.compile( query );

			XPathSelector selector = x.load();
			selector.setContextItem( this.contextItem );
			XdmValue value = selector.evaluate();
			if( moreNodes ) {
				String[] list = new String[value.size()];
				int i = 0;
				for( XdmItem xdmItem : value ) {
					list[i] = xdmItem.toString();
					++i;
				}
				// Replace attribute chars
				return XmlParserHelper.removeAttributeChars( list );
			}

			Sequence rep = value.getUnderlyingValue();
		
			// Replace attribute chars
			return XmlParserHelper.removeAttributeChars( rep.head().getStringValue());
		} catch( SaxonApiException | XPathException  e ) {
			new GeneralLoggingException( getClass() + "buildXPath: Error while quering. given query: " + query + "; given file: " + this.uri, "Internal error in the XmlExtractory" );
			return null;
		}
	}
}
