package org.testng.reporters.jq;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.testng.ISuite;
import org.testng.reporters.XMLStringBuffer;

public class GroupPanel extends BaseMultiSuitePanel {
   public GroupPanel(Model model) {
      super(model);
   }

   public String getPrefix() {
      return "group-";
   }

   public String getHeader(ISuite suite) {
      return "Groups for " + suite.getName();
   }

   public String getContent(ISuite suite, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      List<String> sortedGroups = this.getModel().getGroups(suite.getName());
      Collections.sort(sortedGroups);
      Iterator i$ = sortedGroups.iterator();

      while(i$.hasNext()) {
         String group = (String)i$.next();
         xsb.push("div", "class", "test-group");
         xsb.addRequired("span", group, "class", "test-group-name");
         xsb.addEmptyElement("br");
         List<String> sortedMethods = this.getModel().getMethodsInGroup(group);
         Iterator i$ = sortedMethods.iterator();

         while(i$.hasNext()) {
            String method = (String)i$.next();
            xsb.push("div", "class", "method-in-group");
            xsb.addRequired("span", method, "class", "method-in-group-name");
            xsb.addEmptyElement("br");
            xsb.pop("div");
         }

         xsb.pop("div");
      }

      return xsb.toXML();
   }

   public String getNavigatorLink(ISuite suite) {
      return pluralize(this.getModel().getGroups(suite.getName()).size(), "group");
   }
}
