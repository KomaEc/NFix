package org.apache.maven.surefire.util;

public interface DirectoryScanner {
   TestsToRun locateTestClasses(ClassLoader var1, ScannerFilter var2);
}
