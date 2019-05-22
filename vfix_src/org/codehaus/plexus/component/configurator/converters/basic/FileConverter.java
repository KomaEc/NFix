package org.codehaus.plexus.component.configurator.converters.basic;

import java.io.File;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

public class FileConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$io$File;

   public boolean canConvert(Class type) {
      return type.equals(class$java$io$File == null ? (class$java$io$File = class$("java.io.File")) : class$java$io$File);
   }

   public Object fromString(String str) {
      return new File(str);
   }

   public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type, Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator, ConfigurationListener listener) throws ComponentConfigurationException {
      File f = (File)super.fromConfiguration(converterLookup, configuration, type, baseType, classLoader, expressionEvaluator, listener);
      return f != null ? expressionEvaluator.alignToBaseDirectory(f) : null;
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
