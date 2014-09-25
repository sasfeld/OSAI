/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import de.bht.fb6.s778455.bachelor.importer.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.model.tools.IdComparator;

/**
 * <p>This class handles the courses. It behaves like a {@link Set} but follows the implementation of a {@link Collection}.</p>
 * <p>All added elements are sorted using an {@link IdComparator}. The course instances will be sorted ascending.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 25.01.2014
 *
 */
public class LmsCourseSet extends TreeSet< Course > implements IRdfUsable {       
    private static final long serialVersionUID = 1L;
    protected String name;
    
    /**
     * Create a LmsCourseSet from an existing collection.
     * @param values
     */
    public LmsCourseSet( final Collection< Course > values, final String courseSetName ) {
    	// assign ID comparator
        super(ServiceFactory.newIdComparator());
        
        if ( null == values ) {
            throw new IllegalArgumentException( this.getClass() + ":LmsCourseSet: null value of parameter is not allowed!" );
        }
        
        super.addAll( values );      
        
        this.initialize( courseSetName );
    }
    
    /**
     * Create a new courseSet with a given name.
     * @param courseSetName
     */
    public LmsCourseSet(final String courseSetName) {
    	// assign ID comparator
        super(ServiceFactory.newIdComparator());
        
        this.initialize( courseSetName );
    }

    protected void initialize( final String courseSetName ) {
        if ( null == courseSetName ) {
            throw new IllegalArgumentException("Null is not allowed as name for the courseSet!");
        }
        this.name = courseSetName;
    }
    /**
     * Name of the Course Set.
     * @param name
     */
    public void setName( final String name ) {
        if ( null == name ) {
            throw new IllegalArgumentException( "Null is not allowed as parameter value!" );
        }
        this.name = name;
    }
    
    
    /**
     * Get the name of this CourseSet
     * @return String
     */
    public String getName() {
        return this.name;
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.model.IRdfUsable#getRdfUri()
     */
    public URI getRdfUri() throws URISyntaxException {
        final URI newUri = new URI( INDIVIDUAL_BASE_URI + this.getName());
        return newUri;
    }
    
}
