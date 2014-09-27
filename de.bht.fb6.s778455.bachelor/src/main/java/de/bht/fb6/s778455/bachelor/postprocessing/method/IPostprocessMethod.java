/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.postprocessing.method;

import de.bht.fb6.s778455.bachelor.anonymization.controller.AnonymizationController;
import de.bht.fb6.s778455.bachelor.importer.organization.service.ProcessingFacade;
import de.bht.fb6.s778455.bachelor.model.LmsCourseSet;
import de.bht.fb6.s778455.bachelor.semantic.extraction.controller.SemanticExtractionController;

/**
 * <p>Interface (Template Method Pattern) for postprocesses, that define methods for events that are triggered by the controllers.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public interface IPostprocessMethod 
{
    /**
     * Triggered within the {@link ProcessingFacade} of the Import module, after the import was done and a new 
     * {@link LmsCourseSet} was generated.
     */
    void afterImport();
    
    /**
     * Triggered within the {@link AnonymizationController} right after the anonymization was processed.
     */
    void afterAnonymization();
    
    /**
     * Triggered within the {@link SemanticExtractionController} right after the extraction is completed.
     */
    void afterSemanticExtraction();
    
}
