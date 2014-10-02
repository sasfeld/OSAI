/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.model.Posting;

/**
 * <p>Filter class for removing hyperlinks from the {@link Posting} instances.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.10.2014
 *
 */
public class HyperlinkFilter extends AbstractOliverFilterAdapter
{

    public static final String OLIVER_LINK_REPLACEMENT_TAG = "[LINK]";
    

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.postprocessing.method.AbstractPostprocessMethod#applyMethod()
     */
    @Override
    protected void applyMethod()
    {
        this.removeJavaCodesInPostings();
    }

    @Override
    protected void initializeFilter()
    {
        this.javaCodeFilter = new de.blum.nlp.filter.HyperlinkFilter();        
    }

    @Override
    protected String applyFurtherFilters(String filteredContent)
    {
       String replacedReplacementTags = filteredContent.replaceAll(OLIVER_LINK_REPLACEMENT_TAG, IReplacementTags.HYPERLINK_REPLACEMENT_TAG);
       
       return replacedReplacementTags;
    }

}
