package org.testng.reporters;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.internal.Utils;

public class VerboseReporter extends TestListenerAdapter {
   public static final String LISTENER_PREFIX = "[VerboseTestNG] ";
   private String suiteName;
   private final String prefix;

   public VerboseReporter() {
      this("[VerboseTestNG] ");
   }

   public VerboseReporter(String prefix) {
      this.prefix = prefix;
   }

   public void beforeConfiguration(ITestResult tr) {
      super.beforeConfiguration(tr);
      this.logTestResult(VerboseReporter.Status.STARTED, tr, true);
   }

   public void onConfigurationFailure(ITestResult tr) {
      super.onConfigurationFailure(tr);
      this.logTestResult(VerboseReporter.Status.FAILURE, tr, true);
   }

   public void onConfigurationSkip(ITestResult tr) {
      super.onConfigurationSkip(tr);
      this.logTestResult(VerboseReporter.Status.SKIP, tr, true);
   }

   public void onConfigurationSuccess(ITestResult tr) {
      super.onConfigurationSuccess(tr);
      this.logTestResult(VerboseReporter.Status.SUCCESS, tr, true);
   }

   public void onTestStart(ITestResult tr) {
      this.logTestResult(VerboseReporter.Status.STARTED, tr, false);
   }

   public void onTestFailure(ITestResult tr) {
      super.onTestFailure(tr);
      this.logTestResult(VerboseReporter.Status.FAILURE, tr, false);
   }

   public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {
      super.onTestFailedButWithinSuccessPercentage(tr);
      this.logTestResult(VerboseReporter.Status.SUCCESS_PERCENTAGE_FAILURE, tr, false);
   }

   public void onTestSkipped(ITestResult tr) {
      super.onTestSkipped(tr);
      this.logTestResult(VerboseReporter.Status.SKIP, tr, false);
   }

   public void onTestSuccess(ITestResult tr) {
      super.onTestSuccess(tr);
      this.logTestResult(VerboseReporter.Status.SUCCESS, tr, false);
   }

   public void onStart(ITestContext ctx) {
      this.suiteName = ctx.getName();
      this.log("RUNNING: Suite: \"" + this.suiteName + "\" containing \"" + ctx.getAllTestMethods().length + "\" Tests (config: " + ctx.getSuite().getXmlSuite().getFileName() + ")");
   }

   public void onFinish(ITestContext context) {
      this.logResults();
      this.suiteName = null;
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
      ITestNGMethod[] ft = this.resultsToMethods(this.getFailedTests());
      StringBuilder sb = new StringBuilder("\n===============================================\n");
      sb.append("    ").append(this.suiteName).append("\n");
      sb.append("    Tests run: ").append(Utils.calculateInvokedMethodCount(this.getAllTestMethods()));
      sb.append(", Failures: ").append(Utils.calculateInvokedMethodCount(ft));
      sb.append(", Skips: ").append(Utils.calculateInvokedMethodCount(this.resultsToMethods(this.getSkippedTests())));
      int confFailures = this.getConfigurationFailures().size();
      int confSkips = this.getConfigurationSkips().size();
      if (confFailures > 0 || confSkips > 0) {
         sb.append("\n").append("    Configuration Failures: ").append(confFailures);
         sb.append(", Skips: ").append(confSkips);
      }

      sb.append("\n===============================================");
      this.log(sb.toString());
   }

   private void logTestResult(VerboseReporter.Status st, ITestResult itr, boolean isConfMethod) {
      StringBuilder sb = new StringBuilder();
      StringBuilder succRate = null;
      String stackTrace = "";
      switch(st) {
      case STARTED:
         sb.append("INVOKING");
         break;
      case SKIP:
         sb.append("SKIPPED");
         stackTrace = itr.getThrowable() != null ? Utils.stackTrace(itr.getThrowable(), false)[0] : "";
         break;
      case FAILURE:
         sb.append("FAILED");
         stackTrace = itr.getThrowable() != null ? Utils.stackTrace(itr.getThrowable(), false)[0] : "";
         break;
      case SUCCESS:
         sb.append("PASSED");
         break;
      case SUCCESS_PERCENTAGE_FAILURE:
         sb.append("PASSED with failures");
         break;
      default:
         throw new RuntimeException("Unsupported test status:" + itr.getStatus());
      }

      if (isConfMethod) {
         sb.append(" CONFIGURATION: ");
      } else {
         sb.append(": ");
      }

      ITestNGMethod tm = itr.getMethod();
      int identLevel = sb.length();
      sb.append(this.getMethodDeclaration(tm));
      Object[] params = itr.getParameters();
      Class[] paramTypes = tm.getMethod().getParameterTypes();
      int i;
      if (null != params && params.length > 0) {
         if (params.length != paramTypes.length) {
            sb.append("Wrong number of arguments were passed by the Data Provider: found ");
            sb.append(params.length);
            sb.append(" but expected ");
            sb.append(paramTypes.length);
         } else {
            sb.append("(value(s): ");

            for(i = 0; i < params.length; ++i) {
               if (i > 0) {
                  sb.append(", ");
               }

               sb.append(Utils.toString(params[i], paramTypes[i]));
            }

            sb.append(")");
         }
      }

      if (VerboseReporter.Status.STARTED != st) {
         sb.append(" finished in ");
         sb.append(itr.getEndMillis() - itr.getStartMillis());
         sb.append(" ms");
         if (!Utils.isStringEmpty(tm.getDescription())) {
            sb.append("\n");

            for(i = 0; i < identLevel; ++i) {
               sb.append(" ");
            }

            sb.append(tm.getDescription());
         }

         if (tm.getInvocationCount() > 1) {
            sb.append(" (");
            sb.append(tm.getCurrentInvocationCount());
            sb.append(" of ");
            sb.append(tm.getInvocationCount());
            sb.append(")");
         }

         if (!Utils.isStringEmpty(stackTrace)) {
            sb.append("\n").append(stackTrace.substring(0, stackTrace.lastIndexOf(System.getProperty("line.separator"))));
         }
      } else if (!isConfMethod && tm.getInvocationCount() > 1) {
         sb.append(" success: ");
         sb.append(tm.getSuccessPercentage());
         sb.append("%");
      }

      this.log(sb.toString());
   }

   protected void log(String message) {
      System.out.println(message.replaceAll("(?m)^", this.prefix));
   }

   private String getMethodDeclaration(ITestNGMethod method) {
      Method m = method.getMethod();
      StringBuilder buf = new StringBuilder();
      buf.append("\"");
      if (this.suiteName != null) {
         buf.append(this.suiteName);
      } else {
         buf.append("UNKNOWN");
      }

      buf.append("\"");
      buf.append(" - ");
      if (method.isBeforeSuiteConfiguration()) {
         buf.append("@BeforeSuite ");
      } else if (method.isBeforeTestConfiguration()) {
         buf.append("@BeforeTest ");
      } else if (method.isBeforeClassConfiguration()) {
         buf.append("@BeforeClass ");
      } else if (method.isBeforeGroupsConfiguration()) {
         buf.append("@BeforeGroups ");
      } else if (method.isBeforeMethodConfiguration()) {
         buf.append("@BeforeMethod ");
      } else if (method.isAfterMethodConfiguration()) {
         buf.append("@AfterMethod ");
      } else if (method.isAfterGroupsConfiguration()) {
         buf.append("@AfterGroups ");
      } else if (method.isAfterClassConfiguration()) {
         buf.append("@AfterClass ");
      } else if (method.isAfterTestConfiguration()) {
         buf.append("@AfterTest ");
      } else if (method.isAfterSuiteConfiguration()) {
         buf.append("@AfterSuite ");
      }

      buf.append(m.getDeclaringClass().getName());
      buf.append(".");
      buf.append(m.getName());
      buf.append("(");
      int i = 0;
      Class[] arr$ = m.getParameterTypes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> p = arr$[i$];
         if (i++ > 0) {
            buf.append(", ");
         }

         buf.append(p.getName());
      }

      buf.append(")");
      return buf.toString();
   }

   public String toString() {
      return "VerboseReporter{suiteName=" + this.suiteName + '}';
   }

   private static enum Status {
      SUCCESS(1),
      FAILURE(2),
      SKIP(3),
      SUCCESS_PERCENTAGE_FAILURE(4),
      STARTED(16);

      private int status;

      private Status(int i) {
         this.status = i;
      }
   }
}
