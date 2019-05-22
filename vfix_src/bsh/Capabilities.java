package bsh;

import java.util.Hashtable;

public class Capabilities {
   private static boolean accessibility = false;
   private static Hashtable classes = new Hashtable();
   // $FF: synthetic field
   static Class class$java$lang$String;

   public static boolean haveSwing() {
      return classExists("javax.swing.JButton");
   }

   public static boolean canGenerateInterfaces() {
      return classExists("java.lang.reflect.Proxy");
   }

   public static boolean haveAccessibility() {
      return accessibility;
   }

   public static void setAccessibility(boolean var0) throws Capabilities.Unavailable {
      if (!var0) {
         accessibility = false;
      } else if (classExists("java.lang.reflect.AccessibleObject") && classExists("bsh.reflect.ReflectManagerImpl")) {
         try {
            (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).getDeclaredMethods();
         } catch (SecurityException var2) {
            throw new Capabilities.Unavailable("Accessibility unavailable: " + var2);
         }

         accessibility = true;
      } else {
         throw new Capabilities.Unavailable("Accessibility unavailable");
      }
   }

   public static boolean classExists(String var0) {
      Object var1 = classes.get(var0);
      if (var1 == null) {
         try {
            var1 = Class.forName(var0);
         } catch (ClassNotFoundException var3) {
         }

         if (var1 != null) {
            classes.put(var1, "unused");
         }
      }

      return var1 != null;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class Unavailable extends UtilEvalError {
      public Unavailable(String var1) {
         super(var1);
      }
   }
}
