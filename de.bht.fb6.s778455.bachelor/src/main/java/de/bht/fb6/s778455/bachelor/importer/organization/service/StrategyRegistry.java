/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.importer.organization.service;

import java.util.HashMap;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.importer.AImportStrategy;
import de.bht.fb6.s778455.bachelor.importer.ImportMethod;

/**
 * <p>This registry class contains all the AImportStrategy instances.</p>
 * 
 * <p>They should only be fetched using this registry.</p>
 * 
 * <p>The registry delegates to the ServiceFactory to create the instances once.</p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class StrategyRegistry {
    /**
     * Mapping of import method to the associated import strategy.
     */
    protected static Map<ImportMethod, AImportStrategy> importStrategyMap;
    
    static {
        importStrategyMap = new HashMap<>();
        fillImportStrategies();
    }
    
    /**
     * Fill the importStrategyMap.
     * 
     * Create the instances once.
     */
    protected static void fillImportStrategies()
    {
       for (ImportMethod importMethod : ImportMethod.values()) {
          if (ImportMethod.AUDITORIUM_DB == importMethod) {
              importStrategyMap.put(importMethod, ServiceFactory.newAuditoriumDbStrategy());
              continue;
          }
          
          if (ImportMethod.FILESYSTEM == importMethod) {
              importStrategyMap.put(importMethod, ServiceFactory.newDirectoryImportStrategy());
              continue;
          }
          
          if (ImportMethod.LUEBECK_XML == importMethod) {
              importStrategyMap.put(importMethod, ServiceFactory.newMoodleXmlStrategy());
              continue;
          }
          
          if (ImportMethod.OLIVER_LUEBECK_XML == importMethod) {
              importStrategyMap.put(importMethod, ServiceFactory.newOliverLuebeckStrategy());
              continue;
          }
          
          if (ImportMethod.POSTGREDUMP == importMethod) {
              importStrategyMap.put(importMethod, ServiceFactory.newPostgreDumpStrategy());
              continue;
          }
       }
    }
    
    /**
     * Get the AImportStrategy attached to the given import method from the registry.
     * 
     * @param method
     * @return
     */
    public static AImportStrategy getImportStrategy(ImportMethod method)
    {
        return importStrategyMap.get(method);
    }
}
