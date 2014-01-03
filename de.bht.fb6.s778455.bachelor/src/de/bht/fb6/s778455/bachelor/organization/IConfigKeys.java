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
	/**
	 * Key to fetch multiple corpus values.
	 */
	String ANONYM_STRATEGY_CHAIN = "anonym.strategy.chain";
	String ANONYM_NER_GERMAN_STRATEGY_CLASS = "anonym.ner.german.strategy.class";
	String ANONYM_NER_GERMAN_CORPORA = "anonym.ner.german.corpus";
	String ANONYM_NER_GERMAN_DEWAC_FILE = "anonym.ner.german.corpus.dewac";
	String ANONYM_NER_GERMAN_HGC_FILE = "anonym.ner.german.corpus.hgc";
	String ANONYM_NER_GERMAN_PRIMARY = "anonym.ner.german.primary";
	String ANONY_STRATEGY_CLASS = "anonym.strategy.class";
	String ANONYM_NER_GERMAN_CASCADE = "anonym.ner.german.cascade";
	
	/**
	 * Key to fetch multiple corpus values.
	 */
	String ANONYM_NER_ENGLISH_STRATEGY_CLASS = "anonym.ner.english.strategy.class";
	String ANONYM_NER_ENGLISH_CORPORA = "anonym.ner.english.corpus";	
	String ANONYM_NER_ENGLISH_3CLASS_FILE = "anonym.ner.english.corpus.3class";
	String ANONYM_NER_ENGLISH_4CLASS_FILE = "anonym.ner.english.corpus.4class";
	String ANONYM_NER_ENGLISH_7CLASS_FILE = "anonym.ner.english.corpus.7class";
	String ANONYM_NER_ENGLISH_PRIMARY = "anonym.ner.english.primary";
	String ANONYM_NER_ENGLISH_CASCADE = "anonym.ner.english.cascade";
	
	/**
	 * Key to fetch multiple greetings in German.
	 */
	String ANONYM_GREETINGS_GERMAN = "anonym.greetings.german";
	String ANONYM_GREETINGS_NUMBEROFLINES = "anonym.greetings.numberoflines";
	String ANONYM_GREETINGS_LEARNED_MAXWORDS = "anonym.greetings.learned.maxwords";
	
	/**
	 *  key to the most common German words
	 */
	String ANONYM_LEARNED_COMMON_GERMAN = "anonym.learned.common.german";
	
	/**
	 * key to the most common English words
	 */
	String ANONYM_LEARNED_COMMON_ENGLISH = "anonym.learned.common.english";
	
	/*
	* import module
	*/
	String IMPORT_STRATEGY_CLASS = "import.strategy.class";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_TESTFOLDER = "import.strategy.directoryimport.testfolder";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_DATAFOLDER = "import.strategy.directoryimport.datafolder";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_NAMECORPUS_PRENAMES = "import.strategy.directoryimport.namecorpus.prenames";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_NAMECORPUS_LASTNAMES = "import.strategy.directoryimport.namecorpus.lastnames";
	String IMPORT_STRATEGY_POSTGRESQL_DUMPFOLDER = "import.strategy.postgresql.dumpfolder";
	String IMPORT_STRATEGY_POSTGRESQL_TESTFOLDER = "import.strategy.postgresql.testfolder";
	String IMPORT_STRATEGY_NAMECORPUS_BOARDSPECIFIC = "import.strategy.namecorpus.coursespecific";
	String IMPORT_STRATEGY_DIRECTORYIMPORT_ENCODING = "import.strategy.directoryimport.encoding";
	String IMPORT_STRATEGY_AUDITORIUM_DB_HOST = "import.strategy.auditorium.db.host";
	String IMPORT_STRATEGY_AUDITORIUM_DB_USER = "import.strategy.auditorium.db.user";
	String IMPORT_STRATEGY_AUDITORIUM_DB_PW = "import.strategy.auditorium.db.password";
	String IMPORT_STRATEGY_LUEBECK_DATA = "import.strategy.luebeck.datafolder";
	/*
	 * export module 
	 */
	String EXPORT_STRATEGY_CLASS = "export.strategy.class";
	String EXPORT_STRATEGY_DIRECTORYEXPORT_DATAFOLDER = "export.strategy.directoryexport.datafolder";
	String EXPORT_STRATEGY_DIRECTORYEXPORT_TESTFOLDER = "export.strategy.directoryexport.testfolder";
	String EXPORT_STRATEGY_SERIALIZED_DATAFOLDER = "export.strategy.serialized.datafolder";
	String EXPORT_STRATEGY_DIRECTORYEXPORT_ENCODING = "export.strategy.directoryexport.encoding";
	String IMPORT_STRATEGY_AUDITORIUM_DB_DBNAME = "import.strategy.auditorium.db.dbname";
}
