package org.apache.maven.surefire.testset;

import java.io.File;
import java.util.List;
import org.apache.maven.surefire.util.RunOrder;

public class DirectoryScannerParameters {
   private final File testClassesDirectory;
   private final List includes;
   private final List excludes;
   private final List specificTests;
   private final Boolean failIfNoTests;
   private final RunOrder[] runOrder;

   private DirectoryScannerParameters(File testClassesDirectory, List includes, List excludes, List specificTests, Boolean failIfNoTests, RunOrder[] runOrder) {
      this.testClassesDirectory = testClassesDirectory;
      this.includes = includes;
      this.excludes = excludes;
      this.specificTests = specificTests;
      this.failIfNoTests = failIfNoTests;
      this.runOrder = runOrder;
   }

   public DirectoryScannerParameters(File testClassesDirectory, List includes, List excludes, List specificTests, Boolean failIfNoTests, String runOrder) {
      this(testClassesDirectory, includes, excludes, specificTests, failIfNoTests, runOrder == null ? RunOrder.DEFAULT : RunOrder.valueOfMulti(runOrder));
   }

   public List getSpecificTests() {
      return this.specificTests;
   }

   public File getTestClassesDirectory() {
      return this.testClassesDirectory;
   }

   public List getIncludes() {
      return this.includes;
   }

   public List getExcludes() {
      return this.excludes;
   }

   public Boolean isFailIfNoTests() {
      return this.failIfNoTests;
   }

   public RunOrder[] getRunOrder() {
      return this.runOrder;
   }
}
