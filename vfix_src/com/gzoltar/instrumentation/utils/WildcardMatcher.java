package com.gzoltar.instrumentation.utils;

import java.util.regex.Pattern;

public class WildcardMatcher {
   private final Pattern pattern;

   public WildcardMatcher(String var1) {
      String[] var2 = var1.split("\\:");
      StringBuilder var7 = new StringBuilder(var1.length() << 1);
      boolean var3 = false;
      int var4 = (var2 = var2).length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var2[var5];
         if (var3) {
            var7.append('|');
         }

         var7.append('(').append(toRegex(var6)).append(')');
         var3 = true;
      }

      this.pattern = Pattern.compile(var7.toString());
   }

   private static CharSequence toRegex(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length() << 1);
      char[] var5;
      int var2 = (var5 = var0.toCharArray()).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4;
         switch(var4 = var5[var3]) {
         case '*':
            var1.append(".*");
            break;
         case '?':
            var1.append(".?");
            break;
         default:
            var1.append(Pattern.quote(String.valueOf(var4)));
         }
      }

      return var1;
   }

   public boolean matches(String var1) {
      return this.pattern.matcher(var1).matches();
   }
}
