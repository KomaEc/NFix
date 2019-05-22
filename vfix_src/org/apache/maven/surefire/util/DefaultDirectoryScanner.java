package org.apache.maven.surefire.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.surefire.SpecificTestClassFilter;

public class DefaultDirectoryScanner implements DirectoryScanner {
   private static final String FS = System.getProperty("file.separator");
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final String JAVA_SOURCE_FILE_EXTENSION = ".java";
   private static final String JAVA_CLASS_FILE_EXTENSION = ".class";
   private final File basedir;
   private final List includes;
   private final List excludes;
   private final List specificTests;
   private final List<Class> classesSkippedByValidation = new ArrayList();

   public DefaultDirectoryScanner(File basedir, List includes, List excludes, List specificTests) {
      this.basedir = basedir;
      this.includes = includes;
      this.excludes = excludes;
      this.specificTests = specificTests;
   }

   public TestsToRun locateTestClasses(ClassLoader classLoader, ScannerFilter scannerFilter) {
      String[] testClassNames = this.collectTests();
      List<Class> result = new ArrayList();
      String[] specific = this.specificTests == null ? new String[0] : processIncludesExcludes(this.specificTests);
      SpecificTestClassFilter specificTestFilter = new SpecificTestClassFilter(specific);
      String[] arr$ = testClassNames;
      int len$ = testClassNames.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String className = arr$[i$];
         Class testClass = loadClass(classLoader, className);
         if (specificTestFilter.accept(testClass)) {
            if (scannerFilter != null && !scannerFilter.accept(testClass)) {
               this.classesSkippedByValidation.add(testClass);
            } else {
               result.add(testClass);
            }
         }
      }

      return new TestsToRun(result);
   }

   private static Class loadClass(ClassLoader classLoader, String className) {
      try {
         Class testClass = classLoader.loadClass(className);
         return testClass;
      } catch (ClassNotFoundException var4) {
         throw new RuntimeException("Unable to create test class '" + className + "'", var4);
      }
   }

   String[] collectTests() {
      String[] tests = EMPTY_STRING_ARRAY;
      if (this.basedir.exists()) {
         org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.DirectoryScanner scanner = new org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.DirectoryScanner();
         scanner.setBasedir(this.basedir);
         if (this.includes != null) {
            scanner.setIncludes(processIncludesExcludes(this.includes));
         }

         if (this.excludes != null) {
            scanner.setExcludes(processIncludesExcludes(this.excludes));
         }

         scanner.scan();
         tests = scanner.getIncludedFiles();

         for(int i = 0; i < tests.length; ++i) {
            String test = tests[i];
            test = test.substring(0, test.indexOf("."));
            tests[i] = test.replace(FS.charAt(0), '.');
         }
      }

      return tests;
   }

   private static String[] processIncludesExcludes(List list) {
      List<String> newList = new ArrayList();
      Iterator i$ = list.iterator();

      String inc;
      while(i$.hasNext()) {
         Object aList = i$.next();
         inc = (String)aList;
         String[] includes = inc.split(",");
         Collections.addAll(newList, includes);
      }

      String[] incs = new String[newList.size()];

      for(int i = 0; i < incs.length; ++i) {
         inc = (String)newList.get(i);
         if (inc.endsWith(".java")) {
            inc = inc.substring(0, inc.lastIndexOf(".java")) + ".class";
         }

         incs[i] = inc;
      }

      return incs;
   }
}
