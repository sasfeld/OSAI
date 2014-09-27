/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de)
 */
package de.bht.fb6.s778455.bachelor.test.framework;

import de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader;
import de.bht.fb6.s778455.bachelor.organization.IService;
import de.bht.fb6.s778455.bachelor.organization.ServiceLocalisable;
import de.bht.fb6.s778455.bachelor.organization.ServicesCache;
import de.bht.fb6.s778455.bachelor.test.framework.service.UnitTestServiceFactory;

/**
 * <p>
 * Class that all unit tests should extend.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 12.06.2014
 * 
 */
public class UnitTest {
    protected static final String PATH_UNITTEST_DATA_FOLDER = "./data/unittest";
    protected static final String PATH_UNITTEST_CONFIG_FOLDER = APropertiesConfigReader.PATH_CONFIG_UNITTEST;
    
    protected static boolean bootstrapped = false;
    
    
    public UnitTest()
    {
        if (!bootstrapped) {
            _initalizeUnitTestServices();
            bootstrapped = true;
        }
    }

    protected void _initalizeUnitTestServices()
    {
        _addUnitTestServiceFactory();
        _addUnitTestConfigReader();
    }
    
    private void _addUnitTestServiceFactory() {
        IService s = new UnitTestServiceFactory();
        ServicesCache.addService(s);
    }

    protected void _addUnitTestConfigReader() 
    {
        IService s = new ConfigReader();
        ServicesCache.addService(s);
    }
    
    public ServiceLocalisable getServiceFactory()
    {
        return (ServiceLocalisable) ServicesCache.getService(UnitTestServiceFactory.SERVICE_NAME);
    }
}
