/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;

import de.bht.fb6.s778455.bachelor.semantic.store.vocabulary.IBaseUris;

/**
 * <p>This class represents a (auto-generated) Tag which one posting can have.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class Tag implements IRdfUsable, IBaseUris {
	public enum TagType {
		/**
		 * key for tags extracted by the TopicZoom WebTag service.
		 */
		TOPIC_ZOOM, 
		/**
		 * key for tags won by Stanford Named Entity Recognition (NER)
		 */
		NER_TAG, 
		/**
		 * key for tags won by Stanford Part-of-Speech Tagging (POS)
		 */
		POS_TAG,
	}
	
	protected double weight;
	protected String value;
	protected String uri;
	protected TagType tagType;

	/**
	 * Create a new Tag.
	 * @param weight
	 * @param value
	 * @param uri
	 * @throws IllegalArgumentException if you try to commit null values
	 */
	public Tag(final double weight, final String value, final String uri, final TagType tagType) {
		if ( null == value || null == uri ) {
			throw new IllegalArgumentException( "Illegal null value for 'value' or 'uri'!" );
		}
		
		this.weight = weight;
		this.value = value;
		this.uri = uri;
		this.tagType = tagType;
	}
	
	/**
	 * @return the tagType
	 */
	public final TagType getTagType() {
		return this.tagType;
	}

	/**
	 * @param tagType the tagType to set
	 */
	public final void setTagType( final TagType tagType ) {
		this.tagType = tagType;
	}
	
	/**
	 * @return the weight
	 */
	public double getWeight() {
		return this.weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight( final double weight ) {
		this.weight = weight;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue( final String value ) {
		this.value = value;
	}
	/**
	 * @return the uri
	 */
	public String getUri() {
		return this.uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri( final String uri ) {
		this.uri = uri;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( this.uri == null ) ? 0 : this.uri.hashCode() );
		result = prime * result + ( ( this.value == null ) ? 0 : this.value.hashCode() );
		long temp;
		temp = Double.doubleToLongBits( this.weight );
		result = prime * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( final Object obj ) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( this.getClass() != obj.getClass() )
			return false;
		final Tag other = ( Tag ) obj;
		if( this.uri == null ) {
			if( other.uri != null )
				return false;
		} else if( !this.uri.equals( other.uri ) )
			return false;
		if( this.value == null ) {
			if( other.value != null )
				return false;
		} else if( !this.value.equals( other.value ) )
			return false;
		if( Double.doubleToLongBits( this.weight ) != Double
				.doubleToLongBits( other.weight ) )
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append( "Tag [getWeight()=" );
		builder.append( this.getWeight() );
		builder.append( ", getValue()=" );
		builder.append( this.getValue() );
		builder.append( ", getUri()=" );
		builder.append( this.getUri() );
		builder.append( "]" );
		return builder.toString();
	}

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    public URI getRdfUri() throws URISyntaxException {
        if ( null != this.getUri() || 0 != this.getUri().length() ) {
            final URI newUri = new  URI( INDIVIDUAL_BASE_URI + 
                    "tags/" + this.getUri() );
            
            return newUri;
        }
        
        throw new IllegalArgumentException( "The value for this.getUri() musn't be null and empty!" );
    }	
	
}
