package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.security.AccessControlException;

public class ShutdownHookUtils {
   public static void addShutDownHook(Thread hook) {
      try {
         Runtime.getRuntime().addShutdownHook(hook);
      } catch (IllegalStateException var2) {
      } catch (AccessControlException var3) {
      }

   }

   public static void removeShutdownHook(Thread hook) {
      try {
         Runtime.getRuntime().removeShutdownHook(hook);
      } catch (IllegalStateException var2) {
      } catch (AccessControlException var3) {
      }

   }
}
