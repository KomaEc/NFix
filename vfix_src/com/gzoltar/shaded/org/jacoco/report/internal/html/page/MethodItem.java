package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.ITableItem;

final class MethodItem implements ITableItem {
   private final IMethodCoverage node;
   private final String label;
   private final ILinkable sourcePage;

   MethodItem(IMethodCoverage node, String label, ILinkable sourcePage) {
      this.node = node;
      this.label = label;
      this.sourcePage = sourcePage;
   }

   public String getLinkLabel() {
      return this.label;
   }

   public String getLinkStyle() {
      return "el_method";
   }

   public String getLink(ReportOutputFolder base) {
      if (this.sourcePage == null) {
         return null;
      } else {
         String link = this.sourcePage.getLink(base);
         int first = this.node.getFirstLine();
         return first != -1 ? link + "#L" + first : link;
      }
   }

   public ICoverageNode getNode() {
      return this.node;
   }
}
