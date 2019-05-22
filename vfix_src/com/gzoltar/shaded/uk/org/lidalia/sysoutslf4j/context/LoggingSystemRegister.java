package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingSystemRegister {
   private static final Logger LOG = LoggerFactory.getLogger(SysOutOverSLF4J.class);
   private final Set<String> loggingSystemNameFragments = new CopyOnWriteArraySet();

   void registerLoggingSystem(String packageName) {
      this.loggingSystemNameFragments.add(packageName);
      LOG.info((String)"Package {} registered; all classes within it or subpackages of it will be allowed to print to System.out and System.err", (Object)packageName);
   }

   void unregisterLoggingSystem(String packageName) {
      if (this.loggingSystemNameFragments.remove(packageName)) {
         LOG.info((String)"Package {} unregistered; all classes within it or subpackages of it will have System.out and System.err redirected to SLF4J", (Object)packageName);
      }

   }

   boolean isInLoggingSystem(String className) {
      boolean isInLoggingSystem = false;
      Iterator var3 = this.loggingSystemNameFragments.iterator();

      while(var3.hasNext()) {
         String packageName = (String)var3.next();
         if (className.startsWith(packageName)) {
            isInLoggingSystem = true;
            break;
         }
      }

      return isInLoggingSystem;
   }
}
