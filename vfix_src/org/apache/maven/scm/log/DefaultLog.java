package org.apache.maven.scm.log;

public class DefaultLog implements ScmLogger {
   private boolean debug = false;

   public DefaultLog() {
   }

   public DefaultLog(boolean debug) {
      this.debug = debug;
   }

   public boolean isDebugEnabled() {
      return this.debug;
   }

   public void debug(String content) {
      if (this.debug) {
         System.out.println(content);
      }

   }

   public void debug(String content, Throwable error) {
      if (this.debug) {
         System.out.println(content);
         error.printStackTrace();
      }

   }

   public void debug(Throwable error) {
      if (this.debug) {
         error.printStackTrace();
      }

   }

   public boolean isInfoEnabled() {
      return true;
   }

   public void info(String content) {
      System.out.println(content);
   }

   public void info(String content, Throwable error) {
      System.out.println(content);
      error.printStackTrace();
   }

   public void info(Throwable error) {
      error.printStackTrace();
   }

   public boolean isWarnEnabled() {
      return true;
   }

   public void warn(String content) {
      System.out.println(content);
   }

   public void warn(String content, Throwable error) {
      System.out.println(content);
      error.printStackTrace();
   }

   public void warn(Throwable error) {
      error.printStackTrace();
   }

   public boolean isErrorEnabled() {
      return true;
   }

   public void error(String content) {
      System.out.print("[ERROR] " + content);
   }

   public void error(String content, Throwable error) {
      System.out.println("[ERROR] " + content);
      error.printStackTrace();
   }

   public void error(Throwable error) {
      error.printStackTrace();
   }
}
