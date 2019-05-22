package org.testng.reporters.jq;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.collections.Lists;
import org.testng.reporters.XMLStringBuffer;

public class NavigatorPanel extends BasePanel {
   private List<INavigatorPanel> m_panels;

   public NavigatorPanel(Model model, List<INavigatorPanel> panels) {
      super(model);
      this.m_panels = panels;
   }

   public void generate(XMLStringBuffer main) {
      main.push("div", "class", "navigator-root");
      main.push("div", "class", "navigator-suite-header");
      main.addRequired("span", "All suites");
      main.push("a", "class", "collapse-all-link", "href", "#", "title", "Collapse/expand all the suites");
      main.push("img", "src", "collapseall.gif", "class", "collapse-all-icon");
      main.pop("img");
      main.pop("a");
      main.pop("div");
      Iterator i$ = this.getSuites().iterator();

      while(true) {
         ISuite suite;
         do {
            if (!i$.hasNext()) {
               main.pop("div");
               return;
            }

            suite = (ISuite)i$.next();
         } while(suite.getResults().size() == 0);

         String suiteName = "suite-" + suiteToTag(suite);
         XMLStringBuffer header = new XMLStringBuffer(main.getCurrentIndent());
         Map<String, ISuiteResult> results = suite.getResults();
         int failed = 0;
         int skipped = 0;
         int passed = 0;

         ITestContext context;
         for(Iterator i$ = results.values().iterator(); i$.hasNext(); passed += context.getPassedTests().size()) {
            ISuiteResult result = (ISuiteResult)i$.next();
            context = result.getTestContext();
            failed += context.getFailedTests().size();
            skipped += context.getSkippedTests().size();
         }

         header.push("div", "class", "suite");
         header.push("div", "class", "rounded-window");
         header.push("div", "class", "suite-header light-rounded-window-top");
         header.push("a", "href", "#", "panel-name", suiteName, "class", "navigator-link");
         header.addOptional("span", suite.getName(), "class", "suite-name border-" + this.getModel().getStatusForSuite(suite.getName()));
         header.pop("a");
         header.pop("div");
         header.push("div", "class", "navigator-suite-content");
         this.generateInfo(header, suite);
         this.generateResult(header, failed, skipped, passed, suite, suiteName);
         header.pop("ul");
         header.pop("div");
         header.pop("div");
         header.pop("div");
         header.pop("div");
         header.pop("div");
         main.addString(header.toXML());
      }
   }

   private void generateResult(XMLStringBuffer header, int failed, int skipped, int passed, ISuite suite, String suiteName) {
      header.push("div", "class", "result-section");
      header.push("div", "class", "suite-section-title");
      header.addRequired("span", "Results");
      header.pop("div");
      int total = failed + skipped + passed;
      String stats = String.format("%s, %s %s %s", pluralize(total, "method"), maybe(failed, "failed", ", "), maybe(skipped, "skipped", ", "), maybe(passed, "passed", ""));
      header.push("div", "class", "suite-section-content");
      header.push("ul");
      header.push("li");
      header.addOptional("span", stats, "class", "method-stats");
      header.pop("li");
      this.generateMethodList("Failed methods", new NavigatorPanel.ResultsByStatus(suite, "failed", 2), suiteName, header);
      this.generateMethodList("Skipped methods", new NavigatorPanel.ResultsByStatus(suite, "skipped", 3), suiteName, header);
      this.generateMethodList("Passed methods", new NavigatorPanel.ResultsByStatus(suite, "passed", 1), suiteName, header);
   }

   private void generateInfo(XMLStringBuffer header, ISuite suite) {
      header.push("div", "class", "suite-section-title");
      header.addRequired("span", "Info");
      header.pop("div");
      header.push("div", "class", "suite-section-content");
      header.push("ul");
      Iterator i$ = this.m_panels.iterator();

      while(i$.hasNext()) {
         INavigatorPanel panel = (INavigatorPanel)i$.next();
         this.addLinkTo(header, panel, suite);
      }

      header.pop("ul");
      header.pop("div");
   }

   private void addLinkTo(XMLStringBuffer header, INavigatorPanel panel, ISuite suite) {
      String text = panel.getNavigatorLink(suite);
      header.push("li");
      header.push("a", "href", "#", "panel-name", panel.getPanelName(suite), "class", "navigator-link ");
      String className = panel.getClassName();
      if (className != null) {
         header.addOptional("span", text, "class", className);
      } else {
         header.addOptional("span", text);
      }

      header.pop("a");
      header.pop("li");
   }

   private static String maybe(int count, String s, String sep) {
      return count > 0 ? count + " " + s + sep : "";
   }

   private List<ITestResult> getMethodsByStatus(ISuite suite, int status) {
      List<ITestResult> result = Lists.newArrayList();
      List<ITestResult> testResults = this.getModel().getTestResults(suite);
      Iterator i$ = testResults.iterator();

      while(i$.hasNext()) {
         ITestResult tr = (ITestResult)i$.next();
         if (tr.getStatus() == status) {
            result.add(tr);
         }
      }

      Collections.sort(result, ResultsByClass.METHOD_NAME_COMPARATOR);
      return result;
   }

   private void generateMethodList(String name, NavigatorPanel.IResultProvider provider, String suiteName, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      String type = provider.getType();
      String image = Model.getImage(type);
      xsb.push("li");
      xsb.addRequired("span", name, "class", "method-list-title " + type);
      xsb.push("span", "class", "show-or-hide-methods " + type);
      xsb.addRequired("a", " (hide)", "href", "#", "class", "hide-methods " + type + " " + suiteName, "panel-name", suiteName);
      xsb.addRequired("a", " (show)", "href", "#", "class", "show-methods " + type + " " + suiteName, "panel-name", suiteName);
      xsb.pop("span");
      xsb.push("div", "class", "method-list-content " + type + " " + suiteName);
      int count = 0;
      List<ITestResult> testResults = provider.getResults();
      if (testResults != null) {
         Collections.sort(testResults, ResultsByClass.METHOD_NAME_COMPARATOR);

         for(Iterator i$ = testResults.iterator(); i$.hasNext(); ++count) {
            ITestResult tr = (ITestResult)i$.next();
            String testName = Model.getTestResultName(tr);
            xsb.push("span");
            xsb.addEmptyElement("img", "src", image, "width", "3%");
            xsb.addRequired("a", testName, "href", "#", "hash-for-method", this.getModel().getTag(tr), "panel-name", suiteName, "title", tr.getTestClass().getName(), "class", "method navigator-link");
            xsb.pop("span");
            xsb.addEmptyElement("br");
         }
      }

      xsb.pop("div");
      xsb.pop("li");
      if (count > 0) {
         main.addString(xsb.toXML());
      }

   }

   private class ResultsByStatus extends NavigatorPanel.BaseResultProvider {
      private final int m_status;

      public ResultsByStatus(ISuite suite, String type, int status) {
         super(suite, type);
         this.m_status = status;
      }

      public List<ITestResult> getResults() {
         return NavigatorPanel.this.getMethodsByStatus(this.m_suite, this.m_status);
      }
   }

   private abstract static class BaseResultProvider implements NavigatorPanel.IResultProvider {
      protected ISuite m_suite;
      protected String m_type;

      public BaseResultProvider(ISuite suite, String type) {
         this.m_suite = suite;
         this.m_type = type;
      }

      public String getType() {
         return this.m_type;
      }
   }

   private interface IResultProvider {
      List<ITestResult> getResults();

      String getType();
   }
}
