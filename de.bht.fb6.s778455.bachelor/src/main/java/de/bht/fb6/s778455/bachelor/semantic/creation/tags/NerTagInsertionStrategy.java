/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.creation.tags;

import de.bht.fb6.s778455.bachelor.model.NerTag;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.semantic.store.RdfTripleStoreAdapter;

/**
 * <p>This class realizes the insertion of {@link NerTag} instances.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 19.02.2014
 *
 */
public class NerTagInsertionStrategy extends ATagInsertionStrategy {
    /**
     * Create a new strategy which fetches the {@link RdfTripleStoreAdapter}
     * from the {@link ServiceFactory}.
     */
    public NerTagInsertionStrategy() {
        super();
    }

    /**
     * Create a new strategy with an injected {@link RdfTripleStoreAdapter}.
     * 
     * @param adapter
     */
    public NerTagInsertionStrategy( final RdfTripleStoreAdapter adapter ) {
        super( adapter );
    }
}
