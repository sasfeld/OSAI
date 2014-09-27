/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.blum.nlp.filter.Filter;

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
public class JavaCodeFilter extends AbstractPostprocessMethod
{
    /**
     * Olivers Filter instance.
     */
    protected Filter javaCodeFilter;
    
    public JavaCodeFilter()
    {
        this.initializeFilter();
    }
    
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

    /**
     * Iterate through model hierarchy.
     */
    private void removeJavaCodesInPostings()
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
    private void removeJavaCodeInContent(Posting userContribution)
    {
        String filteredContent = this.javaCodeFilter.remove(userContribution.getContent());        
        userContribution.setContent(filteredContent);        
    }

}
