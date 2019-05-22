package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.ITableItem;

public abstract class NodePage<NodeType extends ICoverageNode> extends ReportPage implements ITableItem {
   private final NodeType node;

   protected NodePage(NodeType node, ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context) {
      super(parent, folder, context);
      this.node = node;
   }

   public String getLinkStyle() {
      return this.isRootPage() ? "el_report" : Resources.getElementStyle(this.node.getElementType());
   }

   public String getLinkLabel() {
      return this.node.getName();
   }

   public NodeType getNode() {
      return this.node;
   }
}
