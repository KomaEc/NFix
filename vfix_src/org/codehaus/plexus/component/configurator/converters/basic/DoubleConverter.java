package org.codehaus.plexus.component.configurator.converters.basic;

public class DoubleConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Double;

   public boolean canConvert(Class type) {
      return type.equals(Double.TYPE) || type.equals(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double);
   }

   public Object fromString(String str) {
      return Double.valueOf(str);
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
