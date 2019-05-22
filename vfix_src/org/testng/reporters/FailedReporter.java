package org.testng.reporters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;
import org.testng.internal.MethodHelper;
import org.testng.internal.Utils;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class FailedReporter extends TestListenerAdapter implements IReporter {
   public static final String TESTNG_FAILED_XML = "testng-failed.xml";
   private XmlSuite m_xmlSuite;

   public FailedReporter() {
   }

   public FailedReporter(XmlSuite xmlSuite) {
      this.m_xmlSuite = xmlSuite;
   }

   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         this.generateFailureSuite(suite.getXmlSuite(), suite, outputDirectory);
      }

   }

   protected void generateFailureSuite(XmlSuite xmlSuite, ISuite suite, String outputDir) {
      XmlSuite failedSuite = (XmlSuite)xmlSuite.clone();
      failedSuite.setName("Failed suite [" + xmlSuite.getName() + "]");
      this.m_xmlSuite = failedSuite;
      Map<String, XmlTest> xmlTests = Maps.newHashMap();
      Iterator i$ = xmlSuite.getTests().iterator();

      while(i$.hasNext()) {
         XmlTest xmlT = (XmlTest)i$.next();
         xmlTests.put(xmlT.getName(), xmlT);
      }

      Map<String, ISuiteResult> results = suite.getResults();
      Iterator i$ = results.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ISuiteResult> entry = (Entry)i$.next();
         ISuiteResult suiteResult = (ISuiteResult)entry.getValue();
         ITestContext testContext = suiteResult.getTestContext();
         this.generateXmlTest(suite, (XmlTest)xmlTests.get(testContext.getName()), testContext, testContext.getFailedTests().getAllResults(), testContext.getSkippedTests().getAllResults());
      }

      if (null != failedSuite.getTests() && failedSuite.getTests().size() > 0) {
         Utils.writeUtf8File(outputDir, "testng-failed.xml", failedSuite.toXml());
         Utils.writeUtf8File(suite.getOutputDirectory(), "testng-failed.xml", failedSuite.toXml());
      }

   }

   /** @deprecated */
   @Deprecated
   public void onFinish(ITestContext context) {
   }

   private void generateXmlTest(ISuite suite, XmlTest xmlTest, ITestContext context, Collection<ITestResult> failedTests, Collection<ITestResult> skippedTests) {
      if (skippedTests.size() > 0 || failedTests.size() > 0) {
         Set<ITestNGMethod> methodsToReRun = Sets.newHashSet();
         Collection[] allTests = new Collection[]{failedTests, skippedTests};
         Collection[] arr$ = allTests;
         int len$ = allTests.length;

         int len$;
         label69:
         for(len$ = 0; len$ < len$; ++len$) {
            Collection<ITestResult> tests = arr$[len$];
            Iterator i$ = tests.iterator();

            while(true) {
               ITestNGMethod method;
               do {
                  ITestResult failedTest;
                  ITestNGMethod current;
                  do {
                     if (!i$.hasNext()) {
                        continue label69;
                     }

                     failedTest = (ITestResult)i$.next();
                     current = failedTest.getMethod();
                  } while(!current.isTest());

                  methodsToReRun.add(current);
                  method = failedTest.getMethod();
               } while(!method.isTest());

               List<ITestNGMethod> methodsDependedUpon = MethodHelper.getMethodsDependedUpon(method, context.getAllTestMethods());
               Iterator i$ = methodsDependedUpon.iterator();

               while(i$.hasNext()) {
                  ITestNGMethod m = (ITestNGMethod)i$.next();
                  if (m.isTest()) {
                     methodsToReRun.add(m);
                  }
               }
            }
         }

         List<ITestNGMethod> result = Lists.newArrayList();
         ITestNGMethod[] arr$ = context.getAllTestMethods();
         len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod m = arr$[i$];
            if (methodsToReRun.contains(m)) {
               result.add(m);
            }
         }

         methodsToReRun.clear();
         Collection<ITestNGMethod> invoked = suite.getInvokedMethods();
         Iterator i$ = invoked.iterator();

         while(i$.hasNext()) {
            ITestNGMethod tm = (ITestNGMethod)i$.next();
            if (!tm.isTest()) {
               methodsToReRun.add(tm);
            }
         }

         result.addAll(methodsToReRun);
         this.createXmlTest(context, result, xmlTest);
      }

   }

   private void createXmlTest(ITestContext context, List<ITestNGMethod> methods, XmlTest srcXmlTest) {
      XmlTest xmlTest = new XmlTest(this.m_xmlSuite);
      xmlTest.setName(context.getName() + "(failed)");
      xmlTest.setBeanShellExpression(srcXmlTest.getExpression());
      xmlTest.setIncludedGroups(srcXmlTest.getIncludedGroups());
      xmlTest.setExcludedGroups(srcXmlTest.getExcludedGroups());
      xmlTest.setParallel(srcXmlTest.getParallel());
      xmlTest.setParameters(srcXmlTest.getLocalParameters());
      xmlTest.setJUnit(srcXmlTest.isJUnit());
      List<XmlClass> xmlClasses = this.createXmlClasses(methods, srcXmlTest);
      xmlTest.setXmlClasses(xmlClasses);
   }

   private List<XmlClass> createXmlClasses(List<ITestNGMethod> methods, XmlTest srcXmlTest) {
      List<XmlClass> result = Lists.newArrayList();
      Map<Class, Set<ITestNGMethod>> methodsMap = Maps.newHashMap();

      ITestNGMethod m;
      Object methodList;
      for(Iterator i$ = methods.iterator(); i$.hasNext(); ((Set)methodList).add(m)) {
         m = (ITestNGMethod)i$.next();
         Object[] instances = m.getInstances();
         Class clazz = instances != null && instances.length != 0 && instances[0] != null ? instances[0].getClass() : m.getRealClass();
         methodList = (Set)methodsMap.get(clazz);
         if (null == methodList) {
            methodList = new HashSet();
            methodsMap.put(clazz, methodList);
         }
      }

      Map<String, String> parameters = Maps.newHashMap();
      Iterator i$ = srcXmlTest.getClasses().iterator();

      while(i$.hasNext()) {
         XmlClass c = (XmlClass)i$.next();
         parameters.putAll(c.getLocalParameters());
      }

      int index = 0;
      Iterator i$ = methodsMap.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Class, Set<ITestNGMethod>> entry = (Entry)i$.next();
         Class clazz = (Class)entry.getKey();
         Set<ITestNGMethod> methodList = (Set)entry.getValue();
         XmlClass xmlClass = new XmlClass(clazz.getName(), index++, false);
         List<XmlInclude> methodNames = Lists.newArrayList(methodList.size());
         int ind = 0;
         Iterator i$ = methodList.iterator();

         while(i$.hasNext()) {
            ITestNGMethod m = (ITestNGMethod)i$.next();
            methodNames.add(new XmlInclude(m.getMethod().getName(), m.getFailedInvocationNumbers(), ind++));
         }

         xmlClass.setIncludedMethods(methodNames);
         xmlClass.setParameters(parameters);
         result.add(xmlClass);
      }

      return result;
   }

   private String getFileName(ITestContext context) {
      return "testng-failed.xml";
   }

   private static void ppp(String s) {
      System.out.println("[FailedReporter] " + s);
   }
}
