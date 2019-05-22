package org.jboss.util;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NestedRuntimeException extends RuntimeException implements NestedThrowable {
   private static final long serialVersionUID = 9017248167673450852L;
   protected final Throwable nested;

   public NestedRuntimeException(String msg) {
      super(msg);
      this.nested = null;
   }

   public NestedRuntimeException(String msg, Throwable nested) {
      super(msg);
      this.nested = nested;
      NestedThrowable.Util.checkNested(this, nested);
   }

   public NestedRuntimeException(Throwable nested) {
      this(nested.getMessage(), nested);
   }

   public NestedRuntimeException() {
      this.nested = null;
   }

   public Throwable getNested() {
      return this.nested;
   }

   public Throwable getCause() {
      return this.nested;
   }

   public String getMessage() {
      return NestedThrowable.Util.getMessage(super.getMessage(), this.nested);
   }

   public void printStackTrace(PrintStream stream) {
      if (this.nested == null || NestedThrowable.PARENT_TRACE_ENABLED) {
         super.printStackTrace(stream);
      }

      NestedThrowable.Util.print(this.nested, stream);
   }

   public void printStackTrace(PrintWriter writer) {
      if (this.nested == null || NestedThrowable.PARENT_TRACE_ENABLED) {
         super.printStackTrace(writer);
      }

      NestedThrowable.Util.print(this.nested, writer);
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }
}
