/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.anonymization.strategy.AAnomyzationStrategy;

/**
 * <p>Replacement tags indicate that an {@link IPostprocessMethod} filtered some entity.</p>
 * 
 * 
 * @see IPostprocessMethod
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.10.2014
 *
 */
public interface IReplacementTags
{
    /**
     * Removed hyperlink.
     * @see HyperlinkFilter
     */
    static String HYPERLINK_REPLACEMENT_TAG = "<REMOVED_HYPERLINK>";
    
    /**
     * Removed personal data, such as e-mail addresses.
     * @see PersonalDataFilter
     */
    static String PERSONAL_DATA_REPLACEMENT_TAG = AAnomyzationStrategy.PERSONAL_DATA_REPLACEMENT;
    
}
