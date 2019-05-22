package soot.toDex;

import soot.options.Options;

public class Debug {
   public static boolean TODEX_DEBUG;

   public static void printDbg(String s, Object... objects) {
      TODEX_DEBUG = Options.v().verbose();
      if (TODEX_DEBUG) {
         Object[] var2 = objects;
         int var3 = objects.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            s = s + o.toString();
         }

         System.out.println(s);
      }

   }

   public static void printDbg(boolean c, String s, Object... objects) {
      if (c) {
         printDbg(s, objects);
      }

   }
}
