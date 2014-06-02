/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */

package de.bht.fb6.s778455.bachelor.model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class defines a model for an (e learning) course, e.g. 'Moodle'.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public class Course implements Serializable, IDirectoryPortable, IRdfUsable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -241430860036615503L;

    /**
     * 
     * <p>
     * The literals in this enumeration specifiy the types of learned words.<br />
     * A type might be the name of a person for example.
     * </p>
     * 
     * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
     * @since 05.12.2013
     * 
     */
    public enum LearnedWordTypes {
        /**
         * A single person name.
         */
        PERSON_NAME,
    };

    protected String title;
    protected int id;
    protected String shortName;
    protected String summary;
    protected String lang;
    protected Date creationDate;
    protected Date modificationDate;
    protected List< Board > boards;
    protected PersonNameCorpus personNameCorpus;
    protected Map< LearnedWordTypes, Set< String > > learnedWords;
    protected URL webUrl;
    protected Map< TagType, List< Tag > > tagMap;
    protected Language language;
    protected LmsCourseSet lmsCourseSet;

    /**
     * Create a course for which only a title is given.
     * 
     * @param courseTitle
     * @param lmsCourseSet
     *            the course set to which this instance belongs
     */
    public Course( final String courseTitle, final LmsCourseSet lmsCourseSet ) {
        if( null == courseTitle || null == lmsCourseSet ) {
            throw new IllegalArgumentException(
                    "Null is not allowed as parameter value!" );
        }

        this.setTitle( courseTitle );
        this.boards = new ArrayList< Board >();
        this.learnedWords = new HashMap< LearnedWordTypes, Set< String > >();
        this.lmsCourseSet = lmsCourseSet;

        this._initialize();
    }

    private void _initialize() {
        this.tagMap = new HashMap< TagType, List< Tag > >();
        this.language = Language.UNKNOWN;
    }

    /**
     * @return the language
     */
    public final Language getLanguage() {
        return this.language;
    }

    /**
     * @param language
     *            the language to set
     */
    public final void setLanguage( final Language language ) {
        this.language = language;
    }

    /**
     * @return the personNameCorpus
     */
    public PersonNameCorpus getPersonNameCorpus() {
        return this.personNameCorpus;
    }

    /**
     * Set a completely new {@link PersonNameCorpus} instance.
     * 
     * @param personNameCorpus
     *            the personNameCorpus to set
     * @return
     */
    public Course setPersonNameCorpus( final PersonNameCorpus personNameCorpus ) {
        if( null == personNameCorpus ) {
            throw new IllegalArgumentException(
                    this.getClass()
                            + "setPersonNameCorpus(): null value is not allowed for personnameCorpus" );
        }
        this.personNameCorpus = personNameCorpus;
        return this;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return this.lang;
    }

    /**
     * @param lang
     *            the lang to set
     * @return
     */
    public Course setLang( final String lang ) {
        this.lang = lang;
        return this;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title
     *            the title to set
     * @return
     */
    public Course setTitle( final String title ) {
        this.title = title;
        return this;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * @param creationDate
     *            the creationDate to set
     * @return
     */
    public Course setCreationDate( final Date creationDate ) {
        this.creationDate = creationDate;
        return this;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return this.modificationDate;
    }

    /**
     * @param modificationDate
     *            the modificationDate to set
     * @return
     */
    public Course setModificationDate( final Date modificationDate ) {
        this.modificationDate = modificationDate;
        return this;
    }

    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     * @return
     */
    public Course setId( final int id ) {
        this.id = id;
        return this;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * @param shortName
     *            the shortName to set
     * @return
     */
    public Course setShortName( final String shortName ) {
        this.shortName = shortName;
        return this;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return this.summary;
    }

    /**
     * @param summary
     *            the summary to set
     * @return
     */
    public Course setSummary( final String summary ) {
        this.summary = summary;
        return this;
    }

    /**
     * Add another board to this course.
     * 
     * @param board
     *            mustn't be null.
     */
    public void addBoard( final Board board ) {
        if( null == board ) {
            throw new IllegalArgumentException( this.getClass()
                    + "addBoard(): null given for board parameter." );
        }
        this.boards.add( board );
    }

    /**
     * @return the board
     */
    public List< Board > getBoards() {
        return this.boards;
    }

    /**
     * Get a single board with the given name.
     * 
     * @param boardName
     *            consider the method works case-insensitiv
     * @return the matched {@link Board} or null if it wasn't found
     */
    public Board getBoard( final String boardName ) {
        if( null == boardName ) {
            throw new IllegalArgumentException( this.getClass()
                    + "getBoard(): null given for boardName parameter." );
        }

        for( final Board board : this.boards ) {
            if( board.getTitle().trim().toLowerCase()
                    .equals( boardName.trim().toLowerCase() ) ) {
                return board;
            }
        }

        return null; // not found
    }

    /**
     * <p>
     * Add a "learned word" defined by the given type.<br />
     * Example: a learned word can be a person's name.
     * </p>
     * 
     * @param singleWord
     * @param wordType
     *            the given type (defined in {@link LearnedWordTypes}).
     */
    public void addLearnedWord( final String singleWord,
            final LearnedWordTypes wordType ) {
        // create word set first if this is the first word for the given type
        if( !this.learnedWords.containsKey( wordType ) ) {
            final Set< String > wordSet = new HashSet< String >();
            this.learnedWords.put( wordType, wordSet );
        }

        // add the word to the existing set
        this.learnedWords.get( wordType ).add( singleWord );
    }

    /**
     * Get the "learned words" for the given {@link LearnedWordTypes}.
     * 
     * @param wordType
     * @return a Set of {@link String} or null if there are no words available.
     */
    public Set< String > getLearnedWords( final LearnedWordTypes wordType ) {
        return this.learnedWords.get( wordType );
    }

    /**
     * Set the whole {@link Tag} list for the given {@link TagType}.
     * 
     * @param fetchedTags
     * @param tagType
     */
    public void setTags( final List< Tag > fetchedTags, final TagType tagType ) {
        this.tagMap.put( tagType, fetchedTags );
    }

    /**
     * Add a single tag.
     * 
     * @param newTag
     * @param tagType
     */
    public void addTag( final Tag newTag, final TagType tagType ) {
        // create list if neccessary
        if( null == this.getTags( tagType ) ) {
            this.tagMap.put( tagType, new ArrayList< Tag >() );
        }

        // add to map
        this.getTags( tagType ).add( newTag );
    }

    /**
     * Get the whole {@link Tag} list for the given {@link TagType}.
     * 
     * @param tagType
     * @return
     * @return might return null.
     */
    public List< Tag > getTags( final TagType tagType ) {
        return this.tagMap.get( tagType );
    }

    /**
     * Return a boolean whether this Posting is tagged by TopicZoom Web Tagging.<br />
     * The condition for a posting to be tagged is the existence of at least one
     * tag.
     * 
     * @return
     */
    public boolean isTopicZoomTagged() {
        if( null == this.getTags( TagType.TOPIC_ZOOM ) ) {
            return false;
        }

        return this.getTags( TagType.TOPIC_ZOOM ).size() > 0 ? true : false;
    }

    /**
     * Return a boolean whether this Posting is tagged.<br />
     * The condition for a posting to be tagged is the existence of at least one
     * tag.
     * 
     * @return
     */
    public boolean isTagged() {
        if( this.isTopicZoomTagged() ) {
            return true;
        }
        if( this.isNerTagged() ) {
            return true;
        }
        if( this.isPosTagged() ) {
            return true;
        }

        return false;
    }

    /**
     * Get the number of all tags which enrich this {@link Posting}.
     * 
     * @return
     */
    public int getNumberTags() {
        int numberTags = 0;

        for( final List< Tag > tags : this.tagMap.values() ) {
            numberTags += tags.size();
        }
        return numberTags;
    }

    /**
     * Return true if this posting is tagged by Named Entity Recognition (NER)
     * tags
     * 
     * @return
     */
    public boolean isNerTagged() {
        if( null == this.getTags( TagType.NER_TAG ) ) {
            return false;
        }

        return this.getTags( TagType.NER_TAG ).size() > 0 ? true : false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.model.AUserContribution#exportToTxt()
     */
    @Override
    public String exportToTxt() {
        final StringBuilder txtExport = new StringBuilder();

        txtExport.append( "ID: " + this.getId() + "\n" );
        if( null != this.getCreationDate() ) {
            txtExport.append( "CREATION_DATETIME: "
                    + this.getCreationDate().getTime() + "\n" );
        }
        if( null != this.getModificationDate() ) {
            txtExport.append( "MODIFICATION_DATETIME: "
                    + this.getModificationDate().getTime() + "\n" );
        }
        txtExport.append( "LANG: " + this.getLang() + "\n" );
        txtExport.append( "SHORT_NAME: " + this.getShortName() + "\n" );
        txtExport.append( "TITLE: " + this.getTitle() + "\n" );
        txtExport.append( "LANGUAGE: " + this.getLanguage() + "\n" );
        txtExport.append( "SUMMARY: " + this.getSummary() + "\n" );
        

        if( null != this.getWebUrl() ) {
            final String url = this.getWebUrl().toExternalForm();
            txtExport.append( "URL: " + url + "\n" );
        }

        // tags
        final List< Tag > topicZoomTags = this.getTags( TagType.TOPIC_ZOOM );

        if( null != topicZoomTags ) {
            for( final Tag tag : topicZoomTags ) {
                if( tag instanceof TopicZoomTag ) {
                    final TopicZoomTag topicZoomTag = ( TopicZoomTag ) tag;
                    txtExport.append( "TOPIC_ZOOM_TAG: " + "value:"
                            + topicZoomTag.getValue() + ";" + "weight:"
                            + topicZoomTag.getWeight() + ";" + "significance:"
                            + topicZoomTag.getSignificance() + ";"
                            + "degreegeneralization:"
                            + topicZoomTag.getDegreeGeneralization() + ";"
                            + "uri:" + topicZoomTag.getRelatedConceptUri() + "\n" );
                } else {
                    Application
                            .log( this.getClass()
                                    + ":exportToTxt(): the saved tag is not of type TopicZoomTag. There must be some error in the extraction process. Only add TopicZoomTags when using TopicZoom.",
                                    LogType.ERROR );
                }
            }
        }

        final List< Tag > nerTags = this.getTags( TagType.NER_TAG );
        if( null != nerTags ) {
            for( final Tag tag : nerTags ) {
                if( tag instanceof NerTag ) {
                    final NerTag nerTag = ( NerTag ) tag;
                    txtExport.append( "NER_TAG: " + "value:"
                            + nerTag.getValue() + ";" + "weight:"
                            + nerTag.getWeight() + ";" + "uri:"
                            + nerTag.getRelatedConceptUri() + ";" + "classifierlabel:"
                            + nerTag.getClassifierLabel() + "\n" );
                } else {
                    Application
                            .log( this.getClass()
                                    + ":exportToTxt(): the saved tag is not of type NerTag. There must be some error in the extraction process. Only add TopicZoomTags when using TopicZoom.",
                                    LogType.ERROR );
                }
            }
        }

        final List< Tag > posTags = this.getTags( TagType.POS_TAG );
        if( null != posTags ) {
            for( final Tag tag : posTags ) {
                if( tag instanceof PosTag ) {
                    final PosTag posTag = ( PosTag ) tag;
                    txtExport.append( "POS_TAG: " + "value:"
                            + posTag.getValue() + ";" + "weight:"
                            + posTag.getWeight() + ";" + "uri:"
                            + posTag.getRelatedConceptUri() + ";" + "postag:"
                            + posTag.getPosTag() + "\n" );
                } else {
                    Application
                            .log( this.getClass()
                                    + ":exportToTxt(): the saved tag is not of type PosTag. There must be some error in the extraction process. Only add TopicZoomTags when using TopicZoom.",
                                    LogType.ERROR );
                }
            }
        }

        return txtExport.toString();
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.model.IDirectoryPortable#importFromTxt(java
     * .lang.String, java.lang.String)
     */
    public void importFromTxt( final String key, final String value ) {
        if( null == key || 0 == key.length() || null == value
                || 0 == value.length() ) {
            throw new IllegalArgumentException(
                    "Illegal value for key or value! Key: " + key + "; value: "
                            + value );
        }

        if( key.equals( "ID" ) ) {
            try {
                this.setId( Integer.parseInt( value ) );
            } catch( final NumberFormatException e ) {
                Application
                        .log( this.getClass()
                                + ":importFromTxt: couldn't parse given value to an integer ID. Given value: "
                                + value, LogType.ERROR );
            }
        } else if( key.equals( "CREATION_DATETIME" ) ) {
            try {
                this.setCreationDate( new Date( Long.parseLong( value ) ) );
            } catch( final NumberFormatException e ) {
                Application
                        .log( this.getClass()
                                + ":importFromTxt: couldn't parse given creation date to an Date . Given value: "
                                + value, LogType.ERROR );
            }
        } else if( key.equals( "MODIFICATION_DATETIME" ) ) {
            try {
                this.setModificationDate( new Date( Long.parseLong( value ) ) );
            } catch( final NumberFormatException e ) {
                Application
                        .log( this.getClass()
                                + ":importFromTxt: couldn't parse given modification date to an Date . Given value: "
                                + value, LogType.ERROR );
            }
        } else if( key.equals( "TITLE" ) ) {
            this.setTitle( value );
        } else if( key.equals( "LANG" ) ) {
            this.setLang( value );
        } else if( key.equals( "SHORT_NAME" ) ) {
            this.setShortName( value );
        } else if( key.equals( "SUMMARY" ) ) {
            this.setSummary( value );
        } else if( key.equals( "URL" ) ) {
            try {
                this.setWebUrl( new URL( value ) );
            } catch ( final MalformedURLException e ) {
                Application.log( "Invalid URL permitted: "+value, LogType.ERROR, this.getClass() );
            }
        } else if( key.equals( "TOPIC_ZOOM_TAG" ) ) {
            this.parseTopicZoomValue( value );
        } else if( key.equals( "NER_TAG" ) ) {
            this.parseNerTagValue( value );
        } else if( key.equals( "POS_TAG" ) ) {
            this.parsePosTagValue( value );
        } else if( key.equals( "LANGUAGE" ) ) {
            this.setLanguage( Language.getFromString( value ) );
        }
    }

    private void parsePosTagValue( final String value ) {
        /*
         * structure:
         * value:xyz;weight:1.0;significance:3.0;degreegeneralization:
         * 6;uri:testuri
         */

        final String[] splitValues = value.split( ";" );

        String name = "";
        double weight = 0;
        String uri = "";
        String posTag = "";

        for( final String splitValue : splitValues ) {
            final Pattern pKeyValue = Pattern.compile( "([a-z]+):(.*)" );
            final Matcher mKeyValue = pKeyValue.matcher( splitValue );

            while( mKeyValue.find() ) {
                final String key = mKeyValue.group( 1 );
                final String kValue = mKeyValue.group( 2 );

                switch( key ) {
                case "value":
                    name = kValue;
                    break;
                case "weight":
                    weight = Double.parseDouble( kValue );
                    break;
                case "uri":
                    uri = kValue;
                    break;
                case "postag":
                    posTag = kValue;
                    break;
                default:
                    Application.log( this.getClass()
                            + ":parsePosTagValue(): key " + key
                            + " couldn't be matched.", LogType.ERROR );
                    ;
                }

            }
        }

        final Tag newTag = new PosTag( posTag, weight, name, uri );
        this.addTag( newTag, TagType.POS_TAG );
    }

    /**
     * Parse the value for the key "NER_TAG" within a txt file and fill the
     * Posting's tags.
     * 
     * @param value
     */
    protected void parseNerTagValue( final String value ) {
        /*
         * structure:
         * value:xyz;weight:1.0;significance:3.0;degreegeneralization:
         * 6;uri:testuri
         */

        final String[] splitValues = value.split( ";" );

        String name = "";
        double weight = 0;
        String uri = "";
        String classifierlabel = "";

        for( final String splitValue : splitValues ) {
            final Pattern pKeyValue = Pattern.compile( "([a-z]+):(.*)" );
            final Matcher mKeyValue = pKeyValue.matcher( splitValue );

            while( mKeyValue.find() ) {
                final String key = mKeyValue.group( 1 );
                final String kValue = mKeyValue.group( 2 );

                switch( key ) {
                case "value":
                    name = kValue;
                    break;
                case "weight":
                    weight = Double.parseDouble( kValue );
                    break;
                case "uri":
                    uri = kValue;
                    break;
                case "classifierlabel":
                    classifierlabel = kValue;
                    break;
                default:
                    Application.log( this.getClass()
                            + ":parseNerTagValue(): key " + key
                            + " couldn't be matched.", LogType.ERROR );
                    ;
                }

            }
        }

        final Tag newTag = new NerTag( classifierlabel, weight, name, uri );
        this.addTag( newTag, TagType.NER_TAG );
    }

    /**
     * Parse the value for the key "TOPIC_ZOOM_TAG" within a txt file and fill
     * the Posting's tags.
     * 
     * @param value
     */
    protected void parseTopicZoomValue( final String value ) {
        /*
         * structure:
         * value:xyz;weight:1.0;significance:3.0;degreegeneralization:
         * 6;uri:testuri
         */

        final String[] splitValues = value.split( ";" );

        String name = "";
        double weight = 0;
        double significance = 0;
        int degreegeneralization = 0;
        String uri = "";

        for( final String splitValue : splitValues ) {
            final Pattern pKeyValue = Pattern.compile( "([a-z]+):(.*)" );
            final Matcher mKeyValue = pKeyValue.matcher( splitValue );

            while( mKeyValue.find() ) {
                final String key = mKeyValue.group( 1 );
                final String kValue = mKeyValue.group( 2 );

                switch( key ) {
                case "value":
                    name = kValue;
                    break;
                case "weight":
                    weight = Double.parseDouble( kValue );
                    break;
                case "significance":
                    significance = Double.parseDouble( kValue );
                    break;
                case "degreegeneralization":
                    degreegeneralization = Integer.parseInt( kValue );
                    break;
                case "uri":
                    uri = kValue;
                    break;
                default:
                    Application.log( this.getClass()
                            + ":parseTopicZoomValue(): key " + key
                            + " couldn't be matched.", LogType.ERROR );
                    ;
                }

            }
        }

        final Tag newTag = new TopicZoomTag( significance,
                degreegeneralization, weight, name, uri );
        this.addTag( newTag, TagType.TOPIC_ZOOM );
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ( ( this.boards == null ) ? 0 : this.boards.hashCode() );
        result = prime
                * result
                + ( ( this.creationDate == null ) ? 0 : this.creationDate
                        .hashCode() );
        result = prime * result + this.id;
        result = prime * result
                + ( ( this.lang == null ) ? 0 : this.lang.hashCode() );
        result = prime * result
                + ( ( this.language == null ) ? 0 : this.language.hashCode() );
        result = prime
                * result
                + ( ( this.learnedWords == null ) ? 0 : this.learnedWords
                        .hashCode() );
        result = prime
                * result
                + ( ( this.modificationDate == null ) ? 0
                        : this.modificationDate.hashCode() );
        result = prime
                * result
                + ( ( this.personNameCorpus == null ) ? 0
                        : this.personNameCorpus.hashCode() );
        result = prime * result
                + ( ( this.shortName == null ) ? 0 : this.shortName.hashCode() );
        result = prime * result
                + ( ( this.summary == null ) ? 0 : this.summary.hashCode() );
        result = prime * result
                + ( ( this.tagMap == null ) ? 0 : this.tagMap.hashCode() );
        result = prime * result
                + ( ( this.title == null ) ? 0 : this.title.hashCode() );
        result = prime * result
                + ( ( this.webUrl == null ) ? 0 : this.webUrl.hashCode() );
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
        if( obj == null )
            return false;
        if( this.getClass() != obj.getClass() )
            return false;
        final Course other = ( Course ) obj;
        if( this.boards == null ) {
            if( other.boards != null )
                return false;
        } else if( !this.boards.equals( other.boards ) )
            return false;
        if( this.creationDate == null ) {
            if( other.creationDate != null )
                return false;
        } else if( !this.creationDate.equals( other.creationDate ) )
            return false;
        if( this.id != other.id )
            return false;
        if( this.lang == null ) {
            if( other.lang != null )
                return false;
        } else if( !this.lang.equals( other.lang ) )
            return false;
        if( this.language != other.language )
            return false;
        if( this.learnedWords == null ) {
            if( other.learnedWords != null )
                return false;
        } else if( !this.learnedWords.equals( other.learnedWords ) )
            return false;
        if( this.modificationDate == null ) {
            if( other.modificationDate != null )
                return false;
        } else if( !this.modificationDate.equals( other.modificationDate ) )
            return false;
        if( this.personNameCorpus == null ) {
            if( other.personNameCorpus != null )
                return false;
        } else if( !this.personNameCorpus.equals( other.personNameCorpus ) )
            return false;
        if( this.shortName == null ) {
            if( other.shortName != null )
                return false;
        } else if( !this.shortName.equals( other.shortName ) )
            return false;
        if( this.summary == null ) {
            if( other.summary != null )
                return false;
        } else if( !this.summary.equals( other.summary ) )
            return false;
        if( this.tagMap == null ) {
            if( other.tagMap != null )
                return false;
        } else if( !this.tagMap.equals( other.tagMap ) )
            return false;
        if( this.title == null ) {
            if( other.title != null )
                return false;
        } else if( !this.title.equals( other.title ) )
            return false;
        if( this.webUrl == null ) {
            if( other.webUrl != null )
                return false;
        } else if( !this.webUrl.equals( other.webUrl ) )
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
        builder.append( "Course [getLanguage()=" );
        builder.append( this.getLanguage() );
        builder.append( ", getPersonNameCorpus()=" );
        builder.append( this.getPersonNameCorpus() );
        builder.append( ", getLang()=" );
        builder.append( this.getLang() );
        builder.append( ", getTitle()=" );
        builder.append( this.getTitle() );
        builder.append( ", getCreationDate()=" );
        builder.append( this.getCreationDate() );
        builder.append( ", getModificationDate()=" );
        builder.append( this.getModificationDate() );
        builder.append( ", getId()=" );
        builder.append( this.getId() );
        builder.append( ", getShortName()=" );
        builder.append( this.getShortName() );
        builder.append( ", getSummary()=" );
        builder.append( this.getSummary() );
        builder.append( ", getBoards()=" );
        builder.append( this.getBoards() );
        builder.append( ", isTopicZoomTagged()=" );
        builder.append( this.isTopicZoomTagged() );
        builder.append( ", isTagged()=" );
        builder.append( this.isTagged() );
        builder.append( ", getNumberTags()=" );
        builder.append( this.getNumberTags() );
        builder.append( ", isNerTagged()=" );
        builder.append( this.isNerTagged() );
        builder.append( ", getUrl()=" );
        builder.append( this.getWebUrl() );
        builder.append( "]" );
        return builder.toString();
    }

    /**
     * Set the url String of this course.
     * 
     * @param url
     * @return
     */
    public Course setWebUrl( final URL url ) {
        this.webUrl = url;
        return this;
    }

    public URL getWebUrl() {
        return this.webUrl;
    }

    /**
     * @return the lmsCourseSet
     */
    public final LmsCourseSet getLmsCourseSet() {
        return this.lmsCourseSet;
    }

    /**
     * @param lmsCourseSet
     *            the lmsCourseSet to set
     */
    public final void setLmsCourseSet( final LmsCourseSet lmsCourseSet ) {
        this.lmsCourseSet = lmsCourseSet;
    }

    /**
     * Check if the course has {@link PosTag} instances.
     * 
     * @return
     */
    public boolean isPosTagged() {
        if( null == this.getTags( TagType.POS_TAG ) ) {
            return false;
        }

        return this.getTags( TagType.POS_TAG ).size() > 0 ? true : false;
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    public URI getRdfUri() throws URISyntaxException {
        // // first try to get URL from import
        // final String url = this.getUrl();
        //
        // URL exists, so try to embed in URI instance
        URI uri = null;
        //
        // if( null != url ) {
        // try {
        // uri = new URI( url );
        // } catch( final URISyntaxException e ) {
        // // leave uri null
        // }
        // }

        // if uri is still null, generate one
        if( null == uri ) {
            uri = this.generateUri();
        }

        return uri;
    }

    protected URI generateUri() throws URISyntaxException {
        final URI uri = new URI( INDIVIDUAL_BASE_URI
                + this.getLmsCourseSet().getName() + "/" + this.getId() );

        return uri;
    }

}
