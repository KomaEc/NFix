package org.apache.tools.ant;

import java.io.File;

public interface FileScanner {
   void addDefaultExcludes();

   File getBasedir();

   String[] getExcludedDirectories();

   String[] getExcludedFiles();

   String[] getIncludedDirectories();

   String[] getIncludedFiles();

   String[] getNotIncludedDirectories();

   String[] getNotIncludedFiles();

   void scan() throws IllegalStateException;

   void setBasedir(String var1);

   void setBasedir(File var1);

   void setExcludes(String[] var1);

   void setIncludes(String[] var1);

   void setCaseSensitive(boolean var1);
}
