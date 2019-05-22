package org.codehaus.plexus.component.configurator.converters.lookup;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.converters.ConfigurationConverter;

public interface ConverterLookup {
   void registerConverter(ConfigurationConverter var1);

   ConfigurationConverter lookupConverterForType(Class var1) throws ComponentConfigurationException;
}
