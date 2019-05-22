package org.apache.maven.doxia.logging;

public interface Log {
   int LEVEL_DEBUG = 0;
   int LEVEL_INFO = 1;
   int LEVEL_WARN = 2;
   int LEVEL_ERROR = 3;
   int LEVEL_FATAL = 4;
   int LEVEL_DISABLED = 5;

   void setLogLevel(int var1);

   boolean isDebugEnabled();

   void debug(CharSequence var1);

   void debug(CharSequence var1, Throwable var2);

   void debug(Throwable var1);

   boolean isInfoEnabled();

   void info(CharSequence var1);

   void info(CharSequence var1, Throwable var2);

   void info(Throwable var1);

   boolean isWarnEnabled();

   void warn(CharSequence var1);

   void warn(CharSequence var1, Throwable var2);

   void warn(Throwable var1);

   boolean isErrorEnabled();

   void error(CharSequence var1);

   void error(CharSequence var1, Throwable var2);

   void error(Throwable var1);
}
