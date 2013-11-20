/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.organization;

/**
 * <p>This interface stores the keys of the configuration target (e.g.: a file) in an unique way.</p>
 * 
 * <p>This interface only contains constants.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 13.11.2013
 *
 */
public interface IConfigKeys {
	/*
	* anonymization module
	*/
	String ANONYM_NER_GERMAN_DEWAC_FILE = "anonym.ner.german.dewac";
	String ANONYM_NER_GERMAN_HGC_FILE = "anonym.ner.german.hgc";
	String ANONY_STRATEGY_CLASS = "anonym.strategy.class";
	
	/*
	* import module
	*/
	String IMPORT_STRATEGY_CLASS = "import.strategy.class";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_TESTFOLDER = "import.strategy.directoryimport.testfolder";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER = "import.strategy.directoryimport.datafolder";
}
