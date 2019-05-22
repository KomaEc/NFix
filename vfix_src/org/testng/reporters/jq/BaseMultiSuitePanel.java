package org.testng.reporters.jq;

import java.util.Iterator;
import org.testng.ISuite;
import org.testng.reporters.XMLStringBuffer;

public abstract class BaseMultiSuitePanel extends BasePanel implements INavigatorPanel {
   abstract String getHeader(ISuite var1);

   abstract String getContent(ISuite var1, XMLStringBuffer var2);

   public BaseMultiSuitePanel(Model model) {
      super(model);
   }

   public void generate(XMLStringBuffer xsb) {
      Iterator i$ = this.getSuites().iterator();

      while(i$.hasNext()) {
         ISuite s = (ISuite)i$.next();
         xsb.push("div", "class", "panel", "panel-name", this.getPanelName(s));
         xsb.push("div", "class", "main-panel-header rounded-window-top");
         xsb.addOptional("span", this.getHeader(s), "class", "header-content");
         xsb.pop("div");
         xsb.push("div", "class", "main-panel-content rounded-window-bottom");
         xsb.addString(this.getContent(s, xsb));
         xsb.pop("div");
         xsb.pop("div");
      }

   }

   public String getClassName() {
      return null;
   }

   public String getPanelName(ISuite suite) {
      return this.getPrefix() + suiteToTag(suite);
   }
}
