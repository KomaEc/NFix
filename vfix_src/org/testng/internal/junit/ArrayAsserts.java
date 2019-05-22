package org.testng.internal.junit;

public class ArrayAsserts {
   public static void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) throws ArrayComparisonFailure {
      internalArrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(Object[] expecteds, Object[] actuals) {
      assertArrayEquals((String)null, (Object[])expecteds, (Object[])actuals);
   }

   public static void assertArrayEquals(String message, byte[] expecteds, byte[] actuals) throws ArrayComparisonFailure {
      internalArrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(byte[] expecteds, byte[] actuals) {
      assertArrayEquals((String)null, (byte[])expecteds, (byte[])actuals);
   }

   public static void assertArrayEquals(String message, char[] expecteds, char[] actuals) throws ArrayComparisonFailure {
      internalArrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(char[] expecteds, char[] actuals) {
      assertArrayEquals((String)null, (char[])expecteds, (char[])actuals);
   }

   public static void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure {
      internalArrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(short[] expecteds, short[] actuals) {
      assertArrayEquals((String)null, (short[])expecteds, (short[])actuals);
   }

   public static void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure {
      internalArrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(int[] expecteds, int[] actuals) {
      assertArrayEquals((String)null, (int[])expecteds, (int[])actuals);
   }

   public static void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure {
      internalArrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(long[] expecteds, long[] actuals) {
      assertArrayEquals((String)null, (long[])expecteds, (long[])actuals);
   }

   public static void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure {
      (new InexactComparisonCriteria(delta)).arrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(double[] expecteds, double[] actuals, double delta) {
      assertArrayEquals((String)null, expecteds, actuals, delta);
   }

   public static void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure {
      (new InexactComparisonCriteria((double)delta)).arrayEquals(message, expecteds, actuals);
   }

   public static void assertArrayEquals(float[] expecteds, float[] actuals, float delta) {
      assertArrayEquals((String)null, expecteds, actuals, delta);
   }

   private static void internalArrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
      (new ExactComparisonCriteria()).arrayEquals(message, expecteds, actuals);
   }
}
