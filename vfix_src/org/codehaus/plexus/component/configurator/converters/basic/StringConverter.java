package org.codehaus.plexus.component.configurator.converters.basic;

public class StringConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$String;

   public boolean canConvert(Class type) {
      return type.equals(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
   }

   public Object fromString(String str) {
      return str;
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
