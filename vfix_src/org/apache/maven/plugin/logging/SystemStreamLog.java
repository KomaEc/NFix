package org.apache.maven.plugin.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SystemStreamLog implements Log {
   public void debug(CharSequence content) {
      this.print("debug", content);
   }

   public void debug(CharSequence content, Throwable error) {
      this.print("debug", content, error);
   }

   public void debug(Throwable error) {
      this.print("debug", error);
   }

   public void info(CharSequence content) {
      this.print("info", content);
   }

   public void info(CharSequence content, Throwable error) {
      this.print("info", content, error);
   }

   public void info(Throwable error) {
      this.print("info", error);
   }

   public void warn(CharSequence content) {
      this.print("warn", content);
   }

   public void warn(CharSequence content, Throwable error) {
      this.print("warn", content, error);
   }

   public void warn(Throwable error) {
      this.print("warn", error);
   }

   public void error(CharSequence content) {
      System.err.println("[error] " + content.toString());
   }

   public void error(CharSequence content, Throwable error) {
      StringWriter sWriter = new StringWriter();
      PrintWriter pWriter = new PrintWriter(sWriter);
      error.printStackTrace(pWriter);
      System.err.println("[error] " + content.toString() + "\n\n" + sWriter.toString());
   }

   public void error(Throwable error) {
      StringWriter sWriter = new StringWriter();
      PrintWriter pWriter = new PrintWriter(sWriter);
      error.printStackTrace(pWriter);
      System.err.println("[error] " + sWriter.toString());
   }

   public boolean isDebugEnabled() {
      return false;
   }

   public boolean isInfoEnabled() {
      return true;
   }

   public boolean isWarnEnabled() {
      return true;
   }

   public boolean isErrorEnabled() {
      return true;
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
      System.out.println("[" + prefix + "] " + content.toString() + "\n\n" + sWriter.toString());
   }
}
