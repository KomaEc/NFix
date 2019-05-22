package org.testng.reporters;

import java.util.Iterator;
import java.util.List;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;

public class TextReporter extends TestListenerAdapter {
   private int m_verbose = 0;
   private String m_testName = null;

   public TextReporter(String testName, int verbose) {
      this.m_testName = testName;
      this.m_verbose = verbose;
   }

   public void onFinish(ITestContext context) {
      if (this.m_verbose >= 2) {
         this.logResults();
      }

   }

   private ITestNGMethod[] resultsToMethods(List<ITestResult> results) {
      ITestNGMethod[] result = new ITestNGMethod[results.size()];
      int i = 0;

      ITestResult tr;
      for(Iterator i$ = results.iterator(); i$.hasNext(); result[i++] = tr.getMethod()) {
         tr = (ITestResult)i$.next();
      }

      return result;
   }

   private void logResults() {
      Iterator i$;
      Object o;
      ITestResult tr;
      Throwable throwable;
      String stackTrace;
      for(i$ = this.getConfigurationFailures().iterator(); i$.hasNext(); this.logResult("FAILED CONFIGURATION", Utils.detailedMethodName(tr.getMethod(), false), tr.getMethod().getDescription(), stackTrace, tr.getParameters(), tr.getMethod().getMethod().getParameterTypes())) {
         o = i$.next();
         tr = (ITestResult)o;
         throwable = tr.getThrowable();
         stackTrace = "";
         if (throwable != null && this.m_verbose >= 2) {
            stackTrace = Utils.stackTrace(throwable, false)[0];
         }
      }

      i$ = this.getConfigurationSkips().iterator();

      while(i$.hasNext()) {
         o = i$.next();
         tr = (ITestResult)o;
         this.logResult("SKIPPED CONFIGURATION", Utils.detailedMethodName(tr.getMethod(), false), tr.getMethod().getDescription(), (String)null, tr.getParameters(), tr.getMethod().getMethod().getParameterTypes());
      }

      i$ = this.getPassedTests().iterator();

      while(i$.hasNext()) {
         o = i$.next();
         tr = (ITestResult)o;
         this.logResult("PASSED", tr, (String)null);
      }

      for(i$ = this.getFailedTests().iterator(); i$.hasNext(); this.logResult("FAILED", tr, stackTrace)) {
         o = i$.next();
         tr = (ITestResult)o;
         throwable = tr.getThrowable();
         stackTrace = "";
         if (throwable != null && this.m_verbose >= 2) {
            stackTrace = Utils.stackTrace(throwable, false)[0];
         }
      }

      i$ = this.getSkippedTests().iterator();

      while(i$.hasNext()) {
         o = i$.next();
         tr = (ITestResult)o;
         throwable = tr.getThrowable();
         this.logResult("SKIPPED", tr, throwable != null ? Utils.stackTrace(throwable, false)[0] : null);
      }

      ITestNGMethod[] ft = this.resultsToMethods(this.getFailedTests());
      StringBuffer logBuf = new StringBuffer("\n===============================================\n");
      logBuf.append("    ").append(this.m_testName).append("\n");
      logBuf.append("    Tests run: ").append(Utils.calculateInvokedMethodCount(this.getAllTestMethods())).append(", Failures: ").append(Utils.calculateInvokedMethodCount(ft)).append(", Skips: ").append(Utils.calculateInvokedMethodCount(this.resultsToMethods(this.getSkippedTests())));
      int confFailures = this.getConfigurationFailures().size();
      int confSkips = this.getConfigurationSkips().size();
      if (confFailures > 0 || confSkips > 0) {
         logBuf.append("\n").append("    Configuration Failures: ").append(confFailures).append(", Skips: ").append(confSkips);
      }

      logBuf.append("\n===============================================\n");
      this.logResult("", logBuf.toString());
   }

   private String getName() {
      return this.m_testName;
   }

   private void logResult(String status, ITestResult tr, String stackTrace) {
      this.logResult(status, tr.getName(), tr.getMethod().getDescription(), stackTrace, tr.getParameters(), tr.getMethod().getMethod().getParameterTypes());
   }

   private void logResult(String status, String message) {
      StringBuffer buf = new StringBuffer();
      if (Utils.isStringNotBlank(status)) {
         buf.append(status).append(": ");
      }

      buf.append(message);
      System.out.println(buf);
   }

   private void logResult(String status, String name, String description, String stackTrace, Object[] params, Class[] paramTypes) {
      StringBuffer msg = new StringBuffer(name);
      int i;
      if (null != params && params.length > 0) {
         msg.append("(");
         if (params.length != paramTypes.length) {
            msg.append(name + ": Wrong number of arguments were passed by " + "the Data Provider: found " + params.length + " but " + "expected " + paramTypes.length + ")");
         } else {
            for(i = 0; i < params.length; ++i) {
               if (i > 0) {
                  msg.append(", ");
               }

               msg.append(Utils.toString(params[i], paramTypes[i]));
            }

            msg.append(")");
         }
      }

      if (!Utils.isStringEmpty(description)) {
         msg.append("\n");

         for(i = 0; i < status.length() + 2; ++i) {
            msg.append(" ");
         }

         msg.append(description);
      }

      if (!Utils.isStringEmpty(stackTrace)) {
         msg.append("\n").append(stackTrace);
      }

      this.logResult(status, msg.toString());
   }

   public void ppp(String s) {
      System.out.println("[TextReporter " + this.getName() + "] " + s);
   }
}
