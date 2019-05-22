package org.testng;

import java.io.File;
import java.io.IOException;

public class FileAssert {
   private FileAssert() {
   }

   public static void assertDirectory(File tstvalue, String message) {
      boolean condition = false;

      try {
         condition = tstvalue.isDirectory();
      } catch (SecurityException var4) {
         failSecurity(var4, tstvalue, fileType(tstvalue), "Directory", message);
      }

      if (!condition) {
         failFile(tstvalue, fileType(tstvalue), "Directory", message);
      }

   }

   public static void assertDirectory(File tstvalue) {
      assertDirectory(tstvalue, (String)null);
   }

   public static void assertFile(File tstvalue, String message) {
      boolean condition = false;

      try {
         condition = tstvalue.isFile();
      } catch (SecurityException var4) {
         failSecurity(var4, tstvalue, fileType(tstvalue), "File", message);
      }

      if (!condition) {
         failFile(tstvalue, fileType(tstvalue), "File", message);
      }

   }

   public static void assertFile(File tstvalue) {
      assertFile(tstvalue, (String)null);
   }

   public static void assertLength(File tstvalue, long expected, String message) {
      long actual = -1L;

      try {
         actual = tstvalue.isDirectory() ? (long)tstvalue.list().length : tstvalue.length();
      } catch (SecurityException var7) {
         failSecurity(var7, tstvalue, String.valueOf(actual), String.valueOf(expected), message);
      }

      if (actual != expected) {
         failFile(tstvalue, String.valueOf(actual), String.valueOf(expected), message);
      }

   }

   public static void assertLength(File tstvalue, long expected) {
      assertLength(tstvalue, expected, (String)null);
   }

   public static void assertMinLength(File tstvalue, long expected, String message) {
      long actual = -1L;

      try {
         actual = tstvalue.isDirectory() ? (long)tstvalue.list().length : tstvalue.length();
      } catch (SecurityException var7) {
         failSecurity(var7, tstvalue, String.valueOf(actual), "at least " + String.valueOf(expected), message);
      }

      if (actual < expected) {
         failFile(tstvalue, String.valueOf(actual), "at least " + String.valueOf(expected), message);
      }

   }

   public static void assertMinLength(File tstvalue, long expected) {
      assertMinLength(tstvalue, expected, (String)null);
   }

   public static void assertMaxLength(File tstvalue, long expected, String message) {
      long actual = -1L;

      try {
         actual = tstvalue.isDirectory() ? (long)tstvalue.list().length : tstvalue.length();
      } catch (SecurityException var7) {
         failSecurity(var7, tstvalue, String.valueOf(actual), "at most " + String.valueOf(expected), message);
      }

      if (actual > expected) {
         failFile(tstvalue, String.valueOf(actual), "at most " + String.valueOf(expected), message);
      }

   }

   public static void assertMaxLength(File tstvalue, long expected) {
      assertMaxLength(tstvalue, expected, (String)null);
   }

   public static void assertReadable(File tstvalue, String message) {
      boolean condition = false;

      try {
         condition = tstvalue.canRead();
      } catch (SecurityException var4) {
         failSecurity(var4, tstvalue, fileAccess(tstvalue), "Read Access", message);
      }

      if (!condition) {
         failFile(tstvalue, fileAccess(tstvalue), "Read Access", message);
      }

   }

   public static void assertReadable(File tstvalue) {
      assertReadable(tstvalue, (String)null);
   }

   public static void assertWriteable(File tstvalue, String message) {
      boolean condition = false;

      try {
         condition = tstvalue.canWrite();
      } catch (SecurityException var4) {
         failSecurity(var4, tstvalue, fileAccess(tstvalue), "Write Access", message);
      }

      if (!condition) {
         failFile(tstvalue, fileAccess(tstvalue), "Write Access", message);
      }

   }

   public static void assertWriteable(File tstvalue) {
      assertReadable(tstvalue, (String)null);
   }

   public static void assertReadWrite(File tstvalue, String message) {
      boolean condition = false;

      try {
         condition = tstvalue.canRead() && tstvalue.canWrite();
      } catch (SecurityException var4) {
         failSecurity(var4, tstvalue, fileAccess(tstvalue), "Read/Write Access", message);
      }

      if (!condition) {
         failFile(tstvalue, fileAccess(tstvalue), "Read/Write Access", message);
      }

   }

   public static void assertReadWrite(File tstvalue) {
      assertReadWrite(tstvalue, (String)null);
   }

   public static void fail(String message, Throwable realCause) {
      AssertionError ae = new AssertionError(message);
      ae.initCause(realCause);
      throw ae;
   }

   public static void fail(String message) {
      throw new AssertionError(message);
   }

   public static void fail() {
      fail((String)null);
   }

   private static void failFile(File path, String actual, String expected, String message) {
      String formatted = "";
      if (message != null) {
         formatted = message + " ";
      }

      fail(formatted + "expected <" + expected + "> but was <" + toString(path) + ">" + (expected != null ? "<" + expected + ">" : ""));
   }

   private static void failSecurity(Exception e, File path, String actual, String expected, String message) {
      String formatted = "";
      if (message != null) {
         formatted = message + " ";
      }

      fail(formatted + "expected <" + expected + "> but was <" + toString(path) + ">" + "<" + (e != null && e.getMessage() != null && e.getMessage().length() > 0 ? e.getMessage() : "not authorized by JVM") + ">");
   }

   private static String fileType(File path) {
      try {
         if (!path.exists()) {
            return "Non existant";
         } else if (path.isDirectory()) {
            return "Directory";
         } else {
            return path.isFile() ? "File" : "Special File";
         }
      } catch (SecurityException var2) {
         return "Unauthorized";
      }
   }

   private static String fileAccess(File path) {
      try {
         if (!path.exists()) {
            return "Non existant";
         } else if (path.canWrite() && path.canRead()) {
            return "Read/Write Access";
         } else if (path.canRead()) {
            return "Read only Access";
         } else {
            return path.canWrite() ? "Write only Access" : "No Access";
         }
      } catch (SecurityException var2) {
         return "Unauthorized";
      }
   }

   private static String toString(File path) {
      try {
         return path.getCanonicalPath();
      } catch (IOException var2) {
         return path.getAbsolutePath();
      }
   }
}
