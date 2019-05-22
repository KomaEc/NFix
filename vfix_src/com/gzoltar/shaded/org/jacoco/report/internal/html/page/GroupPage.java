package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;

public class GroupPage extends TablePage<ICoverageNode> {
   public GroupPage(ICoverageNode node, ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context) {
      super(node, parent, folder, context);
   }

   protected String getOnload() {
      return "initialSort(['breadcrumb', 'coveragetable'])";
   }

   protected String getFileName() {
      return "index.html";
   }
}
