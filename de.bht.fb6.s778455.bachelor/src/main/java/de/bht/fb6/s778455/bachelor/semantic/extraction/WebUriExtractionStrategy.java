/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction;

import java.net.MalformedURLException;
import java.net.URL;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * <p>This class just adds the web URIs of the origins.</p>
 * 
 * <p>This should only be done ONCE!</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 04.02.2014
 *
 */
public class WebUriExtractionStrategy extends AExtractionStrategy {
    private static final String BASE_URL_BEUTH = "https://lms.beuth-hochschule.de/moodle/";
    public static String BASE_URL_LUEBECK = "https://elearning.fh-luebeck.de/";
    public static String BASE_URL = BASE_URL_LUEBECK;
    


    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.AUserContribution)
     */
    @Override
    public void extractSemantics( final AUserContribution userContribution )
            throws GeneralLoggingException {
        
        if ( userContribution instanceof Board ) {
            final Board b = (Board) userContribution;
            
            if ( BASE_URL.equals( BASE_URL_LUEBECK ) || BASE_URL.equals( BASE_URL_BEUTH )) {
                try {
                    b.setWebUrl( new URL ( BASE_URL + "mod/forum/view.php?id=" + userContribution.getId() ));
                } catch( final MalformedURLException e ) {
                   Application.log( "Couldnt set URL", LogType.ERROR, this.getClass() );
                }
            }
        }
        
        if ( userContribution instanceof BoardThread ) {
            final Board b = (Board) userContribution;
            
            if ( BASE_URL.equals( BASE_URL_LUEBECK ) || BASE_URL.equals( BASE_URL_BEUTH )) {
                try {
                    b.setWebUrl( new URL ( BASE_URL + "mod/forum/discuss.php?id=" + userContribution.getId() ));
                } catch( final MalformedURLException e ) {
                    Application.log( "Couldnt set URL", LogType.ERROR, this.getClass() );
                }
            }
        }

    }

    /* (non-Javadoc)
     * @see de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy#extractSemantics(de.bht.fb6.s778455.bachelor.model.Course)
     */
    @Override
    public void extractSemantics( final Course course )
            throws GeneralLoggingException {
        // TODO Auto-generated method stub

    }

}
