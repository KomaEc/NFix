package org.codehaus.plexus.component.configurator.converters.basic;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;

public class IntConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Integer;

   public boolean canConvert(Class type) {
      return type.equals(Integer.TYPE) || type.equals(class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer);
   }

   public Object fromString(String str) throws ComponentConfigurationException {
      try {
         return Integer.valueOf(str);
      } catch (NumberFormatException var3) {
         throw new ComponentConfigurationException("Not a number: '" + str + "'", var3);
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
