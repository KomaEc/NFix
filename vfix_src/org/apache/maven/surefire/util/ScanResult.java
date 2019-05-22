package org.apache.maven.surefire.util;

import java.util.List;
import java.util.Properties;

public interface ScanResult {
   int size();

   String getClassName(int var1);

   TestsToRun applyFilter(ScannerFilter var1, ClassLoader var2);

   List getClassesSkippedByValidation(ScannerFilter var1, ClassLoader var2);

   void writeTo(Properties var1);
}
