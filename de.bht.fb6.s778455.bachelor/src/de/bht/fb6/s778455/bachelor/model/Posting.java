/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.model;

/**
 * 
 * <p>This class describes the a Posting in a {@link BoardThread}.</p>
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
	protected int parentPostingId;
	protected BoardThread belongingThread;

	/**
	 * Create a Posting with a link to the belonging thread {@link Thread}
	 * @param thread
	 */
	public Posting( BoardThread thread ) {
		this.belongingThread = thread;
	}

	/**
	 * Create a bare posting.
	 */
	public Posting() {
		this.belongingThread = null;
	}
	
	/**
	 * @return the belongingThread
	 */
	public BoardThread getBelongingThread() {
		return belongingThread;
	}

	/**
	 * @return the parentPostingId
	 */
	public int getParentPostingId() {
		return parentPostingId;
	}

	/**
	 * @param parentPostingId the parentPostingId to set
	 */
	public void setParentPostingId( int parentPostingId ) {
		this.parentPostingId = parentPostingId;
	}

	/**
	 * @return the untagged content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the untagged content to set
	 */
	public void setContent( String content ) {
		this.content = content;
	}

	/**
	 * @return the taggedContent
	 */
	public String getTaggedContent() {
		return taggedContent;
	}

	/**
	 * @param taggedContent the taggedContent to set
	 */
	public void setTaggedContent( String taggedContent ) {
		this.taggedContent = taggedContent;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
	 */
	public String exportToTxt() {
		StringBuilder txtExport = new StringBuilder();
		
		txtExport.append( super.exportToTxt() );
		txtExport.append( "PARENT_POSTING_ID: " + this.getParentPostingId() + "\n");
		txtExport.append( "CONTENT:\n");
		
		String[] postingLines = this.getContent().split( "\n" );

		for( String line : postingLines ) {
			txtExport.append( line + "\n" );
		}
		

		txtExport.append( "TAGGED_CONTENT:\n");
		String[] taggedPostingLines = this.getTaggedContent().split(
				"\n" );

		for( String taggedLine : taggedPostingLines ) {
			txtExport.append( taggedLine );
		}

		
		return txtExport.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ( ( content == null ) ? 0 : content.hashCode() );
		result = prime * result
				+ ( ( taggedContent == null ) ? 0 : taggedContent.hashCode() );
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
		Posting other = ( Posting ) obj;
		if( content == null ) {
			if( other.content != null )
				return false;
		} else if( !content.equals( other.content ) )
			return false;
		if( taggedContent == null ) {
			if( other.taggedContent != null )
				return false;
		} else if( !taggedContent.equals( other.taggedContent ) )
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Posting [content=" + content + ", taggedContent="
				+ taggedContent + ", creationDate=" + creationDate
				+ ", creator=" + creator + ", title=" + title + "]";
	}
	
	
}
