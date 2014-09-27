/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;

/**
 * <p>Abstract class</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 27.09.2014
 *
 */
public abstract class AbstractPostprocessMethod implements IPostprocessMethod {
    protected LmsCourseSet lmsCourseSet;    

    /**
     * @return the lmsCourseSet
     */
    public LmsCourseSet getLmsCourseSet()
    {
        return lmsCourseSet;
    }

    /**
     * @param lmsCourseSet the lmsCourseSet to set
     * @throws NullPointerException
     */
    public void setLmsCourseSet(LmsCourseSet lmsCourseSet)
    {
        if (null == lmsCourseSet) {
            throw new NullPointerException("Null is not allowed as parameter value.");
        }
        
        this.lmsCourseSet = lmsCourseSet;
    }
    
    /**
     * @throws IllegalStateException
     */
    protected void checkIfLmsCourseIsSet()
    {
        if (null == this.getLmsCourseSet()) {
            throw new IllegalStateException("You need to set an instance of LmsCourseSet.");
        }
    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.method.IPostprocessMethod#afterImport()
     */
    @Override
    public void afterImport() 
    {
        this.checkIfLmsCourseIsSet();
        this.applyMethod();
    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.method.IPostprocessMethod#afterAnonymization()
     */
    @Override
    public void afterAnonymization() 
    {
        this.checkIfLmsCourseIsSet();
        this.applyMethod();
    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.method.IPostprocessMethod#afterSemanticExtraction()
     */
    @Override
    public void afterSemanticExtraction() 
    {
        this.checkIfLmsCourseIsSet();
        this.applyMethod();
    }

    /**
     * Define the main algorithm of the TemplateMethod here.
     * In general, each event method calls this method. In special cases it might be required
     * to do further work for special events.
     */
    protected abstract void applyMethod();
}
