/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.tools.DateComparator;

/**
 * 
 * <p>
 * This class defines a model for a Board. A Board is included in a
 * {@link Course}.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class Board extends AUserContribution {
    protected List< BoardThread > boardThreads;
    protected String type;
    protected String intro;

    /**
     * Create a new Board. A Board is included in the given course.
     * 
     * @param course
     */
    public Board( final Course course ) {
        super( course );

        if( null == course ) {
            throw new IllegalArgumentException( this.getClass()
                    + ": null argument in constructor!" );
        }
        this.course = course;

        this.boardThreads = new ArrayList< BoardThread >();

    }

    /**
     * Create a new Board isntance using a given course and a specific title.
     * 
     * @param course
     * @param boardTitle
     */
    public Board( final Course course, final String boardTitle ) {
        super( course );

        if( null == course || null == boardTitle ) {
            throw new IllegalArgumentException( this.getClass()
                    + ": null argument in constructor!" );
        }

        this.course = course;
        this.title = boardTitle;

        this.boardThreads = new ArrayList< BoardThread >();
    }

    /**
     * @return the intro text
     */
    public String getIntro() {
        return this.intro;
    }

    /**
     * @param intro
     *            the intro text to set
     */
    public void setIntro( final String intro ) {
        this.intro = intro;
    }

    /**
     * Add a {@link BoardThread}. Sort it immediatly.
     * 
     * @param boardThread
     */
    public void addThread( final BoardThread boardThread ) {
        final Comparator< AUserContribution > dateComparator = new DateComparator();

        this.boardThreads.add( boardThread );
        // sort ascending
        Collections.sort( this.boardThreads, dateComparator );
    }

    /**
     * Get a sorted list of {@link BoardThread}. {@link BoardThread} are sorted
     * by the creation date.
     * 
     * @return
     */
    public List< BoardThread > getBoardThreads() {
        return this.boardThreads;
    }

    /**
     * Set the board's type (moodle specific key: e.g. "news" for a news board
     * or "general")
     * 
     * @param type
     */
    public void setType( final String type ) {
        this.type = type;
    }

    /**
     * Get the board's type (moodle specific key: e.g. "news" for a news board
     * or "general")
     * 
     * @return
     */
    public String getType() {
        return this.type;
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
        txtExport.append( "INTRO: " + this.getIntro() + "\n" );
        txtExport.append( "TYPE: " + this.getType() + "\n" );

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

        if( key.equals( "INTRO" ) ) {
            this.setIntro( value );
        } else if( key.equals( "TYPE" ) ) {
            this.setType( value );
        }
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    public URI getRdfUri() throws URISyntaxException {
        final URI uri = new URI( super.prepareRdfUri() + "board" + "/"
                + this.getId() );

        return uri;
    }
  

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append( "Board [getIntro()=" );
        builder.append( this.getIntro() );
        builder.append( ", getBoardThreads()=" );
        builder.append( this.getBoardThreads() );
        builder.append( ", getType()=" );
        builder.append( this.getType() );
        builder.append( ", exportToTxt()=" );
        builder.append( this.exportToTxt() );
        builder.append( ", getId()=" );
        builder.append( this.getId() );
        builder.append( ", getModificationDate()=" );
        builder.append( this.getModificationDate() );
        builder.append( ", getCreationDate()=" );
        builder.append( this.getCreationDate() );
        builder.append( ", getTitle()=" );
        builder.append( this.getTitle() );
        builder.append( "]" );
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ( ( this.boardThreads == null ) ? 0 : this.boardThreads
                        .hashCode() );
        result = prime * result
                + ( ( this.intro == null ) ? 0 : this.intro.hashCode() );
        result = prime * result
                + ( ( this.type == null ) ? 0 : this.type.hashCode() );
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        final Board other = ( Board ) obj;
        if( this.boardThreads == null ) {
            if( other.boardThreads != null )
                return false;
        } else if( !this.boardThreads.equals( other.boardThreads ) )
            return false;
        if( this.course == null ) {
            if( other.course != null )
                return false;
        }
        if( this.intro == null ) {
            if( other.intro != null )
                return false;
        } else if( !this.intro.equals( other.intro ) )
            return false;
        if( this.type == null ) {
            if( other.type != null )
                return false;
        } else if( !this.type.equals( other.type ) )
            return false;
        return true;
    }

}
