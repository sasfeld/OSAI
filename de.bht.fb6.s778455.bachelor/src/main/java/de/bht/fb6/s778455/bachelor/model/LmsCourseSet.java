/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This class handles the courses. It behaves like a {@link Set} but follows the implementation of a {@link Collection}.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 *
 */
public class LmsCourseSet extends HashSet< Course >{       
    private static final long serialVersionUID = 1L;
    
    /**
     * Create a LmsCourseSet from an existing collection.
     * @param values
     */
    public LmsCourseSet( final Collection< Course > values ) {
        super();
        
        if ( null == values ) {
            throw new IllegalArgumentException( this.getClass() + ":LmsCourseSet: null value of parameter is not allowed!" );
        }
        
        super.addAll( values );
    }
    
    public LmsCourseSet() {
        super();
    }
    
}
