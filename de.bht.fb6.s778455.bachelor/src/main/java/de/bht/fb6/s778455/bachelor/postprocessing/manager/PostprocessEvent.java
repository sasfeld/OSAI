/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.manager;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class PostprocessEvent 
{
    /**
     * 
     * <p>Enumeration of the available events.</p>
     *
     * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
     * @since 27.09.2014
     *
     */
    public enum PostProcessEvents 
    {
        AFTER_IMPORT, AFTER_ANONYMIZATION, AFTER_SEMANTIC_EXTRACTION;
        
        /**
         * Map a key from the configuration layer to an enumeration value.
         * @param configKey
         * @return
         * @throws IllegalArgumentException if no mapping could be done
         */
        public static PostProcessEvents mapConfigKey(String configKey)
        {
            switch (configKey) {
            case IConfigKeys.POSTPROCESSING_EVENT_AFTER_ANONYMIZATION:
                return AFTER_ANONYMIZATION;
            case IConfigKeys.POSTPROCESSING_EVENT_AFTER_IMPORT:
                return AFTER_IMPORT;
            case IConfigKeys.POSTPROCESSING_EVENT_AFTER_SEMANTIC_EXTRACTION:
                return AFTER_SEMANTIC_EXTRACTION;
            default:
                throw new IllegalArgumentException("The given configKey '"+ configKey +"'could not be mapped to a PostProcessEvent.");
            }
        }
    };
 
    protected LmsCourseSet lmsCourseSet;
    protected PostProcessEvents event;

    /**
     * 
     * @return
     */
    public final PostProcessEvents getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     */
    public final void setEvent(PostProcessEvents event) {
        this.event = event;
    }

    /**
     * 
     * @return
     */
    public final LmsCourseSet getLmsCourseSet() {
        return lmsCourseSet;
    }

    /**
     * 
     * @param lmsCourseSet
     */
    public final void setLmsCourseSet(LmsCourseSet lmsCourseSet) {
        this.lmsCourseSet = lmsCourseSet;
    }
    
    
}
