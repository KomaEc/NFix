package org.codehaus.plexus.component.configurator.converters.composite;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class PlexusConfigurationConverter extends AbstractConfigurationConverter {
   // $FF: synthetic field
   static Class class$org$codehaus$plexus$configuration$PlexusConfiguration;

   public boolean canConvert(Class type) {
      return (class$org$codehaus$plexus$configuration$PlexusConfiguration == null ? (class$org$codehaus$plexus$configuration$PlexusConfiguration = class$("org.codehaus.plexus.configuration.PlexusConfiguration")) : class$org$codehaus$plexus$configuration$PlexusConfiguration).isAssignableFrom(type);
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      return configuration;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
