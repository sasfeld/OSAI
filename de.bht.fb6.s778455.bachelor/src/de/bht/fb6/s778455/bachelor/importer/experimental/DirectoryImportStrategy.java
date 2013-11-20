/**
 * Copyright (c) 2013 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.experimental;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.model.Course;

/**
 * <p>This class implements the functionality to import from a file system.</p>
 * <p>The structure of the file system must follow this structure:
 * 
 * <ul>
 * 		<li>COURSE/BOARD - DIRECTORY</li>
 * 		<li>
 * 			<ul>
 * 				<li>THREAD - DIRECTORY</li>
 * 				<li>
 * 					<ul>
 * 						<li>posting1.txt</li>
 * 						</li>posting2.txt</li>
 * 				</li>
 * 			</ul>
 * 		</li>
 * </ul>
 * 
 * </p>
 *
 * <p>
 * The name of the top level directory will be the name of the {@link Course} instance.<br />
 * Also, the name of the thread directory will be the name of the resulting {@link Thread} instance.
 * </p>
 * 
 * <p>
 * The posting files names' must follow this scheme: posting[incrementalNumber].txt .<br />
 * The value for incrementalNumber starts at 1 and must be incremented by 1.
 * </p>
 * 
 * <p>
 * The content of a posting.txt file must follow this scheme: <br />
 * CREATION_DATETIME: e.g. Freitag, 28. Juni 2013, 18:37 (moodle view)<br />
 * CONTENT: (each following line will be interpreted to be content)
 * </p>
 *
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 *
 */
public class DirectoryImportStrategy extends AImportStrategy {

}
