package com.mks.api.util;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

abstract class StackTrace {
   public static String getStackTrace() {
      CharArrayWriter out = new CharArrayWriter();
      printStackTrace(new PrintWriter(out));
      return out.toString();
   }

   public static void printStackTrace(PrintWriter out) {
      try {
         throw new Exception();
      } catch (Exception var2) {
         var2.printStackTrace(out);
      }
   }

   public static void printStackTrace(PrintStream out) {
      try {
         throw new Exception();
      } catch (Exception var2) {
         var2.printStackTrace(out);
      }
   }

   public static String getCaller(int level) {
      String s = getStackTrace();
      char[] dst = new char[s.length()];
      s.getChars(0, s.length(), dst, 0);
      BufferedReader r = new BufferedReader(new CharArrayReader(dst));
      String result = "<unknown>";

      try {
         r.readLine();
         r.readLine();
         r.readLine();
         r.readLine();
         r.readLine();

         do {
            result = r.readLine();
         } while(level-- > 0);

         int i = result.indexOf("at ");
         if (i > 0) {
            result = result.substring(i + 3);
         }

         i = result.lastIndexOf("(");
         if (i > 0) {
            result = result.substring(0, i);
         }
      } catch (Exception var14) {
      } finally {
         try {
            r.close();
         } catch (Exception var13) {
         }

      }

      return result;
   }

   private static ThreadGroup getMasterThreadGroup() {
      ThreadGroup g = Thread.currentThread().getThreadGroup();

      for(ThreadGroup parent = g.getParent(); parent != null; parent = parent.getParent()) {
         g = parent;
      }

      return g;
   }

   public static void printThreads() {
      printThreads((ThreadGroup)null, 0);
   }

   private static void printThreads(ThreadGroup master, int level) {
      if (master == null) {
         master = getMasterThreadGroup();
      }

      System.out.println(indent(level) + "Group: " + master.getName());
      ThreadGroup[] gl = new ThreadGroup[master.activeGroupCount() + 100];
      int n = master.enumerate(gl, false);

      for(int i = 0; i < n; ++i) {
         printThreads(gl[i], level + 1);
      }

      Thread[] tl = new Thread[master.activeCount() + 100];
      n = master.enumerate(tl, false);

      for(int i = 0; i < n; ++i) {
         printThread(tl[i], level + 1);
      }

   }

   private static String indent(int level) {
      String s;
      for(s = ""; level-- > 0; s = s + ">") {
      }

      return s;
   }

   private static void printThread(Thread t, int level) {
      System.out.println(indent(level) + "Thread: " + t.getName());
   }
}
