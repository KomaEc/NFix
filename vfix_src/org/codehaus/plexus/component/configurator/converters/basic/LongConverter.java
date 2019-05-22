package org.codehaus.plexus.component.configurator.converters.basic;

public class LongConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Long;

   public boolean canConvert(Class type) {
      return type.equals(Long.TYPE) || type.equals(class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long);
   }

   public Object fromString(String str) {
      return Long.valueOf(str);
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
