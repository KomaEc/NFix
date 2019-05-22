package org.codehaus.plexus.component.configurator.converters;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public interface ConfigurationConverter {
   boolean canConvert(Class var1);

   Object fromConfiguration(ConverterLookup var1, PlexusConfiguration var2, Class var3, Class var4, ClassLoader var5, ExpressionEvaluator var6) throws ComponentConfigurationException;

   Object fromConfiguration(ConverterLookup var1, PlexusConfiguration var2, Class var3, Class var4, ClassLoader var5, ExpressionEvaluator var6, ConfigurationListener var7) throws ComponentConfigurationException;
}
