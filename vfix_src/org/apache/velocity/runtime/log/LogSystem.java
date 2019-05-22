package org.apache.velocity.runtime.log;

import org.apache.velocity.runtime.RuntimeServices;

/** @deprecated */
public interface LogSystem {
   /** @deprecated */
   boolean DEBUG_ON = true;
   int DEBUG_ID = 0;
   int INFO_ID = 1;
   int WARN_ID = 2;
   int ERROR_ID = 3;

   void init(RuntimeServices var1) throws Exception;

   /** @deprecated */
   void logVelocityMessage(int var1, String var2);
}
