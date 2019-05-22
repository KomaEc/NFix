package org.apache.maven.surefire.testset;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestRequest {
   private final List<File> suiteXmlFiles;
   private final File testSourceDirectory;
   private final String requestedTest;
   private final String requestedTestMethod;

   public TestRequest(List suiteXmlFiles, File testSourceDirectory, String requestedTest) {
      this(suiteXmlFiles, testSourceDirectory, requestedTest, (String)null);
   }

   public TestRequest(List suiteXmlFiles, File testSourceDirectory, String requestedTest, String requestedTestMethod) {
      this.suiteXmlFiles = createFiles(suiteXmlFiles);
      this.testSourceDirectory = testSourceDirectory;
      this.requestedTest = requestedTest;
      this.requestedTestMethod = requestedTestMethod;
   }

   public List<File> getSuiteXmlFiles() {
      return this.suiteXmlFiles;
   }

   public File getTestSourceDirectory() {
      return this.testSourceDirectory;
   }

   public String getRequestedTest() {
      return this.requestedTest;
   }

   public String getRequestedTestMethod() {
      return this.requestedTestMethod;
   }

   private static List<File> createFiles(List suiteXmlFiles) {
      if (suiteXmlFiles != null) {
         List<File> files = new ArrayList();
         Iterator i$ = suiteXmlFiles.iterator();

         while(i$.hasNext()) {
            Object suiteXmlFile = i$.next();
            files.add(suiteXmlFile instanceof String ? new File((String)suiteXmlFile) : (File)suiteXmlFile);
         }

         return files;
      } else {
         return null;
      }
   }
}
