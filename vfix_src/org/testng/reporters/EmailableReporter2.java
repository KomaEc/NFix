package org.testng.reporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

public class EmailableReporter2 implements IReporter {
   private static final Logger LOG = Logger.getLogger(EmailableReporter.class);
   protected PrintWriter writer;
   protected List<EmailableReporter2.SuiteResult> suiteResults = Lists.newArrayList();
   private StringBuilder buffer = new StringBuilder();

   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
      try {
         this.writer = this.createWriter(outputDirectory);
      } catch (IOException var6) {
         LOG.error("Unable to create output file", var6);
         return;
      }

      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         this.suiteResults.add(new EmailableReporter2.SuiteResult(suite));
      }

      this.writeDocumentStart();
      this.writeHead();
      this.writeBody();
      this.writeDocumentEnd();
      this.writer.close();
   }

   protected PrintWriter createWriter(String outdir) throws IOException {
      (new File(outdir)).mkdirs();
      return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "emailable-report.html"))));
   }

   protected void writeDocumentStart() {
      this.writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
      this.writer.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
   }

   protected void writeHead() {
      this.writer.print("<head>");
      this.writer.print("<title>TestNG Report</title>");
      this.writeStylesheet();
      this.writer.print("</head>");
   }

   protected void writeStylesheet() {
      this.writer.print("<style type=\"text/css\">");
      this.writer.print("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
      this.writer.print("th,td {border:1px solid #009;padding:.25em .5em}");
      this.writer.print("th {vertical-align:bottom}");
      this.writer.print("td {vertical-align:top}");
      this.writer.print("table a {font-weight:bold}");
      this.writer.print(".stripe td {background-color: #E6EBF9}");
      this.writer.print(".num {text-align:right}");
      this.writer.print(".passedodd td {background-color: #3F3}");
      this.writer.print(".passedeven td {background-color: #0A0}");
      this.writer.print(".skippedodd td {background-color: #DDD}");
      this.writer.print(".skippedeven td {background-color: #CCC}");
      this.writer.print(".failedodd td,.attn {background-color: #F33}");
      this.writer.print(".failedeven td,.stripe .attn {background-color: #D00}");
      this.writer.print(".stacktrace {white-space:pre;font-family:monospace}");
      this.writer.print(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
      this.writer.print("</style>");
   }

   protected void writeBody() {
      this.writer.print("<body>");
      this.writeSuiteSummary();
      this.writeScenarioSummary();
      this.writeScenarioDetails();
      this.writer.print("</body>");
   }

   protected void writeDocumentEnd() {
      this.writer.print("</html>");
   }

   protected void writeSuiteSummary() {
      NumberFormat integerFormat = NumberFormat.getIntegerInstance();
      NumberFormat decimalFormat = NumberFormat.getNumberInstance();
      int totalPassedTests = 0;
      int totalSkippedTests = 0;
      int totalFailedTests = 0;
      long totalDuration = 0L;
      this.writer.print("<table>");
      this.writer.print("<tr>");
      this.writer.print("<th>Test</th>");
      this.writer.print("<th># Passed</th>");
      this.writer.print("<th># Skipped</th>");
      this.writer.print("<th># Failed</th>");
      this.writer.print("<th>Time (ms)</th>");
      this.writer.print("<th>Included Groups</th>");
      this.writer.print("<th>Excluded Groups</th>");
      this.writer.print("</tr>");
      int testIndex = 0;
      Iterator i$ = this.suiteResults.iterator();

      while(i$.hasNext()) {
         EmailableReporter2.SuiteResult suiteResult = (EmailableReporter2.SuiteResult)i$.next();
         this.writer.print("<tr><th colspan=\"7\">");
         this.writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
         this.writer.print("</th></tr>");

         for(Iterator i$ = suiteResult.getTestResults().iterator(); i$.hasNext(); ++testIndex) {
            EmailableReporter2.TestResult testResult = (EmailableReporter2.TestResult)i$.next();
            int passedTests = testResult.getPassedTestCount();
            int skippedTests = testResult.getSkippedTestCount();
            int failedTests = testResult.getFailedTestCount();
            long duration = testResult.getDuration();
            this.writer.print("<tr");
            if (testIndex % 2 == 1) {
               this.writer.print(" class=\"stripe\"");
            }

            this.writer.print(">");
            this.buffer.setLength(0);
            this.writeTableData(this.buffer.append("<a href=\"#t").append(testIndex).append("\">").append(Utils.escapeHtml(testResult.getTestName())).append("</a>").toString());
            this.writeTableData(integerFormat.format((long)passedTests), "num");
            this.writeTableData(integerFormat.format((long)skippedTests), skippedTests > 0 ? "num attn" : "num");
            this.writeTableData(integerFormat.format((long)failedTests), failedTests > 0 ? "num attn" : "num");
            this.writeTableData(decimalFormat.format(duration), "num");
            this.writeTableData(testResult.getIncludedGroups());
            this.writeTableData(testResult.getExcludedGroups());
            this.writer.print("</tr>");
            totalPassedTests += passedTests;
            totalSkippedTests += skippedTests;
            totalFailedTests += failedTests;
            totalDuration += duration;
         }
      }

      if (testIndex > 1) {
         this.writer.print("<tr>");
         this.writer.print("<th>Total</th>");
         this.writeTableHeader(integerFormat.format((long)totalPassedTests), "num");
         this.writeTableHeader(integerFormat.format((long)totalSkippedTests), totalSkippedTests > 0 ? "num attn" : "num");
         this.writeTableHeader(integerFormat.format((long)totalFailedTests), totalFailedTests > 0 ? "num attn" : "num");
         this.writeTableHeader(decimalFormat.format(totalDuration), "num");
         this.writer.print("<th colspan=\"2\"></th>");
         this.writer.print("</tr>");
      }

      this.writer.print("</table>");
   }

   protected void writeScenarioSummary() {
      this.writer.print("<table>");
      this.writer.print("<thead>");
      this.writer.print("<tr>");
      this.writer.print("<th>Class</th>");
      this.writer.print("<th>Method</th>");
      this.writer.print("<th>Start</th>");
      this.writer.print("<th>Time (ms)</th>");
      this.writer.print("</tr>");
      this.writer.print("</thead>");
      int testIndex = 0;
      int scenarioIndex = 0;
      Iterator i$ = this.suiteResults.iterator();

      while(i$.hasNext()) {
         EmailableReporter2.SuiteResult suiteResult = (EmailableReporter2.SuiteResult)i$.next();
         this.writer.print("<tbody><tr><th colspan=\"4\">");
         this.writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
         this.writer.print("</th></tr></tbody>");

         for(Iterator i$ = suiteResult.getTestResults().iterator(); i$.hasNext(); ++testIndex) {
            EmailableReporter2.TestResult testResult = (EmailableReporter2.TestResult)i$.next();
            this.writer.print("<tbody id=\"t");
            this.writer.print(testIndex);
            this.writer.print("\">");
            String testName = Utils.escapeHtml(testResult.getTestName());
            scenarioIndex += this.writeScenarioSummary(testName + " &#8212; failed (configuration methods)", testResult.getFailedConfigurationResults(), "failed", scenarioIndex);
            scenarioIndex += this.writeScenarioSummary(testName + " &#8212; failed", testResult.getFailedTestResults(), "failed", scenarioIndex);
            scenarioIndex += this.writeScenarioSummary(testName + " &#8212; skipped (configuration methods)", testResult.getSkippedConfigurationResults(), "skipped", scenarioIndex);
            scenarioIndex += this.writeScenarioSummary(testName + " &#8212; skipped", testResult.getSkippedTestResults(), "skipped", scenarioIndex);
            scenarioIndex += this.writeScenarioSummary(testName + " &#8212; passed", testResult.getPassedTestResults(), "passed", scenarioIndex);
            this.writer.print("</tbody>");
         }
      }

      this.writer.print("</table>");
   }

   private int writeScenarioSummary(String description, List<EmailableReporter2.ClassResult> classResults, String cssClassPrefix, int startingScenarioIndex) {
      int scenarioCount = 0;
      if (!classResults.isEmpty()) {
         this.writer.print("<tr><th colspan=\"4\">");
         this.writer.print(description);
         this.writer.print("</th></tr>");
         int scenarioIndex = startingScenarioIndex;
         int classIndex = 0;

         for(Iterator i$ = classResults.iterator(); i$.hasNext(); ++classIndex) {
            EmailableReporter2.ClassResult classResult = (EmailableReporter2.ClassResult)i$.next();
            String cssClass = cssClassPrefix + (classIndex % 2 == 0 ? "even" : "odd");
            this.buffer.setLength(0);
            int scenariosPerClass = 0;
            int methodIndex = 0;

            for(Iterator i$ = classResult.getMethodResults().iterator(); i$.hasNext(); ++methodIndex) {
               EmailableReporter2.MethodResult methodResult = (EmailableReporter2.MethodResult)i$.next();
               List<ITestResult> results = methodResult.getResults();
               int resultsCount = results.size();

               assert resultsCount > 0;

               ITestResult firstResult = (ITestResult)results.iterator().next();
               String methodName = Utils.escapeHtml(firstResult.getMethod().getMethodName());
               long start = firstResult.getStartMillis();
               long duration = firstResult.getEndMillis() - start;
               if (methodIndex > 0) {
                  this.buffer.append("<tr class=\"").append(cssClass).append("\">");
               }

               this.buffer.append("<td><a href=\"#m").append(scenarioIndex).append("\">").append(methodName).append("</a></td>").append("<td rowspan=\"").append(resultsCount).append("\">").append(start).append("</td>").append("<td rowspan=\"").append(resultsCount).append("\">").append(duration).append("</td></tr>");
               ++scenarioIndex;

               for(int i = 1; i < resultsCount; ++i) {
                  this.buffer.append("<tr class=\"").append(cssClass).append("\">").append("<td><a href=\"#m").append(scenarioIndex).append("\">").append(methodName).append("</a></td></tr>");
                  ++scenarioIndex;
               }

               scenariosPerClass += resultsCount;
            }

            this.writer.print("<tr class=\"");
            this.writer.print(cssClass);
            this.writer.print("\">");
            this.writer.print("<td rowspan=\"");
            this.writer.print(scenariosPerClass);
            this.writer.print("\">");
            this.writer.print(Utils.escapeHtml(classResult.getClassName()));
            this.writer.print("</td>");
            this.writer.print(this.buffer);
         }

         scenarioCount = scenarioIndex - startingScenarioIndex;
      }

      return scenarioCount;
   }

   protected void writeScenarioDetails() {
      int scenarioIndex = 0;
      Iterator i$ = this.suiteResults.iterator();

      while(i$.hasNext()) {
         EmailableReporter2.SuiteResult suiteResult = (EmailableReporter2.SuiteResult)i$.next();

         EmailableReporter2.TestResult testResult;
         for(Iterator i$ = suiteResult.getTestResults().iterator(); i$.hasNext(); scenarioIndex += this.writeScenarioDetails(testResult.getPassedTestResults(), scenarioIndex)) {
            testResult = (EmailableReporter2.TestResult)i$.next();
            this.writer.print("<h2>");
            this.writer.print(Utils.escapeHtml(testResult.getTestName()));
            this.writer.print("</h2>");
            scenarioIndex += this.writeScenarioDetails(testResult.getFailedConfigurationResults(), scenarioIndex);
            scenarioIndex += this.writeScenarioDetails(testResult.getFailedTestResults(), scenarioIndex);
            scenarioIndex += this.writeScenarioDetails(testResult.getSkippedConfigurationResults(), scenarioIndex);
            scenarioIndex += this.writeScenarioDetails(testResult.getSkippedTestResults(), scenarioIndex);
         }
      }

   }

   private int writeScenarioDetails(List<EmailableReporter2.ClassResult> classResults, int startingScenarioIndex) {
      int scenarioIndex = startingScenarioIndex;
      Iterator i$ = classResults.iterator();

      while(i$.hasNext()) {
         EmailableReporter2.ClassResult classResult = (EmailableReporter2.ClassResult)i$.next();
         String className = classResult.getClassName();
         Iterator i$ = classResult.getMethodResults().iterator();

         while(i$.hasNext()) {
            EmailableReporter2.MethodResult methodResult = (EmailableReporter2.MethodResult)i$.next();
            List<ITestResult> results = methodResult.getResults();

            assert !results.isEmpty();

            String label = Utils.escapeHtml(className + "#" + ((ITestResult)results.iterator().next()).getMethod().getMethodName());

            for(Iterator i$ = results.iterator(); i$.hasNext(); ++scenarioIndex) {
               ITestResult result = (ITestResult)i$.next();
               this.writeScenario(scenarioIndex, label, result);
            }
         }
      }

      return scenarioIndex - startingScenarioIndex;
   }

   private void writeScenario(int scenarioIndex, String label, ITestResult result) {
      this.writer.print("<h3 id=\"m");
      this.writer.print(scenarioIndex);
      this.writer.print("\">");
      this.writer.print(label);
      this.writer.print("</h3>");
      this.writer.print("<table class=\"result\">");
      Object[] parameters = result.getParameters();
      int parameterCount = parameters == null ? 0 : parameters.length;
      if (parameterCount > 0) {
         this.writer.print("<tr class=\"param\">");

         for(int i = 1; i <= parameterCount; ++i) {
            this.writer.print("<th>Parameter #");
            this.writer.print(i);
            this.writer.print("</th>");
         }

         this.writer.print("</tr><tr class=\"param stripe\">");
         Object[] arr$ = parameters;
         int len$ = parameters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object parameter = arr$[i$];
            this.writer.print("<td>");
            this.writer.print(Utils.escapeHtml(Utils.toString(parameter)));
            this.writer.print("</td>");
         }

         this.writer.print("</tr>");
      }

      List<String> reporterMessages = Reporter.getOutput(result);
      if (!reporterMessages.isEmpty()) {
         this.writer.print("<tr><th");
         if (parameterCount > 1) {
            this.writer.print(" colspan=\"");
            this.writer.print(parameterCount);
            this.writer.print("\"");
         }

         this.writer.print(">Messages</th></tr>");
         this.writer.print("<tr><td");
         if (parameterCount > 1) {
            this.writer.print(" colspan=\"");
            this.writer.print(parameterCount);
            this.writer.print("\"");
         }

         this.writer.print(">");
         this.writeReporterMessages(reporterMessages);
         this.writer.print("</td></tr>");
      }

      Throwable throwable = result.getThrowable();
      if (throwable != null) {
         this.writer.print("<tr><th");
         if (parameterCount > 1) {
            this.writer.print(" colspan=\"");
            this.writer.print(parameterCount);
            this.writer.print("\"");
         }

         this.writer.print(">");
         this.writer.print(result.getStatus() == 1 ? "Expected Exception" : "Exception");
         this.writer.print("</th></tr>");
         this.writer.print("<tr><td");
         if (parameterCount > 1) {
            this.writer.print(" colspan=\"");
            this.writer.print(parameterCount);
            this.writer.print("\"");
         }

         this.writer.print(">");
         this.writeStackTrace(throwable);
         this.writer.print("</td></tr>");
      }

      this.writer.print("</table>");
      this.writer.print("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
   }

   protected void writeReporterMessages(List<String> reporterMessages) {
      this.writer.print("<div class=\"messages\">");
      Iterator<String> iterator = reporterMessages.iterator();

      assert iterator.hasNext();

      this.writer.print(Utils.escapeHtml((String)iterator.next()));

      while(iterator.hasNext()) {
         this.writer.print("<br/>");
         this.writer.print(Utils.escapeHtml((String)iterator.next()));
      }

      this.writer.print("</div>");
   }

   protected void writeStackTrace(Throwable throwable) {
      this.writer.print("<div class=\"stacktrace\">");
      this.writer.print(Utils.stackTrace(throwable, true)[0]);
      this.writer.print("</div>");
   }

   protected void writeTableHeader(String html, String cssClasses) {
      this.writeTag("th", html, cssClasses);
   }

   protected void writeTableData(String html) {
      this.writeTableData(html, (String)null);
   }

   protected void writeTableData(String html, String cssClasses) {
      this.writeTag("td", html, cssClasses);
   }

   protected void writeTag(String tag, String html, String cssClasses) {
      this.writer.print("<");
      this.writer.print(tag);
      if (cssClasses != null) {
         this.writer.print(" class=\"");
         this.writer.print(cssClasses);
         this.writer.print("\"");
      }

      this.writer.print(">");
      this.writer.print(html);
      this.writer.print("</");
      this.writer.print(tag);
      this.writer.print(">");
   }

   protected static class MethodResult {
      private final List<ITestResult> results;

      public MethodResult(List<ITestResult> results) {
         this.results = results;
      }

      public List<ITestResult> getResults() {
         return this.results;
      }
   }

   protected static class ClassResult {
      private final String className;
      private final List<EmailableReporter2.MethodResult> methodResults;

      public ClassResult(String className, List<EmailableReporter2.MethodResult> methodResults) {
         this.className = className;
         this.methodResults = methodResults;
      }

      public String getClassName() {
         return this.className;
      }

      public List<EmailableReporter2.MethodResult> getMethodResults() {
         return this.methodResults;
      }
   }

   protected static class TestResult {
      protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {
         public int compare(ITestResult o1, ITestResult o2) {
            int result = o1.getTestClass().getName().compareTo(o2.getTestClass().getName());
            if (result == 0) {
               result = o1.getMethod().getMethodName().compareTo(o2.getMethod().getMethodName());
            }

            return result;
         }
      };
      private final String testName;
      private final List<EmailableReporter2.ClassResult> failedConfigurationResults;
      private final List<EmailableReporter2.ClassResult> failedTestResults;
      private final List<EmailableReporter2.ClassResult> skippedConfigurationResults;
      private final List<EmailableReporter2.ClassResult> skippedTestResults;
      private final List<EmailableReporter2.ClassResult> passedTestResults;
      private final int failedTestCount;
      private final int skippedTestCount;
      private final int passedTestCount;
      private final long duration;
      private final String includedGroups;
      private final String excludedGroups;

      public TestResult(ITestContext context) {
         this.testName = context.getName();
         Set<ITestResult> failedConfigurations = context.getFailedConfigurations().getAllResults();
         Set<ITestResult> failedTests = context.getFailedTests().getAllResults();
         Set<ITestResult> skippedConfigurations = context.getSkippedConfigurations().getAllResults();
         Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
         Set<ITestResult> passedTests = context.getPassedTests().getAllResults();
         this.failedConfigurationResults = this.groupResults(failedConfigurations);
         this.failedTestResults = this.groupResults(failedTests);
         this.skippedConfigurationResults = this.groupResults(skippedConfigurations);
         this.skippedTestResults = this.groupResults(skippedTests);
         this.passedTestResults = this.groupResults(passedTests);
         this.failedTestCount = failedTests.size();
         this.skippedTestCount = skippedTests.size();
         this.passedTestCount = passedTests.size();
         this.duration = context.getEndDate().getTime() - context.getStartDate().getTime();
         this.includedGroups = this.formatGroups(context.getIncludedGroups());
         this.excludedGroups = this.formatGroups(context.getExcludedGroups());
      }

      protected List<EmailableReporter2.ClassResult> groupResults(Set<ITestResult> results) {
         List<EmailableReporter2.ClassResult> classResults = Lists.newArrayList();
         if (!results.isEmpty()) {
            List<EmailableReporter2.MethodResult> resultsPerClass = Lists.newArrayList();
            List<ITestResult> resultsPerMethod = Lists.newArrayList();
            List<ITestResult> resultsList = Lists.newArrayList((Collection)results);
            Collections.sort(resultsList, RESULT_COMPARATOR);
            Iterator<ITestResult> resultsIterator = resultsList.iterator();

            assert resultsIterator.hasNext();

            ITestResult result = (ITestResult)resultsIterator.next();
            resultsPerMethod.add(result);
            String previousClassName = result.getTestClass().getName();

            for(String previousMethodName = result.getMethod().getMethodName(); resultsIterator.hasNext(); resultsPerMethod.add(result)) {
               result = (ITestResult)resultsIterator.next();
               String className = result.getTestClass().getName();
               if (!previousClassName.equals(className)) {
                  assert !resultsPerMethod.isEmpty();

                  resultsPerClass.add(new EmailableReporter2.MethodResult(resultsPerMethod));
                  resultsPerMethod = Lists.newArrayList();

                  assert !resultsPerClass.isEmpty();

                  classResults.add(new EmailableReporter2.ClassResult(previousClassName, resultsPerClass));
                  resultsPerClass = Lists.newArrayList();
                  previousClassName = className;
                  previousMethodName = result.getMethod().getMethodName();
               } else {
                  String methodName = result.getMethod().getMethodName();
                  if (!previousMethodName.equals(methodName)) {
                     assert !resultsPerMethod.isEmpty();

                     resultsPerClass.add(new EmailableReporter2.MethodResult(resultsPerMethod));
                     resultsPerMethod = Lists.newArrayList();
                     previousMethodName = methodName;
                  }
               }
            }

            assert !resultsPerMethod.isEmpty();

            resultsPerClass.add(new EmailableReporter2.MethodResult(resultsPerMethod));

            assert !resultsPerClass.isEmpty();

            classResults.add(new EmailableReporter2.ClassResult(previousClassName, resultsPerClass));
         }

         return classResults;
      }

      public String getTestName() {
         return this.testName;
      }

      public List<EmailableReporter2.ClassResult> getFailedConfigurationResults() {
         return this.failedConfigurationResults;
      }

      public List<EmailableReporter2.ClassResult> getFailedTestResults() {
         return this.failedTestResults;
      }

      public List<EmailableReporter2.ClassResult> getSkippedConfigurationResults() {
         return this.skippedConfigurationResults;
      }

      public List<EmailableReporter2.ClassResult> getSkippedTestResults() {
         return this.skippedTestResults;
      }

      public List<EmailableReporter2.ClassResult> getPassedTestResults() {
         return this.passedTestResults;
      }

      public int getFailedTestCount() {
         return this.failedTestCount;
      }

      public int getSkippedTestCount() {
         return this.skippedTestCount;
      }

      public int getPassedTestCount() {
         return this.passedTestCount;
      }

      public long getDuration() {
         return this.duration;
      }

      public String getIncludedGroups() {
         return this.includedGroups;
      }

      public String getExcludedGroups() {
         return this.excludedGroups;
      }

      protected String formatGroups(String[] groups) {
         if (groups.length == 0) {
            return "";
         } else {
            StringBuilder builder = new StringBuilder();
            builder.append(groups[0]);

            for(int i = 1; i < groups.length; ++i) {
               builder.append(", ").append(groups[i]);
            }

            return builder.toString();
         }
      }
   }

   protected static class SuiteResult {
      private final String suiteName;
      private final List<EmailableReporter2.TestResult> testResults = Lists.newArrayList();

      public SuiteResult(ISuite suite) {
         this.suiteName = suite.getName();
         Iterator i$ = suite.getResults().values().iterator();

         while(i$.hasNext()) {
            ISuiteResult suiteResult = (ISuiteResult)i$.next();
            this.testResults.add(new EmailableReporter2.TestResult(suiteResult.getTestContext()));
         }

      }

      public String getSuiteName() {
         return this.suiteName;
      }

      public List<EmailableReporter2.TestResult> getTestResults() {
         return this.testResults;
      }
   }
}
