package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.CounterComparator;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class BarColumn implements IColumnRenderer {
   private static final int WIDTH = 120;
   private final ICoverageNode.CounterEntity entity;
   private final NumberFormat integerFormat;
   private int max;
   private final Comparator<ITableItem> comparator;

   public BarColumn(ICoverageNode.CounterEntity entity, Locale locale) {
      this.entity = entity;
      this.integerFormat = DecimalFormat.getIntegerInstance(locale);
      this.comparator = new TableItemComparator(CounterComparator.MISSEDITEMS.reverse().on(entity).second(CounterComparator.TOTALITEMS.reverse().on(entity)));
   }

   public boolean init(List<? extends ITableItem> items, ICoverageNode total) {
      this.max = 0;
      Iterator i$ = items.iterator();

      while(i$.hasNext()) {
         ITableItem item = (ITableItem)i$.next();
         int count = item.getNode().getCounter(this.entity).getTotalCount();
         if (count > this.max) {
            this.max = count;
         }
      }

      return true;
   }

   public void footer(HTMLElement td, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
      ICounter counter = total.getCounter(this.entity);
      td.text(this.integerFormat.format((long)counter.getMissedCount())).text(" of ").text(this.integerFormat.format((long)counter.getTotalCount()));
   }

   public void item(HTMLElement td, ITableItem item, Resources resources, ReportOutputFolder base) throws IOException {
      if (this.max > 0) {
         ICounter counter = item.getNode().getCounter(this.entity);
         int missed = counter.getMissedCount();
         this.bar(td, missed, "redbar.gif", resources, base);
         int covered = counter.getCoveredCount();
         this.bar(td, covered, "greenbar.gif", resources, base);
      }

   }

   private void bar(HTMLElement td, int count, String image, Resources resources, ReportOutputFolder base) throws IOException {
      int width = count * 120 / this.max;
      if (width > 0) {
         td.img(resources.getLink(base, image), width, 10, this.integerFormat.format((long)count));
      }

   }

   public Comparator<ITableItem> getComparator() {
      return this.comparator;
   }
}
