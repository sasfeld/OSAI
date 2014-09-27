/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.organization;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.postprocessing.manager.PostprocessEvent.PostProcessEvents;
import de.bht.fb6.s778455.bachelor.postprocessing.method.IPostprocessMethod;
import de.bht.fb6.s778455.bachelor.postprocessing.organization.service.PostprocessMethodRegistry;

/**
 * <p>
 * ConfigReader of the postprocessing module.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 * 
 */
public class ConfigReader extends APropertiesConfigReader
{
    /**
     * Cached map.
     */
    protected Map<PostProcessEvents, Set<IPostprocessMethod>> eventPostprocessMap;
    /**
     * Path to the anonymization.properties file.
     */
    public static final String PATH_POSTPROCESSING_PROPERTIES = PATH_CONFIG
            + "importer.properties";

    /**
     * Create a new special config reader for the importer module.
     * 
     * @see de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader for
     *      the functionality implementation
     */
    public ConfigReader()
    {
        super(new File(PATH_POSTPROCESSING_PROPERTIES));
    }

    /**
     * Return a mapping of configured PostProcessEvents and the
     * {@link IPostprocessMethod} (none, one or multiple) that should be
     * triggered for this event.
     * 
     * @return
     */
    public Map<PostProcessEvents, Set<IPostprocessMethod>> getEventPostprocessMap()
    {
        if (null == this.eventPostprocessMap) {
            this.fillPostprocessMap();
        }

        return this.eventPostprocessMap;
    }
    
    /**
     * Get all registered {@link IPostprocessMethod} instances for the given event.
     * @param event
     * @return
     */
    public Set<IPostprocessMethod> getPostprocessesForEvent(PostProcessEvents event)
    {
        return this.getEventPostprocessMap().get(event);
    }

    /**
     * Fill postprocessMap.
     */
    private void fillPostprocessMap()
    {
        this.eventPostprocessMap = new HashMap<>();

        this.fillPostprocessMapForEvent(POSTPROCESSING_EVENT_AFTER_IMPORT);
        this.fillPostprocessMapForEvent(POSTPROCESSING_EVENT_AFTER_ANONYMIZATION);
        this.fillPostprocessMapForEvent(POSTPROCESSING_EVENT_AFTER_SEMANTIC_EXTRACTION);
    }

    /**
     * @param eventConfigKey
     */
    private void fillPostprocessMapForEvent(String eventConfigKey)
    {
        try {
            List<String> configuredClasses = this.fetchMultipleValues(eventConfigKey);
            
            for (String configuredClass : configuredClasses) {
                // map config key to PostProcessEvent and get registered method from config value
                if (!("".equals(configuredClass.trim()))) {
                    IPostprocessMethod method = PostprocessMethodRegistry.getPostprocessMethod(configuredClass);
                    PostProcessEvents postProcessEvent = PostProcessEvents.mapConfigKey(eventConfigKey);
                    
                    // create set if no entry exists for this event
                    if (!this.eventPostprocessMap.containsKey(postProcessEvent)) {
                        this.eventPostprocessMap.put(postProcessEvent, new HashSet<IPostprocessMethod>());
                    }

                    // add postprocessing method registered for this event
                    this.eventPostprocessMap.get(postProcessEvent).add(method);
                } // else: no method should be triggered for this event
            }
        } catch (InvalidConfigException e) {
            Application.log("Could not add PostProcessMethods for the given event (configured in postprocessing.properties): " + eventConfigKey + ". Please make sure to apply the correct class names (comma-separated) or an empty value.", 
                    LogType.ERROR, getClass());
        }
    }
}
