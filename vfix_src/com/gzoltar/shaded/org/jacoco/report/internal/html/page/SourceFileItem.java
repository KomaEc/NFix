package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.ITableItem;

final class SourceFileItem implements ITableItem {
   private final ICoverageNode node;

   SourceFileItem(ISourceFileCoverage node) {
      this.node = node;
   }

   public String getLinkLabel() {
      return this.node.getName();
   }

   public String getLinkStyle() {
      return "el_source";
   }

   public String getLink(ReportOutputFolder base) {
      return null;
   }

   public ICoverageNode getNode() {
      return this.node;
   }
}
