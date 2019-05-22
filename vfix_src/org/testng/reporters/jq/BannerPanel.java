package org.testng.reporters.jq;

import org.testng.reporters.XMLStringBuffer;

public class BannerPanel extends BasePanel {
   public BannerPanel(Model model) {
      super(model);
   }

   public void generate(XMLStringBuffer xsb) {
      xsb.push("div", "class", "top-banner-root");
      xsb.addRequired("span", "Test results", "class", "top-banner-title-font");
      xsb.addEmptyElement("br");
      int failedCount = this.getModel().getAllFailedResults().size();
      String testResult = failedCount > 0 ? ", " + pluralize(failedCount, "failed test") : "";
      String subTitle = pluralize(this.getModel().getSuites().size(), "suite") + testResult;
      xsb.addRequired("span", subTitle, "class", "top-banner-font-1");
      xsb.pop("div");
   }
}
