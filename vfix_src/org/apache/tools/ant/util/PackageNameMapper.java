package org.apache.tools.ant.util;

import java.io.File;

public class PackageNameMapper extends GlobPatternMapper {
   protected String extractVariablePart(String name) {
      String var = name.substring(this.prefixLength, name.length() - this.postfixLength);
      return var.replace(File.separatorChar, '.');
   }
}
