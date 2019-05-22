package org.codehaus.plexus.component.configurator.converters.basic;

public class ShortConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Short;

   public boolean canConvert(Class type) {
      return type.equals(Short.TYPE) || type.equals(class$java$lang$Short == null ? (class$java$lang$Short = class$("java.lang.Short")) : class$java$lang$Short);
   }

   public Object fromString(String str) {
      return Short.valueOf(str);
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
