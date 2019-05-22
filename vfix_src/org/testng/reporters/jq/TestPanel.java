package org.testng.reporters.jq;

import java.util.Iterator;
import org.testng.ISuite;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.XmlTest;

public class TestPanel extends BaseMultiSuitePanel {
   public TestPanel(Model model) {
      super(model);
   }

   public String getPrefix() {
      return "testlist-";
   }

   public String getHeader(ISuite suite) {
      return "Tests for " + suite.getName();
   }

   public String getContent(ISuite suite, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      xsb.push("ul");
      Iterator i$ = suite.getXmlSuite().getTests().iterator();

      while(i$.hasNext()) {
         XmlTest test = (XmlTest)i$.next();
         xsb.push("li");
         int count = test.getXmlClasses().size();
         String name = test.getName() + " (" + pluralize(count, "class") + ")";
         xsb.addRequired("span", name, "class", "test-name");
         xsb.pop("li");
      }

      xsb.pop("ul");
      return xsb.toXML();
   }

   public String getNavigatorLink(ISuite suite) {
      return pluralize(suite.getXmlSuite().getTests().size(), "test");
   }

   public String getClassName() {
      return "test-stats";
   }
}
