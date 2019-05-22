package org.codehaus.plexus.component.configurator.converters.basic;

public class BooleanConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Boolean;

   public boolean canConvert(Class type) {
      return type.equals(Boolean.TYPE) || type.equals(class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean);
   }

   public Object fromString(String str) {
      return str.equals("true") ? Boolean.TRUE : Boolean.FALSE;
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
