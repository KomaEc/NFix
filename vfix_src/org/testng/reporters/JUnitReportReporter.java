package org.testng.reporters;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

public class JUnitReportReporter implements IReporter {
   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String defaultOutputDirectory) {
      Map<Class<?>, Set<ITestResult>> results = Maps.newHashMap();
      Map<Class<?>, Set<ITestResult>> failedConfigurations = Maps.newHashMap();
      ListMultiMap<Object, ITestResult> befores = Maps.newListMultiMap();
      ListMultiMap<Object, ITestResult> afters = Maps.newListMultiMap();
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         Map<String, ISuiteResult> suiteResults = suite.getResults();
         Iterator i$ = suiteResults.values().iterator();

         while(i$.hasNext()) {
            ISuiteResult sr = (ISuiteResult)i$.next();
            ITestContext tc = sr.getTestContext();
            this.addResults(tc.getPassedTests().getAllResults(), results);
            this.addResults(tc.getFailedTests().getAllResults(), results);
            this.addResults(tc.getSkippedTests().getAllResults(), results);
            this.addResults(tc.getFailedConfigurations().getAllResults(), failedConfigurations);
            Iterator i$ = tc.getPassedConfigurations().getAllResults().iterator();

            while(i$.hasNext()) {
               ITestResult tr = (ITestResult)i$.next();
               if (tr.getMethod().isBeforeMethodConfiguration()) {
                  befores.put(tr.getInstance(), tr);
               }

               if (tr.getMethod().isAfterMethodConfiguration()) {
                  afters.put(tr.getInstance(), tr);
               }
            }
         }
      }

      i$ = results.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Class<?>, Set<ITestResult>> entry = (Entry)i$.next();
         Class<?> cls = (Class)entry.getKey();
         Properties p1 = new Properties();
         p1.setProperty("name", cls.getName());
         Date timeStamp = Calendar.getInstance().getTime();
         p1.setProperty("timestamp", timeStamp.toGMTString());
         List<JUnitReportReporter.TestTag> testCases = Lists.newArrayList();
         int failures = 0;
         int errors = 0;
         int testCount = 0;
         float totalTime = 0.0F;
         Iterator i$ = ((Set)entry.getValue()).iterator();

         JUnitReportReporter.TestTag testTag;
         while(i$.hasNext()) {
            ITestResult tr = (ITestResult)i$.next();
            testTag = new JUnitReportReporter.TestTag();
            boolean isSuccess = tr.getStatus() == 1;
            if (!isSuccess) {
               if (tr.getThrowable() instanceof AssertionError) {
                  ++failures;
               } else {
                  ++errors;
               }
            }

            Properties p2 = new Properties();
            p2.setProperty("classname", cls.getName());
            p2.setProperty("name", this.getTestName(tr));
            long time = tr.getEndMillis() - tr.getStartMillis();
            time += this.getNextConfiguration(befores, tr);
            time += this.getNextConfiguration(afters, tr);
            p2.setProperty("time", "" + this.formatTime((float)time));
            Throwable t = this.getThrowable(tr, failedConfigurations);
            if (!isSuccess && t != null) {
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               t.printStackTrace(pw);
               testTag.message = t.getMessage();
               testTag.type = t.getClass().getName();
               testTag.stackTrace = sw.toString();
               testTag.errorTag = tr.getThrowable() instanceof AssertionError ? "failure" : "error";
            }

            totalTime += (float)time;
            ++testCount;
            testTag.properties = p2;
            testCases.add(testTag);
         }

         p1.setProperty("failures", "" + failures);
         p1.setProperty("errors", "" + errors);
         p1.setProperty("name", cls.getName());
         p1.setProperty("tests", "" + testCount);
         p1.setProperty("time", "" + this.formatTime(totalTime));

         try {
            p1.setProperty("hostname", InetAddress.getLocalHost().getHostName());
         } catch (UnknownHostException var28) {
         }

         XMLStringBuffer xsb = new XMLStringBuffer();
         xsb.addComment("Generated by " + this.getClass().getName());
         xsb.push("testsuite", p1);
         Iterator i$ = testCases.iterator();

         while(i$.hasNext()) {
            testTag = (JUnitReportReporter.TestTag)i$.next();
            if (testTag.stackTrace == null) {
               xsb.addEmptyElement("testcase", testTag.properties);
            } else {
               xsb.push("testcase", testTag.properties);
               Properties p = new Properties();
               if (testTag.message != null) {
                  p.setProperty("message", testTag.message);
               }

               p.setProperty("type", testTag.type);
               xsb.push(testTag.errorTag, p);
               xsb.addCDATA(testTag.stackTrace);
               xsb.pop(testTag.errorTag);
               xsb.pop("testcase");
            }
         }

         xsb.pop("testsuite");
         String outputDirectory = defaultOutputDirectory + File.separator + "junitreports";
         Utils.writeUtf8File(outputDirectory, this.getFileName(cls), xsb.toXML());
      }

   }

   private long getNextConfiguration(ListMultiMap<Object, ITestResult> configurations, ITestResult tr) {
      long result = 0L;
      List<ITestResult> confResults = (List)configurations.get(tr.getInstance());
      Map<ITestNGMethod, ITestResult> seen = Maps.newHashMap();
      if (confResults != null) {
         Iterator i$ = confResults.iterator();

         while(i$.hasNext()) {
            ITestResult r = (ITestResult)i$.next();
            if (!seen.containsKey(r.getMethod())) {
               result += r.getEndMillis() - r.getStartMillis();
               seen.put(r.getMethod(), r);
            }
         }

         confResults.removeAll(seen.values());
      }

      return result;
   }

   protected String getFileName(Class cls) {
      return "TEST-" + cls.getName() + ".xml";
   }

   protected String getTestName(ITestResult tr) {
      return tr.getMethod().getMethodName();
   }

   private String formatTime(float time) {
      DecimalFormatSymbols symbols = new DecimalFormatSymbols();
      symbols.setDecimalSeparator('.');
      DecimalFormat format = new DecimalFormat("#.###", symbols);
      format.setMinimumFractionDigits(3);
      return format.format((double)(time / 1000.0F));
   }

   private Throwable getThrowable(ITestResult tr, Map<Class<?>, Set<ITestResult>> failedConfigurations) {
      Throwable result = tr.getThrowable();
      if (result == null && tr.getStatus() == 3) {
         Iterator i$ = failedConfigurations.values().iterator();

         while(i$.hasNext()) {
            Set<ITestResult> failures = (Set)i$.next();
            Iterator i$ = failures.iterator();

            while(i$.hasNext()) {
               ITestResult failure = (ITestResult)i$.next();
               if (failure.getThrowable() != null) {
                  return failure.getThrowable();
               }
            }
         }
      }

      return result;
   }

   private void addResults(Set<ITestResult> allResults, Map<Class<?>, Set<ITestResult>> out) {
      ITestResult tr;
      Set l;
      for(Iterator i$ = allResults.iterator(); i$.hasNext(); l.add(tr)) {
         tr = (ITestResult)i$.next();
         Class<?> cls = tr.getMethod().getTestClass().getRealClass();
         l = (Set)out.get(cls);
         if (l == null) {
            l = Sets.newHashSet();
            out.put(cls, l);
         }
      }

   }

   class TestTag {
      public Properties properties;
      public String message;
      public String type;
      public String stackTrace;
      public String errorTag;
   }
}
