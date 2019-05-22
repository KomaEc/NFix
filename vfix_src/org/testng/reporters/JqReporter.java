package org.testng.reporters;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Maps;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

public class JqReporter implements IReporter {
   private static final String C = "class";
   private static final String D = "div";
   private static final String S = "span";
   private int m_testCount = 0;
   private String m_outputDirectory;
   private Map<String, String> m_testMap = Maps.newHashMap();

   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
      this.m_outputDirectory = "/Users/cedric/java/misc/jquery";
      XMLStringBuffer xsb = new XMLStringBuffer("  ");
      xsb.push("div", "id", "suites");
      this.generateSuites(xmlSuites, suites, xsb);
      xsb.pop("div");

      try {
         String all = Files.readFile(new File("/Users/cedric/java/misc/jquery/head"));
         Utils.writeFile(this.m_outputDirectory, "index2.html", all + xsb.toXML());
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   private XMLStringBuffer generateSuites(List<XmlSuite> xmlSuites, List<ISuite> suites, XMLStringBuffer main) {
      Iterator i$ = suites.iterator();

      while(true) {
         ISuite suite;
         do {
            if (!i$.hasNext()) {
               return main;
            }

            suite = (ISuite)i$.next();
         } while(suite.getResults().size() == 0);

         XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
         XMLStringBuffer header = new XMLStringBuffer(main.getCurrentIndent());
         xsb.push("div", "class", "suite-content");
         Map<String, ISuiteResult> results = suite.getResults();
         XMLStringBuffer xs1 = new XMLStringBuffer(xsb.getCurrentIndent());
         XMLStringBuffer xs2 = new XMLStringBuffer(xsb.getCurrentIndent());
         XMLStringBuffer xs3 = new XMLStringBuffer(xsb.getCurrentIndent());
         int failed = 0;
         int skipped = 0;
         int passed = 0;
         Iterator i$ = results.values().iterator();

         while(i$.hasNext()) {
            ISuiteResult result = (ISuiteResult)i$.next();
            ITestContext context = result.getTestContext();
            failed += context.getFailedTests().size();
            this.generateTests("failed", context.getFailedTests(), context, xs1);
            skipped += context.getSkippedTests().size();
            this.generateTests("skipped", context.getSkippedTests(), context, xs2);
            passed += context.getPassedTests().size();
            this.generateTests("passed", context.getPassedTests(), context, xs3);
         }

         xsb.addOptional("div", "Failed tests", "class", "result-banner failed");
         xsb.addString(xs1.toXML());
         xsb.addOptional("div", "Skipped tests", "class", "result-banner skipped");
         xsb.addString(xs2.toXML());
         xsb.addOptional("div", "Passed tests", "class", "result-banner passed");
         xsb.addString(xs3.toXML());
         header.push("div", "class", "suite");
         header.push("div", "class", "suite-header");
         header.addOptional("span", suite.getName(), "class", "suite-name");
         header.push("div", "class", "stats");
         int total = failed + skipped + passed;
         String stats = String.format("%s, %d failed, %d skipped, %d passed", this.pluralize(total, "method"), failed, skipped, passed);
         header.push("ul");
         header.push("li");
         header.addOptional("span", stats, "class", "method-stats");
         header.pop("li");
         header.push("li");
         header.addOptional("span", String.format("%s ", this.pluralize(results.values().size(), "test"), "class", "test-stats"));
         header.pop("li");
         header.push("ul");
         Iterator i$ = results.values().iterator();

         while(i$.hasNext()) {
            ISuiteResult tr = (ISuiteResult)i$.next();
            String testName = tr.getTestContext().getName();
            header.push("li");
            header.addOptional("a", testName, "href", "#" + (String)this.m_testMap.get(testName));
            header.pop("li");
         }

         header.pop("ul");
         header.pop("ul");
         header.pop("div");
         header.pop("div");
         main.addString(header.toXML());
         main.addString(xsb.toXML());
      }
   }

   private String capitalize(String s) {
      return Character.toUpperCase(s.charAt(0)) + s.substring(1);
   }

   private void generateTests(String tagClass, IResultMap tests, ITestContext context, XMLStringBuffer xsb) {
      if (!tests.getAllMethods().isEmpty()) {
         xsb.push("div", "class", "test" + (tagClass != null ? " " + tagClass : ""));
         ListMultiMap<Class<?>, ITestResult> map = Maps.newListMultiMap();
         Iterator i$ = tests.getAllResults().iterator();

         while(i$.hasNext()) {
            ITestResult m = (ITestResult)i$.next();
            map.put(m.getTestClass().getRealClass(), m);
         }

         String testName = "test-" + this.m_testCount++;
         this.m_testMap.put(context.getName(), testName);
         xsb.push("div", "class", "test-name");
         xsb.push("a", "name", testName);
         xsb.addString(context.getName());
         xsb.pop("a");
         xsb.push("a", "class", "expand", "href", "#");
         xsb.addEmptyElement("img", "src", getStatusImage(tagClass));
         xsb.pop("a");
         xsb.pop("div");
         xsb.push("div", "class", "test-content");
         Iterator i$ = map.keySet().iterator();

         while(i$.hasNext()) {
            Class<?> c = (Class)i$.next();
            xsb.push("div", "class", "class");
            xsb.push("div", "class", "class-header");
            xsb.addEmptyElement("img", "src", getImage(tagClass));
            xsb.addOptional("span", c.getName(), "class", "class-name");
            xsb.pop("div");
            xsb.push("div", "class", "class-content");
            List<ITestResult> l = (List)map.get(c);
            Iterator i$ = l.iterator();

            while(i$.hasNext()) {
               ITestResult m = (ITestResult)i$.next();
               this.generateMethod(tagClass, m, context, xsb);
            }

            xsb.pop("div");
            xsb.pop("div");
         }

         xsb.pop("div");
         xsb.pop("div");
      }
   }

   private static String getStatusImage(String status) {
      return "up.png";
   }

   private static String getImage(String tagClass) {
      return tagClass + ".png";
   }

   private void generateMethod(String tagClass, ITestResult tr, ITestContext context, XMLStringBuffer xsb) {
      long time = tr.getEndMillis() - tr.getStartMillis();
      xsb.push("div", "class", "method");
      xsb.push("div", "class", "method-content");
      xsb.addOptional("span", tr.getMethod().getMethodName(), "class", "method-name");
      StringBuilder stackTrace;
      int i$;
      if (tr.getParameters().length > 0) {
         stackTrace = new StringBuilder();
         boolean first = true;
         Object[] arr$ = tr.getParameters();
         i$ = arr$.length;

         for(int i$ = 0; i$ < i$; ++i$) {
            Object p = arr$[i$];
            if (!first) {
               stackTrace.append(", ");
            }

            first = false;
            stackTrace.append(Utils.toString(p));
         }

         xsb.addOptional("span", "(" + stackTrace.toString() + ")", "class", "parameters");
      }

      if (tr.getThrowable() != null) {
         stackTrace = new StringBuilder();
         StackTraceElement[] arr$ = tr.getThrowable().getStackTrace();
         int len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            StackTraceElement str = arr$[i$];
            stackTrace.append(str.toString()).append("<br>");
         }

         xsb.addOptional("div", stackTrace.toString() + "\n", "class", "stack-trace");
      }

      xsb.addOptional("span", " " + Long.toString(time) + " ms", "class", "method-time");
      xsb.pop("div");
      xsb.pop("div");
   }

   protected String generateOutputDirectoryName(String outputDirectory) {
      return outputDirectory;
   }

   private String pluralize(int count, String singular) {
      return Integer.toString(count) + " " + (count > 1 ? (singular.endsWith("s") ? singular + "es" : singular + "s") : singular);
   }
}
