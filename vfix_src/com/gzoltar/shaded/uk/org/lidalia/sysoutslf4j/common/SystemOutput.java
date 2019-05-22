package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common;

import java.io.PrintStream;

public enum SystemOutput {
   OUT("System.out") {
      public PrintStream get() {
         return System.out;
      }

      public void set(PrintStream newPrintStream) {
         System.setOut(newPrintStream);
      }
   },
   ERR("System.err") {
      public PrintStream get() {
         return System.err;
      }

      public void set(PrintStream newPrintStream) {
         System.setErr(newPrintStream);
      }
   };

   private final String friendlyName;

   public abstract PrintStream get();

   public abstract void set(PrintStream var1);

   private SystemOutput(String name) {
      this.friendlyName = name;
   }

   public String toString() {
      return this.friendlyName;
   }

   // $FF: synthetic method
   SystemOutput(String var3, SystemOutput var4) {
      this(var3);
   }
}
