package org.jboss.logging;

public interface LoggerPlugin {
   void init(String var1);

   boolean isTraceEnabled();

   void trace(Object var1);

   void trace(Object var1, Throwable var2);

   void trace(String var1, Object var2, Throwable var3);

   /** @deprecated */
   boolean isDebugEnabled();

   void debug(Object var1);

   void debug(Object var1, Throwable var2);

   void debug(String var1, Object var2, Throwable var3);

   /** @deprecated */
   boolean isInfoEnabled();

   void info(Object var1);

   void info(Object var1, Throwable var2);

   void info(String var1, Object var2, Throwable var3);

   void warn(Object var1);

   void warn(Object var1, Throwable var2);

   void warn(String var1, Object var2, Throwable var3);

   void error(Object var1);

   void error(Object var1, Throwable var2);

   void error(String var1, Object var2, Throwable var3);

   void fatal(Object var1);

   void fatal(Object var1, Throwable var2);

   void fatal(String var1, Object var2, Throwable var3);
}
