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
	String ANONYM_NER_GERMAN_PRIMARY = "anonym.ner.german.primary";
	String ANONY_STRATEGY_CLASS = "anonym.strategy.class";
	
	String ANONYM_NER_ENGLISH_3CLASS_FILE = "anonym.ner.english.3class";
	String ANONYM_NER_ENGLISH_4CLASS_FILE = "anonym.ner.english.4class";
	String ANONYM_NER_ENGLISH_7CLASS_FILE = "anonym.ner.english.7class";
	String ANONYM_NER_ENGLISH_PRIMARY = "anonym.ner.english.primary";
	
	/*
	* import module
	*/
	String IMPORT_STRATEGY_CLASS = "import.strategy.class";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_TESTFOLDER = "import.strategy.directoryimport.testfolder";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER = "import.strategy.directoryimport.datafolder";

	/*
	 * export module 
	 */
	String EXPORT_STRATEGY_CLASS = "export.strategy.class";
	String EXPORT_STRATEGY_DIRECTORYEXPORT_DATAFOLDER = "export.strategy.directoryexport.datafolder";
	String EXPORT_STRATEGY_DIRECTORYEXPORT_TESTFOLDER = "export.strategy.directoryexport.testfolder";
}
