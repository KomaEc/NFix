package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class LabelColumn implements IColumnRenderer {
   private static final Comparator<ITableItem> COMPARATOR = new Comparator<ITableItem>() {
      public int compare(ITableItem i1, ITableItem i2) {
         return i1.getLinkLabel().compareToIgnoreCase(i2.getLinkLabel());
      }
   };

   public boolean init(List<? extends ITableItem> items, ICoverageNode total) {
      return true;
   }

   public void footer(HTMLElement td, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
      td.text("Total");
   }

   public void item(HTMLElement td, ITableItem item, Resources resources, ReportOutputFolder base) throws IOException {
      td.a((ILinkable)item, (ReportOutputFolder)base);
   }

   public Comparator<ITableItem> getComparator() {
      return COMPARATOR;
   }
}
