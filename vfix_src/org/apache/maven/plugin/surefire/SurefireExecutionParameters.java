package org.apache.maven.plugin.surefire;

import java.io.File;
import java.util.List;
import org.apache.maven.artifact.repository.ArtifactRepository;

public interface SurefireExecutionParameters {
   boolean isSkipTests();

   void setSkipTests(boolean var1);

   boolean isSkipExec();

   void setSkipExec(boolean var1);

   boolean isSkip();

   void setSkip(boolean var1);

   File getBasedir();

   void setBasedir(File var1);

   File getTestClassesDirectory();

   void setTestClassesDirectory(File var1);

   File getClassesDirectory();

   void setClassesDirectory(File var1);

   File getReportsDirectory();

   void setReportsDirectory(File var1);

   File getTestSourceDirectory();

   void setTestSourceDirectory(File var1);

   String getTest();

   String getTestMethod();

   void setTest(String var1);

   List<String> getIncludes();

   void setIncludes(List<String> var1);

   List<String> getExcludes();

   void setExcludes(List<String> var1);

   ArtifactRepository getLocalRepository();

   void setLocalRepository(ArtifactRepository var1);

   boolean isPrintSummary();

   void setPrintSummary(boolean var1);

   String getReportFormat();

   void setReportFormat(String var1);

   boolean isUseFile();

   void setUseFile(boolean var1);

   String getDebugForkedProcess();

   void setDebugForkedProcess(String var1);

   int getForkedProcessTimeoutInSeconds();

   void setForkedProcessTimeoutInSeconds(int var1);

   double getParallelTestsTimeoutInSeconds();

   void setParallelTestsTimeoutInSeconds(double var1);

   double getParallelTestsTimeoutForcedInSeconds();

   void setParallelTestsTimeoutForcedInSeconds(double var1);

   boolean isUseSystemClassLoader();

   void setUseSystemClassLoader(boolean var1);

   boolean isUseManifestOnlyJar();

   void setUseManifestOnlyJar(boolean var1);

   Boolean getFailIfNoSpecifiedTests();

   void setFailIfNoSpecifiedTests(Boolean var1);
}
