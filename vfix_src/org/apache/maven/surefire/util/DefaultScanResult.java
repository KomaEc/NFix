package org.apache.maven.surefire.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DefaultScanResult implements ScanResult {
   private final List<String> files;
   private static final String scanResultNo = "tc.";

   public DefaultScanResult(List<String> files) {
      this.files = Collections.unmodifiableList(files);
   }

   public int size() {
      return this.files.size();
   }

   public String getClassName(int index) {
      return (String)this.files.get(index);
   }

   public void writeTo(Properties properties) {
      int size = this.files.size();

      for(int i = 0; i < size; ++i) {
         properties.setProperty("tc." + i, (String)this.files.get(i));
      }

   }

   public static DefaultScanResult from(Properties properties) {
      List<String> result = new ArrayList();
      int var2 = 0;

      while(true) {
         String item = properties.getProperty("tc." + var2++);
         if (item == null) {
            return new DefaultScanResult(result);
         }

         result.add(item);
      }
   }

   public boolean isEmpty() {
      return this.files.isEmpty();
   }

   public List getFiles() {
      return this.files;
   }

   public TestsToRun applyFilter(ScannerFilter scannerFilter, ClassLoader testClassLoader) {
      List<Class> result = new ArrayList();
      int size = this.size();

      for(int i = 0; i < size; ++i) {
         String className = this.getClassName(i);
         Class testClass = loadClass(testClassLoader, className);
         if (scannerFilter == null || scannerFilter.accept(testClass)) {
            result.add(testClass);
         }
      }

      return new TestsToRun(result);
   }

   public List getClassesSkippedByValidation(ScannerFilter scannerFilter, ClassLoader testClassLoader) {
      List<Class> result = new ArrayList();
      int size = this.size();

      for(int i = 0; i < size; ++i) {
         String className = this.getClassName(i);
         Class testClass = loadClass(testClassLoader, className);
         if (scannerFilter != null && !scannerFilter.accept(testClass)) {
            result.add(testClass);
         }
      }

      return result;
   }

   private static Class loadClass(ClassLoader classLoader, String className) {
      try {
         Class testClass = classLoader.loadClass(className);
         return testClass;
      } catch (ClassNotFoundException var4) {
         throw new RuntimeException("Unable to create test class '" + className + "'", var4);
      }
   }

   public DefaultScanResult append(DefaultScanResult other) {
      if (other != null) {
         List<String> src = new ArrayList(this.files);
         src.addAll(other.files);
         return new DefaultScanResult(src);
      } else {
         return this;
      }
   }
}
