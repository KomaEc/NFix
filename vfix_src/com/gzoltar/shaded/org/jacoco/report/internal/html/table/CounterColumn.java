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

public abstract class CounterColumn implements IColumnRenderer {
   private final ICoverageNode.CounterEntity entity;
   private final NumberFormat integerFormat;
   private final Comparator<ITableItem> comparator;

   public static CounterColumn newTotal(ICoverageNode.CounterEntity entity, Locale locale) {
      return new CounterColumn(entity, locale, CounterComparator.TOTALITEMS.reverse().on(entity)) {
         protected int getValue(ICounter counter) {
            return counter.getTotalCount();
         }
      };
   }

   public static CounterColumn newMissed(ICoverageNode.CounterEntity entity, Locale locale) {
      return new CounterColumn(entity, locale, CounterComparator.MISSEDITEMS.reverse().on(entity)) {
         protected int getValue(ICounter counter) {
            return counter.getMissedCount();
         }
      };
   }

   public static CounterColumn newCovered(ICoverageNode.CounterEntity entity, Locale locale) {
      return new CounterColumn(entity, locale, CounterComparator.COVEREDITEMS.reverse().on(entity)) {
         protected int getValue(ICounter counter) {
            return counter.getCoveredCount();
         }
      };
   }

   protected CounterColumn(ICoverageNode.CounterEntity entity, Locale locale, Comparator<ICoverageNode> comparator) {
      this.entity = entity;
      this.integerFormat = DecimalFormat.getIntegerInstance(locale);
      this.comparator = new TableItemComparator(comparator);
   }

   public boolean init(List<? extends ITableItem> items, ICoverageNode total) {
      Iterator i$ = items.iterator();

      ITableItem i;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         i = (ITableItem)i$.next();
      } while(i.getNode().getCounter(this.entity).getTotalCount() <= 0);

      return true;
   }

   public void footer(HTMLElement td, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
      this.cell(td, total);
   }

   public void item(HTMLElement td, ITableItem item, Resources resources, ReportOutputFolder base) throws IOException {
      this.cell(td, item.getNode());
   }

   private void cell(HTMLElement td, ICoverageNode node) throws IOException {
      int value = this.getValue(node.getCounter(this.entity));
      td.text(this.integerFormat.format((long)value));
   }

   public Comparator<ITableItem> getComparator() {
      return this.comparator;
   }

   protected abstract int getValue(ICounter var1);
}
