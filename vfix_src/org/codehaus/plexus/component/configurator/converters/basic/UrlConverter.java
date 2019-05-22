package org.codehaus.plexus.component.configurator.converters.basic;

import java.net.MalformedURLException;
import java.net.URL;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;

public class UrlConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$net$URL;

   public boolean canConvert(Class type) {
      return type.equals(class$java$net$URL == null ? (class$java$net$URL = class$("java.net.URL")) : class$java$net$URL);
   }

   public Object fromString(String str) throws ComponentConfigurationException {
      try {
         return new URL(str);
      } catch (MalformedURLException var3) {
         throw new ComponentConfigurationException("Unable to convert '" + str + "' to an URL", var3);
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
