package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.BuildException;

public interface FileSelector {
   boolean isSelected(File var1, String var2, File var3) throws BuildException;
}
