/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.model;

import de.bht.fb6.s778455.bachelor.exporter.experimental.DirectoryExportStrategy;

/**
 * 
 * <p>Interface for models which implement APIs for the {@link DirectoryImportStrategy} and {@link DirectoryExportStrategy}.
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.12.2013
 * 
 */
public interface IDirectoryPortable {

	/**
	 * Get a representation of this model for a *.txt String.
	 * 
	 * @return
	 */
	public abstract String exportToTxt();

	/**
	 * Import from a txt file
	 * @param key
	 * @param value
	 */
	public abstract void importFromTxt( String key, String value );

}