package org.jboss.util.platform;

public final class Java {
   public static final int VERSION_1_0 = 1;
   public static final int VERSION_1_1 = 2;
   public static final int VERSION_1_2 = 3;
   public static final int VERSION_1_3 = 4;
   public static final int VERSION_1_4 = 5;
   public static final int VERSION_1_5 = 6;
   public static final int VERSION_1_6 = 7;
   private static final int VERSION;

   private Java() {
   }

   public static int getVersion() {
      return VERSION;
   }

   public static boolean isVersion(int version) {
      return VERSION == version;
   }

   public static boolean isCompatible(int version) {
      return VERSION >= version;
   }

   static {
      byte version = 1;

      try {
         Class.forName("java.lang.Void");
         int version = true;
         Class.forName("java.lang.ThreadLocal");
         version = true;
         Class.forName("java.lang.StrictMath");
         version = true;
         Class.forName("java.lang.StackTraceElement");
         version = true;
         Class.forName("java.lang.Enum");
         version = true;
         Class.forName("java.lang.management.LockInfo");
         version = 7;
      } catch (ClassNotFoundException var2) {
      }

      VERSION = version;
   }
}
