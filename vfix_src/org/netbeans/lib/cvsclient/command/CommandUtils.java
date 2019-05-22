package org.netbeans.lib.cvsclient.command;

import java.util.List;

public class CommandUtils {
   public static String getExaminedDirectory(String var0, String var1) {
      int var2 = var0.indexOf(var1);
      int var3 = var2 + var1.length() + 1;
      return var2 >= 0 && var0.length() >= var3 + 1 ? var0.substring(var3) : null;
   }

   public static String findUniqueString(String var0, List var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = var1.indexOf(var0);
         if (var2 >= 0) {
            return (String)var1.get(var2);
         } else {
            String var3 = new String(var0);
            var1.add(var3);
            return var3;
         }
      }
   }
}
