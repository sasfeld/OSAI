/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.topicmodelling;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;

/**
 * <p>This class realizes a topic modelling strategy.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.01.2014
 *
 */
public class TopicModellingStrategy extends AExtractionStrategy {

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
     */
    @Override
    public void extractSemantics( final AUserContribution userContribution )
            throws GeneralLoggingException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
     */
    @Override
    public void extractSemantics( final Course course )
            throws GeneralLoggingException {
        // first: import course's postings to mallet
        this._importCourse();
    }

    private void _importCourse() {
            
    }

}
