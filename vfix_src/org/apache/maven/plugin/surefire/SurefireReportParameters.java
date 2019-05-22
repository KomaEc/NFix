package org.apache.maven.plugin.surefire;

import java.io.File;

public interface SurefireReportParameters {
   boolean isSkipTests();

   void setSkipTests(boolean var1);

   boolean isSkipExec();

   void setSkipExec(boolean var1);

   boolean isSkip();

   void setSkip(boolean var1);

   boolean isTestFailureIgnore();

   void setTestFailureIgnore(boolean var1);

   File getBasedir();

   void setBasedir(File var1);

   File getTestClassesDirectory();

   void setTestClassesDirectory(File var1);

   File getReportsDirectory();

   void setReportsDirectory(File var1);

   Boolean getFailIfNoTests();

   void setFailIfNoTests(Boolean var1);
}
