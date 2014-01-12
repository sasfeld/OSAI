/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;


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
	
	public NerTag( String classifierLabel, double weight, String value, String uri ) {
		super( weight, value, uri , TagType.NER_TAGS);	
		
		this.classifierLabel = classifierLabel;
	}

	/**
	 * @return the classifierLabel
	 */
	public final String getClassifierLabel() {
		return classifierLabel;
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
				+ ( ( classifierLabel == null ) ? 0 : classifierLabel
						.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj ) {
		if( this == obj )
			return true;
		if( !super.equals( obj ) )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		NerTag other = ( NerTag ) obj;
		if( classifierLabel == null ) {
			if( other.classifierLabel != null )
				return false;
		} else if( !classifierLabel.equals( other.classifierLabel ) )
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "NerTag [getClassifierLabel()=" );
		builder.append( getClassifierLabel() );
		builder.append( ", toString()=" );
		builder.append( super.toString() );
		builder.append( "]" );
		return builder.toString();
	}

	
}
