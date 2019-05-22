package org.codehaus.groovy.util;

import java.util.Arrays;

public class HashCodeHelper {
   private static final int SEED = 127;
   private static final int MULT = 31;

   public static int initHash() {
      return 127;
   }

   public static int updateHash(int current, boolean var) {
      return shift(current) + (var ? 1 : 0);
   }

   public static int updateHash(int current, char var) {
      return shift(current) + var;
   }

   public static int updateHash(int current, int var) {
      return shift(current) + var;
   }

   public static int updateHash(int current, long var) {
      return shift(current) + (int)(var ^ var >>> 32);
   }

   public static int updateHash(int current, float var) {
      return updateHash(current, Float.floatToIntBits(var));
   }

   public static int updateHash(int current, double var) {
      return updateHash(current, Double.doubleToLongBits(var));
   }

   public static int updateHash(int current, Object var) {
      if (var == null) {
         return updateHash(current, (int)0);
      } else {
         return var.getClass().isArray() ? shift(current) + Arrays.hashCode((Object[])((Object[])var)) : updateHash(current, var.hashCode());
      }
   }

   public static int updateHash(int current, boolean[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, char[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, byte[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, short[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, int[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, long[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, float[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   public static int updateHash(int current, double[] var) {
      return var == null ? updateHash(current, (int)0) : shift(current) + Arrays.hashCode(var);
   }

   private static int shift(int current) {
      return 31 * current;
   }
}
