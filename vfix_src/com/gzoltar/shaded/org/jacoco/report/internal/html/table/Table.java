package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Styles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Table {
   private final List<Table.Column> columns = new ArrayList();
   private Comparator<ITableItem> defaultComparator;

   public void add(String header, String style, IColumnRenderer renderer, boolean defaultSorting) {
      this.columns.add(new Table.Column(this.columns.size(), header, style, renderer, defaultSorting));
      if (defaultSorting) {
         if (this.defaultComparator != null) {
            throw new IllegalStateException("Default sorting only allowed for one column.");
         }

         this.defaultComparator = renderer.getComparator();
      }

   }

   public void render(HTMLElement parent, List<? extends ITableItem> items, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
      List<? extends ITableItem> sortedItems = this.sort(items);
      HTMLElement table = parent.table("coverage");
      table.attr("id", "coveragetable");
      this.header(table, sortedItems, total);
      this.footer(table, total, resources, base);
      this.body(table, sortedItems, resources, base);
   }

   private void header(HTMLElement table, List<? extends ITableItem> items, ICoverageNode total) throws IOException {
      HTMLElement tr = table.thead().tr();
      Iterator i$ = this.columns.iterator();

      while(i$.hasNext()) {
         Table.Column c = (Table.Column)i$.next();
         c.init(tr, items, total);
      }

   }

   private void footer(HTMLElement table, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
      HTMLElement tr = table.tfoot().tr();
      Iterator i$ = this.columns.iterator();

      while(i$.hasNext()) {
         Table.Column c = (Table.Column)i$.next();
         c.footer(tr, total, resources, base);
      }

   }

   private void body(HTMLElement table, List<? extends ITableItem> items, Resources resources, ReportOutputFolder base) throws IOException {
      HTMLElement tbody = table.tbody();
      int idx = 0;

      for(Iterator i$ = items.iterator(); i$.hasNext(); ++idx) {
         ITableItem item = (ITableItem)i$.next();
         HTMLElement tr = tbody.tr();
         Iterator i$ = this.columns.iterator();

         while(i$.hasNext()) {
            Table.Column c = (Table.Column)i$.next();
            c.body(tr, idx, item, resources, base);
         }
      }

   }

   private List<? extends ITableItem> sort(List<? extends ITableItem> items) {
      if (this.defaultComparator != null) {
         List<ITableItem> result = new ArrayList(items);
         Collections.sort(result, this.defaultComparator);
         return result;
      } else {
         return items;
      }
   }

   private static class Column {
      private final char idprefix;
      private final String header;
      private final IColumnRenderer renderer;
      private final SortIndex<ITableItem> index;
      private final String style;
      private final String headerStyle;
      private boolean visible;

      Column(int idx, String header, String style, IColumnRenderer renderer, boolean defaultSorting) {
         this.idprefix = (char)(97 + idx);
         this.header = header;
         this.renderer = renderer;
         this.index = new SortIndex(renderer.getComparator());
         this.style = style;
         this.headerStyle = Styles.combine(defaultSorting ? "down" : null, "sortable", style);
      }

      void init(HTMLElement tr, List<? extends ITableItem> items, ICoverageNode total) throws IOException {
         this.visible = this.renderer.init(items, total);
         if (this.visible) {
            this.index.init(items);
            HTMLElement td = tr.td(this.headerStyle);
            td.attr("id", String.valueOf(this.idprefix));
            td.attr("onclick", "toggleSort(this)");
            td.text(this.header);
         }

      }

      void footer(HTMLElement tr, ICoverageNode total, Resources resources, ReportOutputFolder base) throws IOException {
         if (this.visible) {
            this.renderer.footer(tr.td(this.style), total, resources, base);
         }

      }

      void body(HTMLElement tr, int idx, ITableItem item, Resources resources, ReportOutputFolder base) throws IOException {
         if (this.visible) {
            HTMLElement td = tr.td(this.style);
            td.attr("id", this.idprefix + String.valueOf(this.index.getPosition(idx)));
            this.renderer.item(td, item, resources, base);
         }

      }
   }
}
