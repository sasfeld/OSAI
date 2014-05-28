/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;

import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class describes the a Posting in a {@link BoardThread}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 22.11.2013
 * 
 */
public class Posting extends AUserContribution {

    /**
     * Untagged content.
     */
    protected String content;
    /**
     * Tagged content (by NER).
     */
    protected String taggedContent;
    /**
     * ID of the parent posting. This value can be null.
     */
    protected Long parentPostingId;
    protected BoardThread belongingThread;
    protected String postType;

    /**
     * Create a Posting with a link to the belonging thread {@link Thread}
     * 
     * @param thread
     */
    public Posting( final BoardThread thread ) {
        super( thread.getBelongingCourse() );

        this.belongingThread = thread;
    }

    /**
     * Create a bare posting.
     */
    public Posting() {
        super();

        this.belongingThread = null;
    }

    /**
     * @return the belongingThread
     */
    public BoardThread getBelongingThread() {
        return this.belongingThread;
    }

    /**
     * @return the parentPostingId, can be null
     */
    public Long getParentPostingId() {
        return this.parentPostingId;
    }

    /**
     * @param parentPostingId
     *            the parentPostingId to set, can be null
     * @return
     */
    public Posting setParentPostingId( final Long parentPostingId ) {
        this.parentPostingId = parentPostingId;
        return this;
    }

    /**
     * @return the untagged content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * @param content
     *            the untagged content to set
     * @return
     */
    public Posting setContent( final String content ) {
        this.content = content;
        return this;
    }

    /**
     * @return the taggedContent
     */
    public String getTaggedContent() {
        return this.taggedContent;
    }

    /**
     * @param taggedContent
     *            the taggedContent to set
     * @return
     */
    public Posting setTaggedContent( final String taggedContent ) {
        this.taggedContent = taggedContent;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
     */
    @Override
    public String exportToTxt() {
        final StringBuilder txtExport = new StringBuilder();

        txtExport.append( super.exportToTxt() );

        txtExport.append( "PARENT_POSTING_ID: " + this.getParentPostingId()
                + "\n" );
        final String postingType = this.getPostingType();
        if( null != postingType ) {
            txtExport.append( "POSTING_TYPE: " + postingType + "\n" );
        }
        txtExport.append( "CONTENT:\n" );

        if( null != this.getContent() ) {
            final String[] postingLines = this.getContent().split( "\n" );

            for( final String line : postingLines ) {
                txtExport.append( line + "\n" );
            }
        }

        txtExport.append( "TAGGED_CONTENT:\n" );
        if( null != this.getTaggedContent() ) {
            final String[] taggedPostingLines = this.getTaggedContent().split(
                    "\n" );

            for( final String taggedLine : taggedPostingLines ) {
                txtExport.append( taggedLine );
            }
        }

        return txtExport.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.model.AUserContribution#importFromTxt(java
     * .lang.String, java.lang.String)
     */
    @Override
    public void importFromTxt( final String key, final String value ) {
        super.importFromTxt( key, value );

        if( key.equals( "PARENT_POSTING_ID" ) ) {
            try {
                this.setParentPostingId( Long.parseLong( value ) );
            } catch( final NumberFormatException e ) {
                Application
                        .log( this.getClass()
                                + ":importFromTxt: couldn't parse given value to an integer parentPostingId. Given value: "
                                + value, LogType.ERROR );
            }
        } else if( key.equals( "CONTENT" ) ) {
            this.setContent( value );
        } else if( key.equals( "TAGGED_CONTENT" ) ) {
            this.setTaggedContent( value );
        } else if( key.equals( "POSTING_TYPE" ) ) {
            this.setPostingType( value );
        }
    }

    public Posting setPostingType( final String postType ) {
        this.postType = postType;
        return this;
    }

    public String getPostingType() {
        return this.postType;
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    public URI getRdfUri() throws URISyntaxException {
        final URI uri = new URI( super.prepareRdfUri() + "posting" + "/"
                + this.getId() );

        return uri;
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#hashCode()
     */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((parentPostingId == null) ? 0 : parentPostingId.hashCode());
		result = prime * result
				+ ((postType == null) ? 0 : postType.hashCode());
		result = prime * result
				+ ((taggedContent == null) ? 0 : taggedContent.hashCode());
		return result;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Posting other = (Posting) obj;
		if (belongingThread == null) {
			if (other.belongingThread != null)
				return false;
		} else if (!belongingThread.equals(other.belongingThread))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (parentPostingId == null) {
			if (other.parentPostingId != null)
				return false;
		} else if (!parentPostingId.equals(other.parentPostingId))
			return false;
		if (postType == null) {
			if (other.postType != null)
				return false;
		} else if (!postType.equals(other.postType))
			return false;
		if (taggedContent == null) {
			if (other.taggedContent != null)
				return false;
		} else if (!taggedContent.equals(other.taggedContent))
			return false;
		return true;
	}

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append( "Posting [" );
        builder.append( ", getParentPostingId()=" );
        builder.append( this.getParentPostingId() );
        builder.append( ", getContent()=" );
        builder.append( this.getContent() );
        builder.append( ", getTaggedContent()=" );
        builder.append( this.getTaggedContent() );
        builder.append( ", getPostingType()=" );
        builder.append( this.getPostingType() );
        builder.append( ", toString()=" );
        builder.append( super.toString() );
        builder.append( "]" );
        return builder.toString();
    }

}
