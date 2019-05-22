package org.netbeans.lib.cvsclient.util;

import java.util.ResourceBundle;

public class BundleUtilities {
   public static String getPackageName(Class var0) {
      String var1 = var0.getName();
      int var2 = var1.lastIndexOf(46);
      return var2 < 0 ? "" : var1.substring(0, var2);
   }

   public static String getResourceName(Class var0, String var1) {
      String var2 = getPackageName(var0);
      String var3 = var2.replace('.', '/') + '/' + var1;
      return var3;
   }

   public static ResourceBundle getResourceBundle(Class var0, String var1) {
      String var2 = getResourceName(var0, var1);
      return ResourceBundle.getBundle(var2);
   }
}
