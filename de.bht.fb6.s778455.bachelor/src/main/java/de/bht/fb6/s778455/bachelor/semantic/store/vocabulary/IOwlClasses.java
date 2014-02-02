/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.store.vocabulary;

/**
 * <p>This interface holds the URIs of the OWL classes from the own ontology..</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.02.2014
 *
 */
public interface IOwlClasses extends IBaseUris {    
    
    /*
     * ######### MODELS #########
     */
    String CLASS_LMS = BASE_URI_ID + "class_lms";
    String CLASS_COURSE = BASE_URI_ID + "class_course";
    String CLASS_BOARD = BASE_URI_ID + "class_board";
    String CLASS_THREAD = BASE_URI_ID + "class_thread";
    String CLASS_POSTING = BASE_URI_ID + "class_posting";
    
    /*
     * ######### ATTRIBUTES ########
     */
    String CLASS_LANGUAGE = BASE_URI_ID + "class_language";
    String CLASS_TOPIC = BASE_URI_ID + "class_topic";

}
