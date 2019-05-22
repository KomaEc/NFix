package org.codehaus.plexus.component.configurator.converters.composite;

import java.util.Map;
import java.util.TreeMap;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class MapConverter extends AbstractConfigurationConverter {
   // $FF: synthetic field
   static Class class$java$util$Map;
   // $FF: synthetic field
   static Class class$java$util$Properties;

   public boolean canConvert(Class type) {
      return (class$java$util$Map == null ? (class$java$util$Map = class$("java.util.Map")) : class$java$util$Map).isAssignableFrom(type) && !(class$java$util$Properties == null ? (class$java$util$Properties = class$("java.util.Properties")) : class$java$util$Properties).isAssignableFrom(type);
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      String expression = configuration.getValue((String)null);
      Object retValue;
      if (expression == null) {
         Map map = new TreeMap();
         PlexusConfiguration[] children = configuration.getChildren();

         for(int i = 0; i < children.length; ++i) {
            PlexusConfiguration child = children[i];
            String name = child.getName();
            map.put(name, this.fromExpression(child, expressionEvaluator));
         }

         retValue = map;
      } else {
         retValue = this.fromExpression(configuration, expressionEvaluator);
      }

      return retValue;
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
