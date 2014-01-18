/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class realizes an {@link AExtractionStrategy} which detects the language of {@link AUserContribution} or an {@link Course}.</p>
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.01.2014
 *
 */
public class LanguageDetectionStrategy extends AExtractionStrategy {

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
        // TODO Auto-generated method stub

    }

}
