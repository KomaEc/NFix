package org.apache.tools.ant.types.selectors;

public interface SelectorScanner {
   void setSelectors(FileSelector[] var1);

   String[] getDeselectedDirectories();

   String[] getDeselectedFiles();
}
