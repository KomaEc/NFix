package bsh;

import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtil {
   public static String[] split(String var0, String var1) {
      Vector var2 = new Vector();
      StringTokenizer var3 = new StringTokenizer(var0, var1);

      while(var3.hasMoreTokens()) {
         var2.addElement(var3.nextToken());
      }

      String[] var4 = new String[var2.size()];
      var2.copyInto(var4);
      return var4;
   }

   public static String[] bubbleSort(String[] var0) {
      Vector var1 = new Vector();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.addElement(var0[var2]);
      }

      int var3 = var1.size();
      boolean var4 = true;

      while(var4) {
         var4 = false;

         for(int var5 = 0; var5 < var3 - 1; ++var5) {
            if (((String)var1.elementAt(var5)).compareTo((String)var1.elementAt(var5 + 1)) > 0) {
               String var6 = (String)var1.elementAt(var5 + 1);
               var1.removeElementAt(var5 + 1);
               var1.insertElementAt(var6, var5);
               var4 = true;
            }
         }
      }

      String[] var7 = new String[var3];
      var1.copyInto(var7);
      return var7;
   }

   public static String maxCommonPrefix(String var0, String var1) {
      int var2;
      for(var2 = 0; var0.regionMatches(0, var1, 0, var2); ++var2) {
      }

      return var0.substring(0, var2 - 1);
   }

   public static String methodString(String var0, Class[] var1) {
      StringBuffer var2 = new StringBuffer(var0 + "(");
      if (var1.length > 0) {
         var2.append(" ");
      }

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Class var4 = var1[var3];
         var2.append((var4 == null ? "null" : var4.getName()) + (var3 < var1.length - 1 ? ", " : " "));
      }

      var2.append(")");
      return var2.toString();
   }

   public static String normalizeClassName(Class var0) {
      return Reflect.normalizeClassName(var0);
   }
}
