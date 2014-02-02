/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store.ontology;

/**
 * <p>This interface holds the URLs to the datatype properties defined in the own ontology.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 *
 */
public interface IOwlDatatypeProperties extends IBaseUris {
    String PROPERTY_DATA_TITLE = BASE_URI + "property_data_title";
    String PROPERTY_DATA_WEB_URI = BASE_URI + "property_data_web_url";
    String PROPERTY_DATA_LANGUAGE_READABLE_NAME = BASE_URI + "property_data_language_readable_name";
    String PROPERTY_DATA_LANGUAGE_CODE = BASE_URI + "property_data_language_code";
    String PROPERTY_DATA_TOPIC_TITLE = BASE_URI + "property_datatype_topic_title";

}
