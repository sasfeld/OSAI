/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

/**
 * <p>This class represents a (auto-generated) Tag which one posting can have.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class Tag {
	public enum TagType {
		/**
		 * key for tags extracted by the TopicZoom WebTag service.
		 */
		TOPIC_ZOOM, 
		/**
		 * key for tags won by Stanford Named Entity Recognition (NER)
		 */
		NER_TAGS,
	}
	
	protected double weight;
	protected String value;
	protected String uri;
	
	/**
	 * Create a new Tag.
	 * @param weight
	 * @param value
	 * @param uri
	 * @throws IllegalArgumentException if you try to commit null values
	 */
	public Tag(double weight, String value, String uri) {
		if ( null == value || null == uri ) {
			throw new IllegalArgumentException( "Illegal null value for 'value' or 'uri'!" );
		}
		
		this.weight = weight;
		this.value = value;
		this.uri = uri;
	}
	
	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight( double weight ) {
		this.weight = weight;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue( String value ) {
		this.value = value;
	}
	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri the uri to set
	 */
	public void setUri( String uri ) {
		this.uri = uri;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( uri == null ) ? 0 : uri.hashCode() );
		result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
		long temp;
		temp = Double.doubleToLongBits( weight );
		result = prime * result + ( int ) ( temp ^ ( temp >>> 32 ) );
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		Tag other = ( Tag ) obj;
		if( uri == null ) {
			if( other.uri != null )
				return false;
		} else if( !uri.equals( other.uri ) )
			return false;
		if( value == null ) {
			if( other.value != null )
				return false;
		} else if( !value.equals( other.value ) )
			return false;
		if( Double.doubleToLongBits( weight ) != Double
				.doubleToLongBits( other.weight ) )
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "Tag [getWeight()=" );
		builder.append( getWeight() );
		builder.append( ", getValue()=" );
		builder.append( getValue() );
		builder.append( ", getUri()=" );
		builder.append( getUri() );
		builder.append( "]" );
		return builder.toString();
	}	
	
}
