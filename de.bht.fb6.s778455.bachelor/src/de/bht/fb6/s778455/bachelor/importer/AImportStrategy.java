/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.importer;

import java.io.InputStream;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.Course;


public abstract class AImportStrategy {
	public Map<Course, Board> importFromStream(InputStream inputStream) {
		return null;	
	}
}
