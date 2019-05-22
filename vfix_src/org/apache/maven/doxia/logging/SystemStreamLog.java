package org.apache.maven.doxia.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SystemStreamLog implements Log {
   private static final String EOL = System.getProperty("line.separator");
   private int currentLevel = 1;

   public void setLogLevel(int level) {
      if (level <= 0) {
         this.currentLevel = 0;
      } else if (level <= 1) {
         this.currentLevel = 1;
      } else if (level <= 2) {
         this.currentLevel = 2;
      } else if (level <= 3) {
         this.currentLevel = 3;
      } else {
         this.currentLevel = 5;
      }

   }

   public void debug(CharSequence content) {
      if (this.isDebugEnabled()) {
         this.print("debug", content);
      }

   }

   public void debug(CharSequence content, Throwable error) {
      if (this.isDebugEnabled()) {
         this.print("debug", content, error);
      }

   }

   public void debug(Throwable error) {
      if (this.isDebugEnabled()) {
         this.print("debug", error);
      }

   }

   public void info(CharSequence content) {
      if (this.isInfoEnabled()) {
         this.print("info", content);
      }

   }

   public void info(CharSequence content, Throwable error) {
      if (this.isInfoEnabled()) {
         this.print("info", content, error);
      }

   }

   public void info(Throwable error) {
      if (this.isInfoEnabled()) {
         this.print("info", error);
      }

   }

   public void warn(CharSequence content) {
      if (this.isWarnEnabled()) {
         this.print("warn", content);
      }

   }

   public void warn(CharSequence content, Throwable error) {
      if (this.isWarnEnabled()) {
         this.print("warn", content, error);
      }

   }

   public void warn(Throwable error) {
      if (this.isWarnEnabled()) {
         this.print("warn", error);
      }

   }

   public void error(CharSequence content) {
      if (this.isErrorEnabled()) {
         System.err.println("[error] " + content.toString());
      }

   }

   public void error(CharSequence content, Throwable error) {
      if (this.isErrorEnabled()) {
         StringWriter sWriter = new StringWriter();
         PrintWriter pWriter = new PrintWriter(sWriter);
         error.printStackTrace(pWriter);
         System.err.println("[error] " + content.toString() + EOL + EOL + sWriter.toString());
      }

   }

   public void error(Throwable error) {
      if (this.isErrorEnabled()) {
         StringWriter sWriter = new StringWriter();
         PrintWriter pWriter = new PrintWriter(sWriter);
         error.printStackTrace(pWriter);
         System.err.println("[error] " + sWriter.toString());
      }

   }

   public boolean isDebugEnabled() {
      return this.currentLevel <= 0;
   }

   public boolean isInfoEnabled() {
      return this.currentLevel <= 1;
   }

   public boolean isWarnEnabled() {
      return this.currentLevel <= 2;
   }

   public boolean isErrorEnabled() {
      return this.currentLevel <= 3;
   }

   private void print(String prefix, CharSequence content) {
      System.out.println("[" + prefix + "] " + content.toString());
   }

   private void print(String prefix, Throwable error) {
      StringWriter sWriter = new StringWriter();
      PrintWriter pWriter = new PrintWriter(sWriter);
      error.printStackTrace(pWriter);
      System.out.println("[" + prefix + "] " + sWriter.toString());
   }

   private void print(String prefix, CharSequence content, Throwable error) {
      StringWriter sWriter = new StringWriter();
      PrintWriter pWriter = new PrintWriter(sWriter);
      error.printStackTrace(pWriter);
      System.out.println("[" + prefix + "] " + content.toString() + EOL + EOL + sWriter.toString());
   }
}
