package org.testng.reporters.jq;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.testng.IInvokedMethod;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.reporters.XMLStringBuffer;

public class ChronologicalPanel extends BaseMultiSuitePanel {
   public ChronologicalPanel(Model model) {
      super(model);
   }

   public String getPrefix() {
      return "chronological-";
   }

   public String getHeader(ISuite suite) {
      return "Methods in chronological order";
   }

   public String getContent(ISuite suite, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
      Collections.sort(invokedMethods, new Comparator<IInvokedMethod>() {
         public int compare(IInvokedMethod arg0, IInvokedMethod arg1) {
            return (int)(arg0.getTestResult().getStartMillis() - arg1.getTestResult().getStartMillis());
         }
      });
      String currentClass = "";
      long start = 0L;
      Iterator i$ = invokedMethods.iterator();

      while(i$.hasNext()) {
         IInvokedMethod im = (IInvokedMethod)i$.next();
         ITestNGMethod m = im.getTestMethod();
         String cls = "test-method";
         if (m.isBeforeSuiteConfiguration()) {
            cls = "configuration-suite before";
         } else if (m.isAfterSuiteConfiguration()) {
            cls = "configuration-suite after";
         } else if (m.isBeforeTestConfiguration()) {
            cls = "configuration-test before";
         } else if (m.isAfterTestConfiguration()) {
            cls = "configuration-test after";
         } else if (m.isBeforeClassConfiguration()) {
            cls = "configuration-class before";
         } else if (m.isAfterClassConfiguration()) {
            cls = "configuration-class after";
         } else if (m.isBeforeMethodConfiguration()) {
            cls = "configuration-method before";
         } else if (m.isAfterMethodConfiguration()) {
            cls = "configuration-method after";
         }

         ITestResult tr = im.getTestResult();
         String methodName = Model.getTestResultName(tr);
         if (!m.getTestClass().getName().equals(currentClass)) {
            if (!"".equals(currentClass)) {
               xsb.pop("div");
            }

            xsb.push("div", "class", "chronological-class");
            xsb.addRequired("div", m.getTestClass().getName(), "class", "chronological-class-name");
            currentClass = m.getTestClass().getName();
         }

         xsb.push("div", "class", cls);
         if (tr.getStatus() == 2) {
            xsb.push("img", "src", Model.getImage("failed"));
            xsb.pop("img");
         }

         xsb.addRequired("span", methodName, "class", "method-name");
         if (start == 0L) {
            start = tr.getStartMillis();
         }

         xsb.addRequired("span", Long.toString(tr.getStartMillis() - start) + " ms", "class", "method-start");
         xsb.pop("div");
      }

      return xsb.toXML();
   }

   public String getNavigatorLink(ISuite suite) {
      return "Chronological view";
   }
}
