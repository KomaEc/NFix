package org.codehaus.plexus.component.configurator.converters.basic;

public class ByteConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Byte;

   public boolean canConvert(Class type) {
      return type.equals(Byte.TYPE) || type.equals(class$java$lang$Byte == null ? (class$java$lang$Byte = class$("java.lang.Byte")) : class$java$lang$Byte);
   }

   public Object fromString(String str) {
      return new Byte((byte)Integer.parseInt(str));
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
