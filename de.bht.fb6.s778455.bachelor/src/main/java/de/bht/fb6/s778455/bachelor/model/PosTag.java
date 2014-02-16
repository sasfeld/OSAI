/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>
 * A PosTag represents tags won by Part of Speech - Tagging. They can be used to
 * be connected to public ontologies and to identify topics that people were
 * speaking about.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 16.01.2014
 * 
 */
public class PosTag extends Tag {
    
    protected String posTag;

    /**
     * Create a new POSTag won by Part-of-Speech - Tagging.
     * @param weight
     * @param value
     * @param uri
     */
	public PosTag( final String posTag, final double weight, final String value, final String uri) {
		super( weight, value, uri, TagType.POS_TAG);
		
		this.posTag = posTag;
	}

    /**
     * @return the posTag (depending on the Tagset used by Stanford classifier)
     */
    public final String getPosTag() {
        return this.posTag;
    }

    /**
     * @param posTag the posTag to set
     */
    public final void setPosTag( final String posTag ) {
        this.posTag = posTag;
    }
    
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    @Override
    public URI getRdfUri() throws URISyntaxException {
        if ( null != this.getValue() || 0 != this.getValue().length() ) {
            final URI newUri = new  URI( INDIVIDUAL_BASE_URI + 
                    "tags/pos/" + this.getValue().trim() );
            
            return newUri;
        }
        
        throw new IllegalArgumentException( "The value for this.getUri() musn't be null and empty!" );
    }   

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( this.posTag == null ) ? 0 : this.posTag.hashCode() );
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
        final PosTag other = ( PosTag ) obj;
        if( this.posTag == null ) {
            if( other.posTag != null )
                return false;
        } else if( !this.posTag.equals( other.posTag ) )
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append( "PosTag [getPosTag()=" );
        builder.append( this.getPosTag() );
        builder.append( ", toString()=" );
        builder.append( super.toString() );
        builder.append( "]" );
        return builder.toString();
    }

}
