package org.apache.commons.digester.plugins;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.NoOpLog;

class LogUtils {
   static Log getLogger(Digester digester) {
      return (Log)(digester == null ? new NoOpLog() : digester.getLogger());
   }
}
