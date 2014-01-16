/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.model.Tag.TagType;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;

/**
 * 
 * <p>
 * This class describes any content that is made by a user.
 * </p>
 * <p>
 * Those can be: a {@link Board}, a {@link BoardThread} and a {@link Posting} of
 * course.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public abstract class AUserContribution implements IDirectoryPortable {
	protected Date creationDate;
	protected Date modificationDate;
	protected String title;
	protected int id;
	protected Language lang;

	protected Map< TagType, List< Tag > > tagMap;

	public AUserContribution() {
		this._initialize();
	}
	
	private void _initialize() {
		this.tagMap = new HashMap< TagType, List< Tag > >();
		this.lang = Language.UNKNOWN;
	}
	
	/**
     * @return the lang
     */
    public final Language getLang() {
        return this.lang;
    }

    /**
     * @param lang the lang to set
     */
    public final void setLang( final Language lang ) {
        this.lang = lang;
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
	public AUserContribution setId( final int id ) {
		this.id = id;
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
	public AUserContribution setModificationDate( final Date modificationDate ) {
		this.modificationDate = modificationDate;
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
	public AUserContribution setCreationDate( final Date creationDate ) {
		this.creationDate = creationDate;
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
	public AUserContribution setTitle( final String title ) {
		this.title = title;
		return this;
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

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ( ( this.creationDate == null ) ? 0 : this.creationDate.hashCode() );
        result = prime * result + this.id;
        result = prime * result + ( ( this.lang == null ) ? 0 : this.lang.hashCode() );
        result = prime
                * result
                + ( ( this.modificationDate == null ) ? 0 : this.modificationDate
                        .hashCode() );
        result = prime * result + ( ( this.tagMap == null ) ? 0 : this.tagMap.hashCode() );
        result = prime * result + ( ( this.title == null ) ? 0 : this.title.hashCode() );
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
        final AUserContribution other = ( AUserContribution ) obj;
        if( this.creationDate == null ) {
            if( other.creationDate != null )
                return false;
        } else if( !this.creationDate.equals( other.creationDate ) )
            return false;
        if( this.id != other.id )
            return false;
        if( this.lang != other.lang )
            return false;
        if( this.modificationDate == null ) {
            if( other.modificationDate != null )
                return false;
        } else if( !this.modificationDate.equals( other.modificationDate ) )
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
        return true;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append( "AUserContribution [getLang()=" );
        builder.append( this.getLang() );
        builder.append( ", getId()=" );
        builder.append( this.getId() );
        builder.append( ", getModificationDate()=" );
        builder.append( this.getModificationDate() );
        builder.append( ", getCreationDate()=" );
        builder.append( this.getCreationDate() );
        builder.append( ", getTitle()=" );
        builder.append( this.getTitle() );
        builder.append( ", isTopicZoomTagged()=" );
        builder.append( this.isTopicZoomTagged() );
        builder.append( ", isTagged()=" );
        builder.append( this.isTagged() );
        builder.append( ", getNumberTags()=" );
        builder.append( this.getNumberTags() );
        builder.append( ", isNerTagged()=" );
        builder.append( this.isNerTagged() );
        builder.append( "]" );
        return builder.toString();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.bht.fb6.s778455.bachelor.model.IDirectoryPortable#exportToTxt()
	 */
	@Override
	public String exportToTxt() {
		final StringBuilder exportStr = new StringBuilder();

		exportStr.append( "ID: " + this.getId() + "\n" );
		if( null != this.getCreationDate() ) {
			exportStr.append( "CREATION_DATETIME: "
					+ this.getCreationDate().getTime() + "\n" );
		}
		if( null != this.getModificationDate() ) {
			exportStr.append( "MODIFICATION_DATETIME: "
					+ this.getModificationDate().getTime() + "\n" );
		}
		exportStr.append( "TITLE: " + this.getTitle() + "\n" );

		// tags
		final List< Tag > topicZoomTags = this.getTags( TagType.TOPIC_ZOOM );

		if( null != topicZoomTags ) {
			for( final Tag tag : topicZoomTags ) {
				if( tag instanceof TopicZoomTag ) {
					final TopicZoomTag topicZoomTag = ( TopicZoomTag ) tag;
					exportStr.append( "TOPIC_ZOOM_TAG: " + "value:"
							+ topicZoomTag.getValue() + ";" + "weight:"
							+ topicZoomTag.getWeight() + ";" + "significance:"
							+ topicZoomTag.getSignificance() + ";"
							+ "degreegeneralization:"
							+ topicZoomTag.getDegreeGeneralization() + ";"
							+ "uri:" + topicZoomTag.getUri() + "\n" );
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
					exportStr.append( "NER_TAG: " + "value:"
							+ nerTag.getValue() + ";" + "weight:"
							+ nerTag.getWeight() + ";" 
							+ "uri:" + nerTag.getUri() + ";"
							+ "classifierlabel:" + nerTag.getClassifierLabel() + "\n" );
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
		            exportStr.append( "POS_TAG: " + "value:"
		                    + posTag.getValue() + ";" + "weight:"
		                    + posTag.getWeight() + ";"
		                    + "uri:" + posTag.getUri() + ";"
		                    + "postag:" + posTag.getPosTag() + "\n" );
		        } else {
		            Application
		            .log( this.getClass()
		                    + ":exportToTxt(): the saved tag is not of type PosTag. There must be some error in the extraction process. Only add TopicZoomTags when using TopicZoom.",
		                    LogType.ERROR );
		        }
		    }
		}

		return exportStr.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.bht.fb6.s778455.bachelor.model.IDirectoryPortable#importFromTxt(java
	 * .lang.String, java.lang.String)
	 */
	@Override
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
		}  else if( key.equals( "TOPIC_ZOOM_TAG" ) ) {
			this.parseTopicZoomValue( value );
		} else if ( key.equals( "NER_TAG" )) {
			this.parseNerTagValue( value );
		} else if ( key.equals( "POS_TAG" )) {
		    this.parsePosTagValue( value );
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
                final String key = mKeyValue.group(1);
                final String kValue = mKeyValue.group(2);           
                
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
                    Application.log( this.getClass() + ":parsePosTagValue(): key " + key + " couldn't be matched.", LogType.ERROR );;
                }
                
            }
        }           
        
        final Tag newTag = new PosTag(posTag, weight, name, uri);
        this.addTag(newTag, TagType.POS_TAG);        
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
				final String key = mKeyValue.group(1);
				final String kValue = mKeyValue.group(2);			
				
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
					Application.log( this.getClass() + ":parseTopicZoomValue(): key " + key + " couldn't be matched.", LogType.ERROR );;
				}
				
			}
		}			
		
		final Tag newTag = new TopicZoomTag( significance, degreegeneralization, weight, name, uri );
		this.addTag(newTag, TagType.TOPIC_ZOOM);
	}
	
	/**
	 * Parse the value for the key "NER_TAG" within a txt file and fill
	 * the Posting's tags.
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
				final String key = mKeyValue.group(1);
				final String kValue = mKeyValue.group(2);			
				
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
					Application.log( this.getClass() + ":parseNerTagValue(): key " + key + " couldn't be matched.", LogType.ERROR );;
				}
				
			}
		}			
		
		final Tag newTag = new NerTag(classifierlabel, weight, name, uri);
		this.addTag(newTag, TagType.NER_TAG);
	}

}
