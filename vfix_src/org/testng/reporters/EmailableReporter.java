package org.testng.reporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

public class EmailableReporter implements IReporter {
   private static final Logger L = Logger.getLogger(EmailableReporter.class);
   private PrintWriter m_out;
   private int m_row;
   private Integer m_testIndex;
   private int m_methodIndex;

   public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {
      try {
         this.m_out = this.createWriter(outdir);
      } catch (IOException var5) {
         L.error("output file", var5);
         return;
      }

      this.startHtml(this.m_out);
      this.generateSuiteSummaryReport(suites);
      this.generateMethodSummaryReport(suites);
      this.generateMethodDetailReport(suites);
      this.endHtml(this.m_out);
      this.m_out.flush();
      this.m_out.close();
   }

   protected PrintWriter createWriter(String outdir) throws IOException {
      (new File(outdir)).mkdirs();
      return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "emailable-report.html"))));
   }

   protected void generateMethodSummaryReport(List<ISuite> suites) {
      this.m_methodIndex = 0;
      this.startResultSummaryTable("methodOverview");
      int testIndex = 1;
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         if (suites.size() > 1) {
            this.titleRow(suite.getName(), 5);
         }

         Map<String, ISuiteResult> r = suite.getResults();

         for(Iterator i$ = r.values().iterator(); i$.hasNext(); ++testIndex) {
            ISuiteResult r2 = (ISuiteResult)i$.next();
            ITestContext testContext = r2.getTestContext();
            String testName = testContext.getName();
            this.m_testIndex = testIndex;
            this.resultSummary(suite, testContext.getFailedConfigurations(), testName, "failed", " (configuration methods)");
            this.resultSummary(suite, testContext.getFailedTests(), testName, "failed", "");
            this.resultSummary(suite, testContext.getSkippedConfigurations(), testName, "skipped", " (configuration methods)");
            this.resultSummary(suite, testContext.getSkippedTests(), testName, "skipped", "");
            this.resultSummary(suite, testContext.getPassedTests(), testName, "passed", "");
         }
      }

      this.m_out.println("</table>");
   }

   protected void generateMethodDetailReport(List<ISuite> suites) {
      this.m_methodIndex = 0;
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         Map<String, ISuiteResult> r = suite.getResults();
         Iterator i$ = r.values().iterator();

         while(i$.hasNext()) {
            ISuiteResult r2 = (ISuiteResult)i$.next();
            ITestContext testContext = r2.getTestContext();
            if (r.values().size() > 0) {
               this.m_out.println("<h1>" + testContext.getName() + "</h1>");
            }

            this.resultDetail(testContext.getFailedConfigurations());
            this.resultDetail(testContext.getFailedTests());
            this.resultDetail(testContext.getSkippedConfigurations());
            this.resultDetail(testContext.getSkippedTests());
            this.resultDetail(testContext.getPassedTests());
         }
      }

   }

   private void resultSummary(ISuite suite, IResultMap tests, String testname, String style, String details) {
      if (tests.getAllResults().size() > 0) {
         StringBuffer buff = new StringBuffer();
         String lastClassName = "";
         int mq = 0;
         int cq = 0;
         Iterator i$ = this.getMethodSet(tests, suite).iterator();

         while(i$.hasNext()) {
            ITestNGMethod method = (ITestNGMethod)i$.next();
            ++this.m_row;
            ++this.m_methodIndex;
            ITestClass testClass = method.getTestClass();
            String className = testClass.getName();
            if (mq == 0) {
               String id = this.m_testIndex == null ? null : "t" + Integer.toString(this.m_testIndex);
               this.titleRow(testname + " &#8212; " + style + details, 5, id);
               this.m_testIndex = null;
            }

            if (!className.equalsIgnoreCase(lastClassName)) {
               if (mq > 0) {
                  ++cq;
                  this.m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
                  if (mq > 1) {
                     this.m_out.print(" rowspan=\"" + mq + "\"");
                  }

                  this.m_out.println(">" + lastClassName + "</td>" + buff);
               }

               mq = 0;
               buff.setLength(0);
               lastClassName = className;
            }

            Set<ITestResult> resultSet = tests.getResults(method);
            long end = Long.MIN_VALUE;
            long start = Long.MAX_VALUE;
            Iterator i$ = tests.getResults(method).iterator();

            while(i$.hasNext()) {
               ITestResult testResult = (ITestResult)i$.next();
               if (testResult.getEndMillis() > end) {
                  end = testResult.getEndMillis();
               }

               if (testResult.getStartMillis() < start) {
                  start = testResult.getStartMillis();
               }
            }

            ++mq;
            if (mq > 1) {
               buff.append("<tr class=\"" + style + (cq % 2 == 0 ? "odd" : "even") + "\">");
            }

            String description = method.getDescription();
            String testInstanceName = ((ITestResult[])resultSet.toArray(new ITestResult[0]))[0].getTestName();
            buff.append("<td><a href=\"#m" + this.m_methodIndex + "\">" + this.qualifiedName(method) + " " + (description != null && description.length() > 0 ? "(\"" + description + "\")" : "") + "</a>" + (null == testInstanceName ? "" : "<br>(" + testInstanceName + ")") + "</td>" + "<td class=\"numi\">" + resultSet.size() + "</td>" + "<td>" + start + "</td>" + "<td class=\"numi\">" + (end - start) + "</td>" + "</tr>");
         }

         if (mq > 0) {
            ++cq;
            this.m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
            if (mq > 1) {
               this.m_out.print(" rowspan=\"" + mq + "\"");
            }

            this.m_out.println(">" + lastClassName + "</td>" + buff);
         }
      }

   }

   private void startResultSummaryTable(String style) {
      this.tableStart(style, "summary");
      this.m_out.println("<tr><th>Class</th><th>Method</th><th># of<br/>Scenarios</th><th>Start</th><th>Time<br/>(ms)</th></tr>");
      this.m_row = 0;
   }

   private String qualifiedName(ITestNGMethod method) {
      StringBuilder addon = new StringBuilder();
      String[] groups = method.getGroups();
      int length = groups.length;
      if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
         addon.append("(");

         for(int i = 0; i < length; ++i) {
            if (i > 0) {
               addon.append(", ");
            }

            addon.append(groups[i]);
         }

         addon.append(")");
      }

      return "<b>" + method.getMethodName() + "</b> " + addon;
   }

   private void resultDetail(IResultMap tests) {
      Iterator i$ = tests.getAllResults().iterator();

      while(i$.hasNext()) {
         ITestResult result = (ITestResult)i$.next();
         ITestNGMethod method = result.getMethod();
         ++this.m_methodIndex;
         String cname = method.getTestClass().getName();
         this.m_out.println("<h2 id=\"m" + this.m_methodIndex + "\">" + cname + ":" + method.getMethodName() + "</h2>");
         Set<ITestResult> resultSet = tests.getResults(method);
         this.generateForResult(result, method, resultSet.size());
         this.m_out.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
      }

   }

   private void generateForResult(ITestResult ans, ITestNGMethod method, int resultSetSize) {
      Object[] parameters = ans.getParameters();
      boolean hasParameters = parameters != null && parameters.length > 0;
      if (hasParameters) {
         this.tableStart("result", (String)null);
         this.m_out.print("<tr class=\"param\">");

         for(int x = 1; x <= parameters.length; ++x) {
            this.m_out.print("<th>Parameter #" + x + "</th>");
         }

         this.m_out.println("</tr>");
         this.m_out.print("<tr class=\"param stripe\">");
         Object[] arr$ = parameters;
         int len$ = parameters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object p = arr$[i$];
            this.m_out.println("<td>" + Utils.escapeHtml(Utils.toString(p)) + "</td>");
         }

         this.m_out.println("</tr>");
      }

      List<String> msgs = Reporter.getOutput(ans);
      boolean hasReporterOutput = msgs.size() > 0;
      Throwable exception = ans.getThrowable();
      boolean hasThrowable = exception != null;
      if (hasReporterOutput || hasThrowable) {
         if (hasParameters) {
            this.m_out.print("<tr><td");
            if (parameters.length > 1) {
               this.m_out.print(" colspan=\"" + parameters.length + "\"");
            }

            this.m_out.println(">");
         } else {
            this.m_out.println("<div>");
         }

         if (hasReporterOutput) {
            if (hasThrowable) {
               this.m_out.println("<h3>Test Messages</h3>");
            }

            Iterator i$ = msgs.iterator();

            while(i$.hasNext()) {
               String line = (String)i$.next();
               this.m_out.println(line + "<br/>");
            }
         }

         if (hasThrowable) {
            boolean wantsMinimalOutput = ans.getStatus() == 1;
            if (hasReporterOutput) {
               this.m_out.println("<h3>" + (wantsMinimalOutput ? "Expected Exception" : "Failure") + "</h3>");
            }

            this.generateExceptionReport(exception, method);
         }

         if (hasParameters) {
            this.m_out.println("</td></tr>");
         } else {
            this.m_out.println("</div>");
         }
      }

      if (hasParameters) {
         this.m_out.println("</table>");
      }

   }

   protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
      this.m_out.print("<div class=\"stacktrace\">");
      this.m_out.print(Utils.stackTrace(exception, true)[0]);
      this.m_out.println("</div>");
   }

   private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {
      List<IInvokedMethod> r = Lists.newArrayList();
      List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
      Iterator i$ = invokedMethods.iterator();

      while(i$.hasNext()) {
         IInvokedMethod im = (IInvokedMethod)i$.next();
         if (tests.getAllMethods().contains(im.getTestMethod())) {
            r.add(im);
         }
      }

      Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new EmailableReporter.TestSorter());
      List<ITestNGMethod> result = Lists.newArrayList();
      Iterator i$ = r.iterator();

      while(i$.hasNext()) {
         IInvokedMethod m = (IInvokedMethod)i$.next();
         result.add(m.getTestMethod());
      }

      i$ = tests.getAllMethods().iterator();

      while(i$.hasNext()) {
         ITestNGMethod m = (ITestNGMethod)i$.next();
         if (!result.contains(m)) {
            result.add(m);
         }
      }

      return result;
   }

   public void generateSuiteSummaryReport(List<ISuite> suites) {
      this.tableStart("testOverview", (String)null);
      this.m_out.print("<tr>");
      this.tableColumnStart("Test");
      this.tableColumnStart("Methods<br/>Passed");
      this.tableColumnStart("Scenarios<br/>Passed");
      this.tableColumnStart("# skipped");
      this.tableColumnStart("# failed");
      this.tableColumnStart("Total<br/>Time");
      this.tableColumnStart("Included<br/>Groups");
      this.tableColumnStart("Excluded<br/>Groups");
      this.m_out.println("</tr>");
      NumberFormat formatter = new DecimalFormat("#,##0.0");
      int qty_tests = 0;
      int qty_pass_m = 0;
      int qty_pass_s = 0;
      int qty_skip = 0;
      int qty_fail = 0;
      long time_start = Long.MAX_VALUE;
      long time_end = Long.MIN_VALUE;
      this.m_testIndex = 1;
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         if (suites.size() > 1) {
            this.titleRow(suite.getName(), 8);
         }

         Map<String, ISuiteResult> tests = suite.getResults();

         Integer var20;
         for(Iterator i$ = tests.values().iterator(); i$.hasNext(); var20 = this.m_testIndex = this.m_testIndex + 1) {
            ISuiteResult r = (ISuiteResult)i$.next();
            ++qty_tests;
            ITestContext overview = r.getTestContext();
            this.startSummaryRow(overview.getName());
            int q = this.getMethodSet(overview.getPassedTests(), suite).size();
            qty_pass_m += q;
            this.summaryCell(q, Integer.MAX_VALUE);
            q = overview.getPassedTests().size();
            qty_pass_s += q;
            this.summaryCell(q, Integer.MAX_VALUE);
            q = this.getMethodSet(overview.getSkippedTests(), suite).size();
            qty_skip += q;
            this.summaryCell(q, 0);
            q = this.getMethodSet(overview.getFailedTests(), suite).size();
            qty_fail += q;
            this.summaryCell(q, 0);
            time_start = Math.min(overview.getStartDate().getTime(), time_start);
            time_end = Math.max(overview.getEndDate().getTime(), time_end);
            this.summaryCell(formatter.format((double)(overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000.0D) + " seconds", true);
            this.summaryCell(overview.getIncludedGroups());
            this.summaryCell(overview.getExcludedGroups());
            this.m_out.println("</tr>");
            Integer var19 = this.m_testIndex;
         }
      }

      if (qty_tests > 1) {
         this.m_out.println("<tr class=\"total\"><td>Total</td>");
         this.summaryCell(qty_pass_m, Integer.MAX_VALUE);
         this.summaryCell(qty_pass_s, Integer.MAX_VALUE);
         this.summaryCell(qty_skip, 0);
         this.summaryCell(qty_fail, 0);
         this.summaryCell(formatter.format((double)(time_end - time_start) / 1000.0D) + " seconds", true);
         this.m_out.println("<td colspan=\"2\">&nbsp;</td></tr>");
      }

      this.m_out.println("</table>");
   }

   private void summaryCell(String[] val) {
      StringBuffer b = new StringBuffer();
      String[] arr$ = val;
      int len$ = val.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String v = arr$[i$];
         b.append(v + " ");
      }

      this.summaryCell(b.toString(), true);
   }

   private void summaryCell(String v, boolean isgood) {
      this.m_out.print("<td class=\"numi" + (isgood ? "" : "_attn") + "\">" + v + "</td>");
   }

   private void startSummaryRow(String label) {
      ++this.m_row;
      this.m_out.print("<tr" + (this.m_row % 2 == 0 ? " class=\"stripe\"" : "") + "><td style=\"text-align:left;padding-right:2em\"><a href=\"#t" + this.m_testIndex + "\">" + label + "</a>" + "</td>");
   }

   private void summaryCell(int v, int maxexpected) {
      this.summaryCell(String.valueOf(v), v <= maxexpected);
   }

   private void tableStart(String cssclass, String id) {
      this.m_out.println("<table cellspacing=\"0\" cellpadding=\"0\"" + (cssclass != null ? " class=\"" + cssclass + "\"" : " style=\"padding-bottom:2em\"") + (id != null ? " id=\"" + id + "\"" : "") + ">");
      this.m_row = 0;
   }

   private void tableColumnStart(String label) {
      this.m_out.print("<th>" + label + "</th>");
   }

   private void titleRow(String label, int cq) {
      this.titleRow(label, cq, (String)null);
   }

   private void titleRow(String label, int cq, String id) {
      this.m_out.print("<tr");
      if (id != null) {
         this.m_out.print(" id=\"" + id + "\"");
      }

      this.m_out.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
      this.m_row = 0;
   }

   protected void startHtml(PrintWriter out) {
      out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
      out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
      out.println("<head>");
      out.println("<title>TestNG Report</title>");
      out.println("<style type=\"text/css\">");
      out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
      out.println("td,th {border:1px solid #009;padding:.25em .5em}");
      out.println(".result th {vertical-align:bottom}");
      out.println(".param th {padding-left:1em;padding-right:1em}");
      out.println(".param td {padding-left:.5em;padding-right:2em}");
      out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
      out.println(".numi,.numi_attn {text-align:right}");
      out.println(".total td {font-weight:bold}");
      out.println(".passedodd td {background-color: #0A0}");
      out.println(".passedeven td {background-color: #3F3}");
      out.println(".skippedodd td {background-color: #CCC}");
      out.println(".skippedodd td {background-color: #DDD}");
      out.println(".failedodd td,.numi_attn {background-color: #F33}");
      out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
      out.println(".stacktrace {white-space:pre;font-family:monospace}");
      out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
      out.println("</style>");
      out.println("</head>");
      out.println("<body>");
   }

   protected void endHtml(PrintWriter out) {
      out.println("</body></html>");
   }

   private class TestSorter implements Comparator<IInvokedMethod> {
      private TestSorter() {
      }

      public int compare(IInvokedMethod o1, IInvokedMethod o2) {
         return (int)(o1.getDate() - o2.getDate());
      }

      // $FF: synthetic method
      TestSorter(Object x1) {
         this();
      }
   }
}
