/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class just adds the web URIs of the origins.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 *
 */
public class WebUriExtractionStrategy extends AExtractionStrategy {

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
     */
    @Override
    public void extractSemantics( final AUserContribution userContribution )
            throws GeneralLoggingException {
       

    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
     */
    @Override
    public void extractSemantics( final Course course )
            throws GeneralLoggingException {
        // TODO Auto-generated method stub

    }

}
