package org.codehaus.plexus.logging;

public interface Logger {
   int LEVEL_DEBUG = 0;
   int LEVEL_INFO = 1;
   int LEVEL_WARN = 2;
   int LEVEL_ERROR = 3;
   int LEVEL_FATAL = 4;
   int LEVEL_DISABLED = 5;

   void debug(String var1);

   void debug(String var1, Throwable var2);

   boolean isDebugEnabled();

   void info(String var1);

   void info(String var1, Throwable var2);

   boolean isInfoEnabled();

   void warn(String var1);

   void warn(String var1, Throwable var2);

   boolean isWarnEnabled();

   void error(String var1);

   void error(String var1, Throwable var2);

   boolean isErrorEnabled();

   void fatalError(String var1);

   void fatalError(String var1, Throwable var2);

   boolean isFatalErrorEnabled();

   Logger getChildLogger(String var1);

   int getThreshold();

   String getName();
}
