package com.mks.connect;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class BlimpException extends IOException {
   private Throwable cause;

   public BlimpException(String msg) {
      super(msg);
   }

   public BlimpException(Throwable cause) {
      this.cause = cause;
   }

   public void printStackTrace() {
      if (this.cause != null) {
         this.cause.printStackTrace();
      } else {
         super.printStackTrace();
      }

   }

   public void printStackTrace(PrintStream s) {
      if (this.cause != null) {
         this.cause.printStackTrace(s);
      } else {
         super.printStackTrace(s);
      }

   }

   public void printStackTrace(PrintWriter s) {
      if (this.cause != null) {
         this.cause.printStackTrace(s);
      } else {
         super.printStackTrace(s);
      }

   }
}
