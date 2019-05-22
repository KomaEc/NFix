package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.CounterComparator;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PercentageColumn implements IColumnRenderer {
   private final ICoverageNode.CounterEntity entity;
   private final NumberFormat percentageFormat;
   private final Comparator<ITableItem> comparator;

   public PercentageColumn(ICoverageNode.CounterEntity entity, Locale locale) {
      this.entity = entity;
      this.percentageFormat = NumberFormat.getPercentInstance(locale);
      this.comparator = new TableItemComparator(CounterComparator.MISSEDRATIO.on(entity));
   }

   public boolean init(List<? extends ITableItem> items, ICoverageNode total) {
      return true;
   }

   public void footer(HTMLElement td, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
      this.cell(td, total);
   }

   public void item(HTMLElement td, ITableItem item, Resources resources, ReportOutputFolder base) throws IOException {
      this.cell(td, item.getNode());
   }

   private void cell(HTMLElement td, ICoverageNode node) throws IOException {
      ICounter counter = node.getCounter(this.entity);
      int total = counter.getTotalCount();
      if (total == 0) {
         td.text("n/a");
      } else {
         td.text(this.format(counter.getCoveredRatio()));
      }

   }

   private String format(double ratio) {
      return this.percentageFormat.format(BigDecimal.valueOf(ratio).setScale(2, RoundingMode.FLOOR));
   }

   public Comparator<ITableItem> getComparator() {
      return this.comparator;
   }
}
