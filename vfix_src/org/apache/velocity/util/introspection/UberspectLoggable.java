package org.apache.velocity.util.introspection;

import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;

public interface UberspectLoggable {
   void setLog(Log var1);

   /** @deprecated */
   void setRuntimeLogger(RuntimeLogger var1);
}
