package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.testapi.AbstractTestUnit;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Events;
import com.gzoltar.shaded.org.pitest.util.ClassLoaderDetectionStrategy;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.testng.ITestListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestNGTestUnit extends AbstractTestUnit {
   private static final TestNG TESTNG = new TestNG(false);
   private final ClassLoaderDetectionStrategy classloaderDetection;
   private final Class<?> clazz;
   private final TestGroupConfig config;

   public TestNGTestUnit(ClassLoaderDetectionStrategy classloaderDetection, Class<?> clazz, TestGroupConfig config) {
      super(new Description("_", clazz));
      this.clazz = clazz;
      this.classloaderDetection = classloaderDetection;
      this.config = config;
   }

   public TestNGTestUnit(Class<?> clazz, TestGroupConfig config) {
      this(IsolationUtils.loaderDetectionStrategy(), clazz, config);
   }

   public void execute(ClassLoader loader, ResultCollector rc) {
      synchronized(TESTNG) {
         if (this.classloaderDetection.fromDifferentLoader(this.clazz, loader)) {
            this.executeInForeignLoader(rc, loader);
         } else {
            this.executeInCurrentLoader(rc);
         }

      }
   }

   private void executeInForeignLoader(ResultCollector rc, ClassLoader loader) {
      Callable e = (Callable)IsolationUtils.cloneForLoader(new ForeignClassLoaderTestNGExecutor(this.createSuite()), loader);

      try {
         List<String> q = (List)e.call();
         Events.applyEvents(q, rc, this.getDescription());
      } catch (Exception var5) {
         throw Unchecked.translateCheckedException(var5);
      }
   }

   private void executeInCurrentLoader(ResultCollector rc) {
      ITestListener listener = new TestNGAdapter(this.clazz, this.getDescription(), rc);
      XmlSuite suite = this.createSuite();
      TESTNG.setDefaultSuiteName(suite.getName());
      TESTNG.setXmlSuites(Collections.singletonList(suite));
      TESTNG.addListener((ITestListener)listener);

      try {
         TESTNG.run();
      } finally {
         TESTNG.getTestListeners().remove(listener);
      }

   }

   private XmlSuite createSuite() {
      XmlSuite suite = new XmlSuite();
      suite.setName(this.clazz.getName());
      XmlTest test = new XmlTest(suite);
      test.setName(this.clazz.getName());
      XmlClass xclass = new XmlClass(this.clazz.getName());
      test.setXmlClasses(Collections.singletonList(xclass));
      if (!this.config.getExcludedGroups().isEmpty()) {
         suite.setExcludedGroups(this.config.getExcludedGroups());
      }

      if (!this.config.getIncludedGroups().isEmpty()) {
         suite.setIncludedGroups(this.config.getIncludedGroups());
      }

      return suite;
   }
}
