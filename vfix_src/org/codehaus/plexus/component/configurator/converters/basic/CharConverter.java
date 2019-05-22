package org.codehaus.plexus.component.configurator.converters.basic;

public class CharConverter extends AbstractBasicConverter {
   // $FF: synthetic field
   static Class class$java$lang$Character;

   public boolean canConvert(Class type) {
      return type.equals(Character.TYPE) || type.equals(class$java$lang$Character == null ? (class$java$lang$Character = class$("java.lang.Character")) : class$java$lang$Character);
   }

   public Object fromString(String str) {
      return new Character(str.charAt(0));
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
