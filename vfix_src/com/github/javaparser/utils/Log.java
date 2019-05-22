package com.github.javaparser.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {
   private static Log.Adapter CURRENT_ADAPTER = new Log.SilentAdapter();

   public static void setAdapter(Log.Adapter adapter) {
      CURRENT_ADAPTER = adapter;
   }

   public static void trace(String format, Object... args) {
      CURRENT_ADAPTER.trace(CodeGenerationUtils.f(format, args));
   }

   public static void info(String format, Object... args) {
      CURRENT_ADAPTER.info(CodeGenerationUtils.f(format, args));
   }

   public static void error(Throwable throwable) {
      CURRENT_ADAPTER.error(throwable, (String)null);
   }

   public static void error(Throwable throwable, String format, Object... args) {
      CURRENT_ADAPTER.error(throwable, CodeGenerationUtils.f(format, args));
   }

   public static void error(String format, Object... args) {
      CURRENT_ADAPTER.error((Throwable)null, CodeGenerationUtils.f(format, args));
   }

   public interface Adapter {
      void info(String message);

      void trace(String message);

      void error(Throwable throwable, String f);
   }

   public static class SilentAdapter implements Log.Adapter {
      public void info(String message) {
      }

      public void trace(String message) {
      }

      public void error(Throwable throwable, String f) {
      }
   }

   public static class StandardOutStandardErrorAdapter implements Log.Adapter {
      public void info(String message) {
         System.out.println(message);
      }

      public void trace(String message) {
         System.out.println(message);
      }

      public void error(Throwable throwable, String message) {
         if (message == null) {
            System.err.println(throwable.getMessage());
            this.printStackTrace(throwable);
         } else if (throwable == null) {
            System.err.println(message);
         } else {
            System.err.println(message + ":" + throwable.getMessage());
            this.printStackTrace(throwable);
         }

      }

      private void printStackTrace(Throwable throwable) {
         try {
            StringWriter sw = new StringWriter();
            Throwable var3 = null;

            try {
               PrintWriter pw = new PrintWriter(sw);
               Throwable var5 = null;

               try {
                  throwable.printStackTrace(pw);
                  this.trace(sw.toString());
               } catch (Throwable var30) {
                  var5 = var30;
                  throw var30;
               } finally {
                  if (pw != null) {
                     if (var5 != null) {
                        try {
                           pw.close();
                        } catch (Throwable var29) {
                           var5.addSuppressed(var29);
                        }
                     } else {
                        pw.close();
                     }
                  }

               }
            } catch (Throwable var32) {
               var3 = var32;
               throw var32;
            } finally {
               if (sw != null) {
                  if (var3 != null) {
                     try {
                        sw.close();
                     } catch (Throwable var28) {
                        var3.addSuppressed(var28);
                     }
                  } else {
                     sw.close();
                  }
               }

            }

         } catch (IOException var34) {
            throw new AssertionError("Error in logging library");
         }
      }
   }
}
