package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;

public interface Algorithm {
   boolean isValid();

   String getValue(File var1);
}
