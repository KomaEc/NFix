package org.codehaus.plexus.component.configurator.converters.basic;

public class FloatConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Float;

   public boolean canConvert(Class type) {
      return type.equals(Float.TYPE) || type.equals(class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float);
   }

   public Object fromString(String str) {
      return Float.valueOf(str);
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
