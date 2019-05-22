package com.gzoltar.client.utils;

public class ArrayUtils {
   public static boolean startsWith(String[] var0, String var1) {
      int var2 = (var0 = var0).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var0[var3];
         if (var1.startsWith(var4) && var1.length() > var4.length() + 1) {
            return true;
         }
      }

      return false;
   }

   public static <T> boolean contains(T[] var0, T var1) {
      int var2;
      int var3;
      if (var1 == null) {
         var2 = (var0 = var0).length;

         for(var3 = 0; var3 < var2; ++var3) {
            if (var0[var3] == null) {
               return true;
            }
         }
      } else {
         var2 = (var0 = var0).length;

         for(var3 = 0; var3 < var2; ++var3) {
            Object var4;
            if ((var4 = var0[var3]) == var1 || var1.equals(var4)) {
               return true;
            }
         }
      }

      return false;
   }
}
