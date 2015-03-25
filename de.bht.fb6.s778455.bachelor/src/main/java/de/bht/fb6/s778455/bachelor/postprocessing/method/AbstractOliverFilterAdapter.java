/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.blum.nlp.filter.Filter;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 02.10.2014
 *
 */
public abstract class AbstractOliverFilterAdapter extends AbstractPostprocessMethod
{

    protected Filter javaCodeFilter;
    
    public AbstractOliverFilterAdapter()
    {
        this.initializeFilter();
    }
    
    
    /**
     * Initialize the Filter instance.
     */
    protected abstract void initializeFilter();
    
    /**
     * Implement further filtering work that is called in removeJavaCodeInPosting().
     * @param filteredContent 
     * 
     * @return
     */
    protected abstract String applyFurtherFilters(String filteredContent);
    
    /**
     * Iterate through model hierarchy.
     */
    protected void removeJavaCodesInPostings()
    {
        for (Course course : this.getLmsCourseSet()) {
            for (Board board : course.getBoards()) {
                for (BoardThread boardThread : board.getBoardThreads()) {
                    for (Posting posting : boardThread.getPostings()) {
                        this.removeJavaCodeInContent(posting);
                    }
                }
            }
        }        
    }

    /**
     * Apply Olivers filter and set the filtered content.
     * @param userContribution
     */
    protected void removeJavaCodeInContent(Posting userContribution)
    {
        String filteredContent = this.javaCodeFilter.remove(userContribution.getContent()); 
        filteredContent = this.applyFurtherFilters(filteredContent);
        
        userContribution.setContent(filteredContent);        
    }
}
