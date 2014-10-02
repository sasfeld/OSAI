/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;

/**
 * <p>This class works as adapter for the JavaCodeFilter class of Oliver Blum.</p>
 * 
 * <p>It takes an {@link LmsCourseSet}, iterates over the {@link Posting} instances and applys the filter
 * on the content.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public class JavaCodeFilter extends AbstractOliverFilterAdapter
{    
   
    protected void initializeFilter()
    {
        this.javaCodeFilter = new de.blum.nlp.filter.JavaCodeFilter();        
    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.method.AbstractPostprocessMethod#applyMethod()
     */
    @Override
    protected void applyMethod()
    {
       this.removeJavaCodesInPostings();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.postprocessing.method.AbstractOliverFilterAdapter#applyFurtherFilters(java.lang.String)
     */
    protected String applyFurtherFilters(String filteredContent)
    {
        return filteredContent;
    }

  

}
