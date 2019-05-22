package soot.jbco.util;

import java.util.Random;

public class Rand {
   private static final Random r = new Random(1L);

   public static int getInt(int n) {
      return r.nextInt(n);
   }

   public static int getInt() {
      return r.nextInt();
   }

   public static float getFloat() {
      return r.nextFloat();
   }

   public static long getLong() {
      return r.nextLong();
   }

   public static double getDouble() {
      return r.nextDouble();
   }
}
