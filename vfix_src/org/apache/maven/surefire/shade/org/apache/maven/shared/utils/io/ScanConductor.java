package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io;

import java.io.File;

public interface ScanConductor {
   ScanConductor.ScanAction visitDirectory(String var1, File var2);

   ScanConductor.ScanAction visitFile(String var1, File var2);

   public static enum ScanAction {
      ABORT,
      CONTINUE,
      NO_RECURSE,
      ABORT_DIRECTORY;
   }
}
