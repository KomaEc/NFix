package org.codehaus.plexus.component.configurator.converters.basic;

public class StringBufferConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$StringBuffer;

   public boolean canConvert(Class type) {
      return type.equals(class$java$lang$StringBuffer == null ? (class$java$lang$StringBuffer = class$("java.lang.StringBuffer")) : class$java$lang$StringBuffer);
   }

   public Object fromString(String str) {
      return new StringBuffer(str);
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
