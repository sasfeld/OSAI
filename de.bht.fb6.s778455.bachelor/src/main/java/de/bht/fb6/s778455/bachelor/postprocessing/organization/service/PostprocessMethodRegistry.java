/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.organization.service;

import java.util.HashMap;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.postprocessing.method.HyperlinkFilter;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.method.JavaCodeFilter;
import de.bht.fb6.s778455.bachelor.postprocessing.method.MoodleCharacterFilter;
import de.bht.fb6.s778455.bachelor.postprocessing.method.PersonalDataFilter;
import de.bht.fb6.s778455.bachelor.postprocessing.method.WhitespaceInserter;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class PostprocessMethodRegistry
{
    public static String FULL_CLASS_NAME_JAVA_CODE_FILTER = JavaCodeFilter.class.getName();
    public static String FULL_CLASS_NAME_WHITESPACE_INSERTER_FILTER = WhitespaceInserter.class.getName();
    public static String FULL_CLASS_NAME_HYPERLINK_FILTER = HyperlinkFilter.class.getName();
    public static String FULL_CLASS_NAME_MOODLE_CHARACTER_FILTER = MoodleCharacterFilter.class.getName();
    public static String FULL_CLASS_NAME_JAVA_PERSONAL_DATA_FILTER = PersonalDataFilter.class.getName();
    
    /**
     * Mapping of class name -> {@link IPostprocessMethod} instance.
     */
    protected static Map<String, IPostprocessMethod> postprocessMethodMap;
    
    static {
        postprocessMethodMap = new HashMap<>();
        fillPostprocessMethods();
    }
    
    /**
     * Fill the importStrategyMap.
     * 
     * Create the instances once.
     */
    protected static void fillPostprocessMethods()
    {
       postprocessMethodMap.put(FULL_CLASS_NAME_JAVA_CODE_FILTER, ServiceFactory.newJavaCodeFilter());
       postprocessMethodMap.put(FULL_CLASS_NAME_WHITESPACE_INSERTER_FILTER, ServiceFactory.newWhitespaceInserterFilter());
       postprocessMethodMap.put(FULL_CLASS_NAME_HYPERLINK_FILTER, ServiceFactory.newHyperlinkFilter());
       postprocessMethodMap.put(FULL_CLASS_NAME_MOODLE_CHARACTER_FILTER, ServiceFactory.newMoodleCharacterFilter());
       postprocessMethodMap.put(FULL_CLASS_NAME_JAVA_PERSONAL_DATA_FILTER, ServiceFactory.newPersonalDataFilter());       
    }

    /**
     * Get the {@link IPostprocessMethod} instance associated to the given class name.
     * @param configuredClassName
     * @return
     * @throws IllegalArgumentException
     */
    public static IPostprocessMethod getPostprocessMethod(String configuredClassName)
    {
        IPostprocessMethod method = postprocessMethodMap.get(configuredClassName);
        
        if (null == method) {
            throw new IllegalArgumentException("The given configuredClassName '" + configuredClassName + "'could not be mapped to an IPostprocessMethod instance.");
        }
        
        return method;
    }
}
