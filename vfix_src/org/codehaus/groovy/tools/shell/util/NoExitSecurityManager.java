package org.codehaus.groovy.tools.shell.util;

import java.security.Permission;

public class NoExitSecurityManager extends SecurityManager {
   private final SecurityManager parent;

   public NoExitSecurityManager(SecurityManager parent) {
      assert parent != null;

      this.parent = parent;
   }

   public NoExitSecurityManager() {
      this(System.getSecurityManager());
   }

   public void checkPermission(Permission perm) {
      if (this.parent != null) {
         this.parent.checkPermission(perm);
      }

   }

   public void checkExit(int code) {
      throw new SecurityException("Use of System.exit() is forbidden!");
   }
}
