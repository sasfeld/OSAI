/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;

import de.bht.fb6.s778455.bachelor.organization.FileUtil;


/**
 * <p>This class realizes the represenation of a tag extracted by Stanford NLP using Named Entity Recognition (NER).</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 *
 */
public class NerTag extends Tag {
	/**
	 * Classifier label. An example for German HGC corpus: I-LOC, I-ORG, I-MISC
	 */
	protected String classifierLabel;
	
	public NerTag( final String classifierLabel, final double weight, final String value, final String uri ) {
		super( weight, value, uri , TagType.NER_TAG);	
		
		this.classifierLabel = classifierLabel;
	}

	/**
	 * @return the classifierLabel
	 */
	public final String getClassifierLabel() {
		return this.classifierLabel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ( ( this.classifierLabel == null ) ? 0 : this.classifierLabel
						.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( this == obj )
			return true;
		if( !super.equals( obj ) )
			return false;
		if( this.getClass() != obj.getClass() )
			return false;
		final NerTag other = ( NerTag ) obj;
		if( this.classifierLabel == null ) {
			if( other.classifierLabel != null )
				return false;
		} else if( !this.classifierLabel.equals( other.classifierLabel ) )
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append( "NerTag [getClassifierLabel()=" );
		builder.append( this.getClassifierLabel() );
		builder.append( ", toString()=" );
		builder.append( super.toString() );
		builder.append( "]" );
		return builder.toString();
	}

	/*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    @Override
    public URI getRdfUri() throws URISyntaxException {
        if ( null != this.getValue() || 0 != this.getValue().length() ) {
            final URI newUri = new  URI( INDIVIDUAL_BASE_URI + 
                    "tags/ner/" + FileUtil.removeIllegalChars( this.getValue().trim() ) );
            
            return newUri;
        }
        
        throw new IllegalArgumentException( "The value for this.getUri() musn't be null and empty!" );
    }   
}
