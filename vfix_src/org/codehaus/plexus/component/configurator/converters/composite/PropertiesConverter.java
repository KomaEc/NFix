package org.codehaus.plexus.component.configurator.converters.composite;

import java.util.Properties;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class PropertiesConverter extends AbstractConfigurationConverter {
   // $FF: synthetic field
   static Class class$java$util$Properties;

   public boolean canConvert(Class type) {
      return (class$java$util$Properties == null ? (class$java$util$Properties = class$("java.util.Properties")) : class$java$util$Properties).isAssignableFrom(type);
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      Object retValueInterpolated = this.fromExpression(configuration, expressionEvaluator, type);
      if (retValueInterpolated != null) {
         return retValueInterpolated;
      } else {
         String element = configuration.getName();
         Properties retValue = new Properties();
         PlexusConfiguration[] children = configuration.getChildren("property");
         if (children != null && children.length > 0) {
            for(int i = 0; i < children.length; ++i) {
               PlexusConfiguration child = children[i];
               this.addEntry(retValue, element, child);
            }
         }

         return retValue;
      }
   }

   private void addEntry(Properties properties, String element, PlexusConfiguration property) throws ComponentConfigurationException {
      String name = property.getChild("name").getValue((String)null);
      String value;
      if (name == null) {
         value = "Converter: java.util.Properties. Trying to convert the configuration element: '" + element + "', missing child element 'name'.";
         throw new ComponentConfigurationException(value);
      } else {
         value = property.getChild("value").getValue("");
         properties.put(name, value);
      }
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
