package org.apache.commons.logging;

import java.io.IOException;
import java.security.PrivilegedAction;

class LogFactory$4 implements PrivilegedAction {
   // $FF: synthetic field
   private final ClassLoader val$loader;
   // $FF: synthetic field
   private final String val$name;

   LogFactory$4(ClassLoader var1, String var2) {
      this.val$loader = var1;
      this.val$name = var2;
   }

   public Object run() {
      try {
         return this.val$loader != null ? this.val$loader.getResources(this.val$name) : ClassLoader.getSystemResources(this.val$name);
      } catch (IOException var2) {
         if (LogFactory.isDiagnosticsEnabled()) {
            LogFactory.access$000("Exception while trying to find configuration file " + this.val$name + ":" + var2.getMessage());
         }

         return null;
      } catch (NoSuchMethodError var3) {
         return null;
      }
   }
}
