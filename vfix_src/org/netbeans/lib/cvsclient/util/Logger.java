package org.netbeans.lib.cvsclient.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Logger {
   private static OutputStream outLogStream;
   private static OutputStream inLogStream;
   private static final String LOG_PROPERTY = "cvsClientLog";
   private static boolean logging;

   public static void setLogging(String var0) {
      logging = var0 != null;

      try {
         if (logging) {
            if (var0.equals("system")) {
               outLogStream = System.err;
               inLogStream = System.err;
            } else {
               outLogStream = new BufferedOutputStream(new FileOutputStream(var0 + ".out"));
               inLogStream = new BufferedOutputStream(new FileOutputStream(var0 + ".in"));
            }
         }
      } catch (IOException var5) {
         System.err.println("Unable to create log files: " + var5);
         System.err.println("Logging DISABLED");
         logging = false;

         try {
            if (outLogStream != null) {
               outLogStream.close();
            }
         } catch (IOException var4) {
         }

         try {
            if (inLogStream != null) {
               inLogStream.close();
            }
         } catch (IOException var3) {
         }
      }

   }

   public static void logInput(byte[] var0) {
      logInput(var0, 0, var0.length);
   }

   public static void logInput(byte[] var0, int var1, int var2) {
      if (logging) {
         try {
            inLogStream.write(var0, var1, var2);
            inLogStream.flush();
         } catch (IOException var4) {
            System.err.println("Could not write to log file: " + var4);
            System.err.println("Logging DISABLED.");
            logging = false;
         }

      }
   }

   public static void logInput(char var0) {
      if (logging) {
         try {
            inLogStream.write(var0);
            inLogStream.flush();
         } catch (IOException var2) {
            System.err.println("Could not write to log file: " + var2);
            System.err.println("Logging DISABLED.");
            logging = false;
         }

      }
   }

   public static void logOutput(byte[] var0) {
      if (logging) {
         try {
            outLogStream.write(var0);
            outLogStream.flush();
         } catch (IOException var2) {
            System.err.println("Could not write to log file: " + var2);
            System.err.println("Logging DISABLED.");
            logging = false;
         }

      }
   }

   static {
      setLogging(System.getProperty("cvsClientLog"));
   }
}
