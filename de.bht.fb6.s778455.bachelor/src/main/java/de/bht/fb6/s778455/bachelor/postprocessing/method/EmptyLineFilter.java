/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 * 
 * http://saschafeldmann.de 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.StringUtil;

/**
 * <p>Filter / remove all empty lines.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 14.10.2014
 *
 */
public class EmptyLineFilter extends AbstractPostprocessMethod {

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.postprocessing.method.AbstractPostprocessMethod#applyMethod()
     */
    @Override
    protected void applyMethod() 
    {
        this.removeEmptyLines();
    }
    
    /**
     * Remove moodle characters in each {@link Board}, {@link BoardThread} and {@link Posting}.
     */
    private void removeEmptyLines() 
    {
        for (Course course : this.getLmsCourseSet()) {            
            for (Board board : course.getBoards()) {
                for (BoardThread boardThread : board.getBoardThreads()) {
                    for (Posting posting : boardThread.getPostings()) {
                        this.applyFilterOnPosting(posting);
                    }
                }
            }
        }               
    }
    
    /**
     * Try to apply the filter on the posting if it has content.
     * @param posting
     */
    private void applyFilterOnPosting(Posting posting) 
    {
        if (null != posting.getContent()) {
            posting.setContent(StringUtil.removeEmptyLines(posting.getContent()));
        }
    }

}
