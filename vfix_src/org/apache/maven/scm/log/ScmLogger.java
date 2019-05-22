package org.apache.maven.scm.log;

public interface ScmLogger {
   boolean isDebugEnabled();

   void debug(String var1);

   void debug(String var1, Throwable var2);

   void debug(Throwable var1);

   boolean isInfoEnabled();

   void info(String var1);

   void info(String var1, Throwable var2);

   void info(Throwable var1);

   boolean isWarnEnabled();

   void warn(String var1);

   void warn(String var1, Throwable var2);

   void warn(Throwable var1);

   boolean isErrorEnabled();

   void error(String var1);

   void error(String var1, Throwable var2);

   void error(Throwable var1);
}
