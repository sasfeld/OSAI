/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

/**
 * <p>This class describes Tags recieved by the Topic Zoom WebTags service.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 05.01.2014
 *
 */
public class TopicZoomTag extends Tag {
	protected int degreeGeneralization;
	protected double significance;
	
	/**
	 * Create a new TopicZoomTag
	 * @param significance
	 * @param degreeGeneralization
	 * @param weight
	 * @param value
	 * @param uri
	 */
	public TopicZoomTag(double significance, int degreeGeneralization, double weight, String value, String uri) {
		super( weight, value, uri );
		
		this.significance = significance;
		this.degreeGeneralization = degreeGeneralization;
	}
	/**
	 * @return the degreeGeneralization
	 */
	public int getDegreeGeneralization() {
		return degreeGeneralization;
	}
	/**
	 * @param degreeGeneralization the degreeGeneralization to set
	 */
	public void setDegreeGeneralization( int degreeGeneralization ) {
		this.degreeGeneralization = degreeGeneralization;
	}
	/**
	 * @return the significance
	 */
	public double getSignificance() {
		return significance;
	}
	/**
	 * @param significance the significance to set
	 */
	public void setSignificance( double significance ) {
		this.significance = significance;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + degreeGeneralization;
		long temp;
		temp = Double.doubleToLongBits( significance );
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
		TopicZoomTag other = ( TopicZoomTag ) obj;
		if( degreeGeneralization != other.degreeGeneralization )
			return false;
		if( Double.doubleToLongBits( significance ) != Double
				.doubleToLongBits( other.significance ) )
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "TopicZoomTag [getDegreeGeneralization()=" );
		builder.append( getDegreeGeneralization() );
		builder.append( ", getSignificance()=" );
		builder.append( getSignificance() );
		builder.append( "]" );
		return builder.toString();
	}	
	
	
}
