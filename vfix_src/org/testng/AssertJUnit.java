package org.testng;

import org.testng.internal.junit.ArrayAsserts;

public class AssertJUnit extends ArrayAsserts {
   protected AssertJUnit() {
   }

   public static void assertTrue(String message, boolean condition) {
      if (!condition) {
         fail(message);
      }

   }

   public static void assertTrue(boolean condition) {
      assertTrue((String)null, condition);
   }

   public static void assertFalse(String message, boolean condition) {
      assertTrue(message, !condition);
   }

   public static void assertFalse(boolean condition) {
      assertFalse((String)null, condition);
   }

   public static void fail(String message) {
      if (null == message) {
         message = "";
      }

      throw new AssertionError(message);
   }

   public static void fail() {
      fail((String)null);
   }

   public static void assertEquals(String message, Object expected, Object actual) {
      if (expected != null || actual != null) {
         if (expected == null || !expected.equals(actual)) {
            failNotEquals(message, expected, actual);
         }
      }
   }

   public static void assertEquals(Object expected, Object actual) {
      assertEquals((String)null, (Object)expected, (Object)actual);
   }

   public static void assertEquals(String message, String expected, String actual) {
      if (expected != null || actual != null) {
         if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(format(message, expected, actual));
         }
      }
   }

   public static void assertEquals(String expected, String actual) {
      assertEquals((String)null, (String)expected, (String)actual);
   }

   public static void assertEquals(String message, double expected, double actual, double delta) {
      if (Double.isInfinite(expected)) {
         if (expected != actual) {
            failNotEquals(message, expected, actual);
         }
      } else if (Math.abs(expected - actual) > delta) {
         failNotEquals(message, expected, actual);
      }

   }

   public static void assertEquals(double expected, double actual, double delta) {
      assertEquals((String)null, expected, actual, delta);
   }

   public static void assertEquals(String message, float expected, float actual, float delta) {
      if (Float.isInfinite(expected)) {
         if (expected != actual) {
            failNotEquals(message, expected, actual);
         }
      } else if (Math.abs(expected - actual) > delta) {
         failNotEquals(message, expected, actual);
      }

   }

   public static void assertEquals(float expected, float actual, float delta) {
      assertEquals((String)null, expected, actual, delta);
   }

   public static void assertEquals(String message, long expected, long actual) {
      assertEquals(message, (Object)expected, (Object)actual);
   }

   public static void assertEquals(long expected, long actual) {
      assertEquals((String)null, expected, actual);
   }

   public static void assertEquals(String message, boolean expected, boolean actual) {
      assertEquals(message, (Object)expected, (Object)actual);
   }

   public static void assertEquals(boolean expected, boolean actual) {
      assertEquals((String)null, expected, actual);
   }

   public static void assertEquals(String message, byte expected, byte actual) {
      assertEquals(message, (Object)expected, (Object)actual);
   }

   public static void assertEquals(byte expected, byte actual) {
      assertEquals((String)null, (byte)expected, (byte)actual);
   }

   public static void assertEquals(String message, char expected, char actual) {
      assertEquals(message, (Object)expected, (Object)actual);
   }

   public static void assertEquals(char expected, char actual) {
      assertEquals((String)null, (char)expected, (char)actual);
   }

   public static void assertEquals(String message, short expected, short actual) {
      assertEquals(message, (Object)expected, (Object)actual);
   }

   public static void assertEquals(short expected, short actual) {
      assertEquals((String)null, (short)expected, (short)actual);
   }

   public static void assertEquals(String message, int expected, int actual) {
      assertEquals(message, (Object)expected, (Object)actual);
   }

   public static void assertEquals(int expected, int actual) {
      assertEquals((String)null, (int)expected, (int)actual);
   }

   public static void assertNotNull(Object object) {
      assertNotNull((String)null, object);
   }

   public static void assertNotNull(String message, Object object) {
      assertTrue(message, object != null);
   }

   public static void assertNull(Object object) {
      assertNull((String)null, object);
   }

   public static void assertNull(String message, Object object) {
      assertTrue(message, object == null);
   }

   public static void assertSame(String message, Object expected, Object actual) {
      if (expected != actual) {
         failNotSame(message, expected, actual);
      }
   }

   public static void assertSame(Object expected, Object actual) {
      assertSame((String)null, expected, actual);
   }

   public static void assertNotSame(String message, Object expected, Object actual) {
      if (expected == actual) {
         failSame(message);
      }

   }

   public static void assertNotSame(Object expected, Object actual) {
      assertNotSame((String)null, expected, actual);
   }

   public static void assertEquals(byte[] expected, byte[] actual) {
      assertEquals("", expected, actual);
   }

   public static void assertEquals(String message, byte[] expected, byte[] actual) {
      if (expected != actual) {
         if (null == expected) {
            fail("expected a null array, but not null found. " + message);
         }

         if (null == actual) {
            fail("expected not null array, but null found. " + message);
         }

         assertEquals("arrays don't have the same size. " + message, expected.length, actual.length);

         for(int i = 0; i < expected.length; ++i) {
            if (expected[i] != actual[i]) {
               fail("arrays differ firstly at element [" + i + "]; " + format(message, expected[i], actual[i]));
            }
         }

      }
   }

   private static void failSame(String message) {
      String formatted = "";
      if (message != null) {
         formatted = message + " ";
      }

      fail(formatted + "expected not same");
   }

   private static void failNotSame(String message, Object expected, Object actual) {
      String formatted = "";
      if (message != null) {
         formatted = message + " ";
      }

      fail(formatted + "expected same:<" + expected + "> was not:<" + actual + ">");
   }

   private static void failNotEquals(String message, Object expected, Object actual) {
      fail(format(message, expected, actual));
   }

   static String format(String message, Object expected, Object actual) {
      String formatted = "";
      if (message != null) {
         formatted = message + " ";
      }

      return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
   }
}
