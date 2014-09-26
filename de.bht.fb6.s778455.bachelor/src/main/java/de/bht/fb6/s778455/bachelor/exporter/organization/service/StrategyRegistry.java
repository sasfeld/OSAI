/**
 * Copyright (c) 2013-2014 Sascha Feldmann (me@saschafeldmann.de) 
 */
package de.bht.fb6.s778455.bachelor.exporter.organization.service;

import java.util.HashMap;
import java.util.Map;

import de.bht.fb6.s778455.bachelor.exporter.AExportStrategy;
import de.bht.fb6.s778455.bachelor.exporter.ExportMethod;

/**
 * <p></p>
 *
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 26.09.2014
 *
 */
public class StrategyRegistry {
    /**
     * Mapping of import method to the associated import strategy.
     */
    protected static Map<ExportMethod, AExportStrategy> exportStrategyMap;
    
    static {
        exportStrategyMap = new HashMap<>();
        fillExportStrategies();
    }
    
    /**
     * Fill the importStrategyMap.
     * 
     * Create the instances once.
     */
    protected static void fillExportStrategies()
    {
       for (ExportMethod exportMethod : ExportMethod.values()) {
          if (ExportMethod.FILESYSTEM == exportMethod) {
              exportStrategyMap.put(exportMethod, ServiceFactory.newDirectoryExportStrategy());
              continue;
          }          
       }
    }
    
    /**
     * Get the AImportStrategy attached to the given export method from the registry.
     * 
     * @param method
     * @return
     */
    public static AExportStrategy getExportStrategy(ExportMethod method)
    {
        return exportStrategyMap.get(method);
    }
}
