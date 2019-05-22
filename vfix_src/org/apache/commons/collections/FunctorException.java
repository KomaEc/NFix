package org.apache.commons.collections;

import java.io.PrintStream;
import java.io.PrintWriter;

public class FunctorException extends RuntimeException {
   private static final boolean JDK_SUPPORTS_NESTED;
   private final Throwable rootCause;
   // $FF: synthetic field
   static Class class$java$lang$Throwable;

   public FunctorException() {
      this.rootCause = null;
   }

   public FunctorException(String msg) {
      super(msg);
      this.rootCause = null;
   }

   public FunctorException(Throwable rootCause) {
      super(rootCause == null ? null : rootCause.getMessage());
      this.rootCause = rootCause;
   }

   public FunctorException(String msg, Throwable rootCause) {
      super(msg);
      this.rootCause = rootCause;
   }

   public Throwable getCause() {
      return this.rootCause;
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream out) {
      synchronized(out) {
         PrintWriter pw = new PrintWriter(out, false);
         this.printStackTrace(pw);
         pw.flush();
      }
   }

   public void printStackTrace(PrintWriter out) {
      synchronized(out) {
         super.printStackTrace(out);
         if (this.rootCause != null && !JDK_SUPPORTS_NESTED) {
            out.print("Caused by: ");
            this.rootCause.printStackTrace(out);
         }

      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      boolean flag = false;

      try {
         (class$java$lang$Throwable == null ? (class$java$lang$Throwable = class$("java.lang.Throwable")) : class$java$lang$Throwable).getDeclaredMethod("getCause");
         flag = true;
      } catch (NoSuchMethodException var2) {
         flag = false;
      }

      JDK_SUPPORTS_NESTED = flag;
   }
}
