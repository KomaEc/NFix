package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import java.util.Comparator;

class TableItemComparator implements Comparator<ITableItem> {
   private final Comparator<ICoverageNode> comparator;

   TableItemComparator(Comparator<ICoverageNode> comparator) {
      this.comparator = comparator;
   }

   public int compare(ITableItem i1, ITableItem i2) {
      return this.comparator.compare(i1.getNode(), i2.getNode());
   }
}
