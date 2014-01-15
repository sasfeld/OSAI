/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.nlp.ner;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.NerTag;
import de.bht.fb6.s778455.bachelor.model.Tag.TagType;

/**
 * <p>
 * This class realizes the mapping between outcomes of Stanford NLP (tagged
 * strings) and our models which shall be filled with semantical information.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 09.01.2014
 * 
 */
public class NerTagMapper {
	public enum MappableLabels {
		GERMAN_ORGANIZATION, GERMAN_LOCATION, GERMAN_MISC,
		ENGLISH_LOCATION, ENGLISH_ORGANIZATION, ENGLISH_MISC, ENGLISH_TIME, ENGLISH_MONEY,
		ENGLISH_PERCENT, ENGLISH_DATE;

		/**
		 * Get the {@link MappableLabels} for the given classifier label by
		 * Stanford.
		 * 
		 * @param classifierLabel
		 * @return the {@link MappableLabels} or null
		 */
		public MappableLabels getLabel( String classifierLabel ) {
			switch( classifierLabel.trim().toLowerCase() ) {
			case "i-org":
				return GERMAN_ORGANIZATION;
			case "i-loc":
				return GERMAN_LOCATION;
			case "i-misc":
				return GERMAN_MISC;
			case "location":
				return ENGLISH_LOCATION;
			case "organization":
				return ENGLISH_ORGANIZATION;
			case "misc":
				return ENGLISH_MISC;
			case "time":
				return ENGLISH_TIME;
			case "money":
				return ENGLISH_MONEY;
			case "percent":
				return ENGLISH_PERCENT;
			case "date":
				return ENGLISH_DATE;
			default:
				return null;
			}
		}

		/**
		 * Get the classifier label to the current {@link MappableLabels}.
		 * 
		 * @return a String
		 */
		public String getClassifierLabel() {
			switch( this ) {
			case GERMAN_ORGANIZATION:
				return "I-ORG";
			case GERMAN_LOCATION:
				return "I-LOC";
			case GERMAN_MISC:
				return "I-MISC";
			case ENGLISH_DATE:
				return "DATE";
			case ENGLISH_LOCATION:
				return "LOCATION";
			case ENGLISH_MISC:
				return "MISC";
			case ENGLISH_MONEY:
				return "MONEY";
			case ENGLISH_ORGANIZATION:
				return "ORGANIZATION";
			case ENGLISH_PERCENT:
				return "PERCENT";
			case ENGLISH_TIME:
				return "TIME";
						
			default:
				return null; // can't happen
			}
		}
	}

	protected Set< String > classifierLabels;

	/**
	 * Create a new {@link NerTagMapper} with a given set of labels (given by
	 * Stanford NLP).
	 * 
	 * @param classifierLabels
	 */
	public NerTagMapper( final Set< String > classifierLabels ) {
		this.classifierLabels = classifierLabels;
	}

	/**
	 * Map the given string with inline XML tags by Stanford NER to an instance
	 * of {@link AUserContribution}.
	 * 
	 * @param taggedString
	 * @param contribution
	 */
	public void mapToContribution( final String taggedString,
			final AUserContribution contribution ) {
		// parse tagged string for tagged entities
		for( MappableLabels label : MappableLabels.values() ) {
			final String classifierLabel = label.getClassifierLabel();
			final Pattern pNerTag = Pattern.compile( "<" + classifierLabel
					+ ">(.*?)</" + classifierLabel + ">" );
			final Matcher mNerTag = pNerTag.matcher( taggedString );
			
			while ( mNerTag.find() ) {
				final String tagContent = mNerTag.group( 1 );
				
				// create new NerTag
				final NerTag newTag = new NerTag( classifierLabel, -1.0, tagContent, "" );
				contribution.addTag( newTag, TagType.NER_TAGS );
			}
		}

	}
	
	public void mapToCourse( final String taggedString, final Course course) {
		for( MappableLabels label : MappableLabels.values() ) {
			final String classifierLabel = label.getClassifierLabel();
			final Pattern pNerTag = Pattern.compile( "<" + classifierLabel
					+ ">(.*?)</" + classifierLabel + ">" );
			final Matcher mNerTag = pNerTag.matcher( taggedString );
			
			while ( mNerTag.find() ) {
				final String tagContent = mNerTag.group( 1 );
				
				// create new NerTag
				final NerTag newTag = new NerTag( classifierLabel, -1.0, tagContent, "" );
				course.addTag( newTag, TagType.NER_TAGS );
			}
		}
	}

}
