package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.ITableItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TablePage<NodeType extends ICoverageNode> extends NodePage<NodeType> {
   private final List<ITableItem> items = new ArrayList();

   protected TablePage(NodeType node, ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context) {
      super(node, parent, folder, context);
   }

   public void addItem(ITableItem item) {
      this.items.add(item);
   }

   protected void head(HTMLElement head) throws IOException {
      super.head(head);
      head.script("text/javascript", this.context.getResources().getLink(this.folder, "sort.js"));
   }

   protected void content(HTMLElement body) throws IOException {
      this.context.getTable().render(body, this.items, this.getNode(), this.context.getResources(), this.folder);
      this.items.clear();
   }
}
