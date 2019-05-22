package org.apache.velocity.runtime.log;

import org.apache.velocity.runtime.RuntimeServices;

public interface LogChute {
   String TRACE_PREFIX = " [trace] ";
   String DEBUG_PREFIX = " [debug] ";
   String INFO_PREFIX = "  [info] ";
   String WARN_PREFIX = "  [warn] ";
   String ERROR_PREFIX = " [error] ";
   int TRACE_ID = -1;
   int DEBUG_ID = 0;
   int INFO_ID = 1;
   int WARN_ID = 2;
   int ERROR_ID = 3;

   void init(RuntimeServices var1) throws Exception;

   void log(int var1, String var2);

   void log(int var1, String var2, Throwable var3);

   boolean isLevelEnabled(int var1);
}
