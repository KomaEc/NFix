package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.io.File;

public interface Arg {
   void setValue(String var1);

   void setLine(String var1);

   void setFile(File var1);
}
