/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * <p>Util class to work with {@link Collection).</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 30.01.2014
 *
 */
public class CollectionUtil {
    
    /**
     * Build a set from any {@link Iterator}.
     * @param it
     * @return
     */
    public static <E > Set< E > buildSetFromIterator( final Iterator< E > it ) {
        final Set< E > set = new HashSet<>();
        
        while ( it.hasNext() ) {
            set.add( it.next() );
        }
        
        return set;
    }
}
