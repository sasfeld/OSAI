/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization.corpus;

import java.util.List;

import de.bht.fb6.s778455.bachelor.anonymization.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;

/**
 * 
 * <p>This singleton class offers a corpus of common words for both - the English and German language.</p>
 * <p>Currently, the words have to be configured in the 'anonymization.properties'.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 18.01.2014
 *
 */
public class CommonWordCorpus {
    protected static CommonWordCorpus instance;
    
    protected List< String > commonGermanWords;
    protected List< String > commonEnglishWords;

    private CommonWordCorpus() throws InvalidConfigException {
        this.commonGermanWords = ServiceFactory.getConfigReader()
                .fetchMultipleValues( IConfigKeys.ANONYM_LEARNED_COMMON_GERMAN );
        this.commonEnglishWords = ServiceFactory.getConfigReader()
                .fetchMultipleValues( IConfigKeys.ANONYM_LEARNED_COMMON_ENGLISH );
        
        this.prepareCommonWords();       
    }
    
    protected void prepareCommonWords() {
        // trim all words from config
        for( String germanWord : this.commonGermanWords ) {
            germanWord = germanWord.trim();
        }       
        
        for( String englishWord : this.commonEnglishWords ) {
            englishWord = englishWord.trim();
        }
    }
    
   
    /**
     * @return the commonGermanWords
     */
    public final List< String > getCommonGermanWords() {
        return this.commonGermanWords;
    }

    /**
     * @return the commonEnglishWords
     */
    public final List< String > getCommonEnglishWords() {
        return this.commonEnglishWords;
    }

    /**
     * Get the singleton instance of common word corpus.
     * @return
     * @throws InvalidConfigException
     */
    public static CommonWordCorpus getCommonWordCorpus() throws InvalidConfigException {
        if ( null == instance ) {
            instance = new CommonWordCorpus();
        }
        
        return instance;
    }
}