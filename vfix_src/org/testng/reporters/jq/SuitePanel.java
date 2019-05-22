package org.testng.reporters.jq;

import java.util.Iterator;
import java.util.List;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.internal.Utils;
import org.testng.reporters.XMLStringBuffer;
import org.testng.util.Strings;

public class SuitePanel extends BasePanel {
   private static final String PASSED = "passed";
   private static final String SKIPPED = "skipped";
   private static final String FAILED = "failed";

   public SuitePanel(Model model) {
      super(model);
   }

   public void generate(XMLStringBuffer xsb) {
      Iterator i$ = this.getSuites().iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         this.generateSuitePanel(suite, xsb);
      }

   }

   private void generateSuitePanel(ISuite suite, XMLStringBuffer xsb) {
      String divName = suiteToTag(suite);
      xsb.push("div", "class", "panel " + divName, "panel-name", "suite-" + divName);
      String[] statuses = new String[]{"failed", "skipped", "passed"};
      ResultsByClass[] results = new ResultsByClass[]{this.getModel().getFailedResultsByClass(suite), this.getModel().getSkippedResultsByClass(suite), this.getModel().getPassedResultsByClass(suite)};

      for(int i = 0; i < results.length; ++i) {
         ResultsByClass byClass = results[i];
         Iterator i$ = byClass.getClasses().iterator();

         while(i$.hasNext()) {
            Class<?> c = (Class)i$.next();
            this.generateClassPanel(c, byClass.getResults(c), xsb, statuses[i], suite);
         }
      }

      xsb.pop("div");
   }

   private void generateClassPanel(Class c, List<ITestResult> results, XMLStringBuffer xsb, String status, ISuite suite) {
      xsb.push("div", "class", "suite-" + suiteToTag(suite) + "-class-" + status);
      xsb.push("div", "class", "main-panel-header rounded-window-top");
      xsb.addEmptyElement("img", "src", Model.getImage(status));
      xsb.addOptional("span", c.getName(), "class", "class-name");
      xsb.pop("div");
      xsb.push("div", "class", "main-panel-content rounded-window-bottom");
      Iterator i$ = results.iterator();

      while(i$.hasNext()) {
         ITestResult tr = (ITestResult)i$.next();
         this.generateMethod(tr, xsb);
      }

      xsb.pop("div");
      xsb.pop("div");
   }

   private void generateMethod(ITestResult tr, XMLStringBuffer xsb) {
      xsb.push("div", "class", "method");
      xsb.push("div", "class", "method-content");
      xsb.push("a", "name", Model.getTestResultName(tr));
      xsb.pop("a");
      xsb.addOptional("span", tr.getMethod().getMethodName(), "class", "method-name");
      StringBuilder sb;
      if (tr.getParameters().length > 0) {
         sb = new StringBuilder();
         boolean first = true;
         Object[] arr$ = tr.getParameters();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object p = arr$[i$];
            if (!first) {
               sb.append(", ");
            }

            first = false;
            sb.append(Utils.toString(p));
         }

         xsb.addOptional("span", "(" + sb.toString() + ")", "class", "parameters");
      }

      if (tr.getStatus() != 1 && tr.getThrowable() != null) {
         sb = new StringBuilder();
         sb.append(Utils.stackTrace(tr.getThrowable(), true)[0]);
         xsb.addOptional("div", sb.toString() + "\n", "class", "stack-trace");
      }

      String description = tr.getMethod().getDescription();
      if (!Strings.isNullOrEmpty(description)) {
         xsb.push("em");
         xsb.addString("(" + description + ")");
         xsb.pop("em");
      }

      xsb.pop("div");
      xsb.pop("div");
   }
}
