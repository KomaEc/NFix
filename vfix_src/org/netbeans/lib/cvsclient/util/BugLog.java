package org.netbeans.lib.cvsclient.util;

public class BugLog {
   private static BugLog instance;

   public static synchronized BugLog getInstance() {
      if (instance == null) {
         instance = new BugLog();
      }

      return instance;
   }

   public static synchronized void setInstance(BugLog var0) {
      instance = var0;
   }

   public void showException(Exception var1) {
      var1.printStackTrace();
   }

   public void assertTrue(boolean var1, String var2) {
      if (!var1) {
         throw new BugLog.BugException(var2);
      }
   }

   public void assertNotNull(Object var1) {
      if (var1 == null) {
         throw new BugLog.BugException("Value must not be null!");
      }
   }

   public void bug(String var1) {
      (new Exception(var1)).printStackTrace();
   }

   public static class BugException extends RuntimeException {
      public BugException(String var1) {
         super(var1);
      }
   }
}
