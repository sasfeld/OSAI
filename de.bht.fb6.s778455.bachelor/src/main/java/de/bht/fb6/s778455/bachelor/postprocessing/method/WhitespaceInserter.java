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
 * <p>This implementation was designed to insert whitespaces after punctuation characters.</p>
 * 
 * <p>It should be applied on raw {@link Posting} instances to make sure that the NLP processing works well.</p>
 
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 14.10.2014
 */
public class WhitespaceInserter extends AbstractPostprocessMethod 
{

    @Override
    protected void applyMethod() 
    {
       this.fillWhitespaces();        
    }
    
    /**
     * Remove moodle characters in each {@link Board}, {@link BoardThread} and {@link Posting}.
     */
    private void fillWhitespaces() 
    {
        for (Course course : this.getLmsCourseSet()) {            
            for (Board board : course.getBoards()) {
                for (BoardThread boardThread : board.getBoardThreads()) {
                    for (Posting posting : boardThread.getPostings()) {
                        this.fillWhitespaceOnPosting(posting);
                    }
                }
            }
        }               
    }
    
    /**
     * Try to apply the filter on the posting if it has content.
     * @param posting
     */
    private void fillWhitespaceOnPosting(Posting posting) 
    {
        if (null != posting.getContent()) {
            posting.setContent(StringUtil.fillMissingWhitespaces(posting.getContent()));
        }
    }

}
