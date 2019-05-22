package org.testng.reporters.jq;

import java.util.Iterator;
import java.util.List;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.reporters.XMLStringBuffer;

public class ReporterPanel extends BaseMultiSuitePanel {
   public ReporterPanel(Model model) {
      super(model);
   }

   public String getPrefix() {
      return "reporter-";
   }

   public String getHeader(ISuite suite) {
      return "Reporter output for " + suite.getName();
   }

   public String getContent(ISuite suite, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      Iterator i$ = this.getModel().getAllTestResults(suite).iterator();

      while(true) {
         ITestResult tr;
         List lines;
         do {
            if (!i$.hasNext()) {
               return xsb.toXML();
            }

            tr = (ITestResult)i$.next();
            lines = Reporter.getOutput(tr);
         } while(lines.isEmpty());

         xsb.push("div", "class", "reporter-method-div");
         xsb.addRequired("span", Model.getTestResultName(tr), "class", "reporter-method-name");
         xsb.push("div", "class", "reporter-method-output-div");
         Iterator i$ = lines.iterator();

         while(i$.hasNext()) {
            String output = (String)i$.next();
            xsb.addRequired("span", output, "class", "reporter-method-output");
         }

         xsb.pop("div");
         xsb.pop("div");
      }
   }

   public String getNavigatorLink(ISuite suite) {
      return "Reporter output";
   }
}
