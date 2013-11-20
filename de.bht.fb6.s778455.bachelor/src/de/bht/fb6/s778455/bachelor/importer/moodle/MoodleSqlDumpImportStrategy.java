/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */


package de.bht.fb6.s778455.bachelor.importer.moodle;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;

/**
 * 
 * <p>This class implements the functionality to import raw dump files from moodle to this software.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class MoodleSqlDumpImportStrategy extends AImportStrategy {

	@Override
	public Map< String, Board > importFromStream( InputStream inputStream ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map< String, Board > importFromFile( File inputFile )
			throws GeneralLoggingException {
		// TODO Auto-generated method stub
		return null;
	}
}
