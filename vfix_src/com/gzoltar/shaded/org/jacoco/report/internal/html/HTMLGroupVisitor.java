package com.gzoltar.shaded.org.jacoco.report.internal.html;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.internal.AbstractGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.BundlePage;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.GroupPage;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.NodePage;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.ReportPage;
import java.io.IOException;

public class HTMLGroupVisitor extends AbstractGroupVisitor {
   private final ReportOutputFolder folder;
   private final IHTMLReportContext context;
   private final GroupPage page;

   public HTMLGroupVisitor(ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context, String name) {
      super(name);
      this.folder = folder;
      this.context = context;
      this.page = new GroupPage(this.total, parent, folder, context);
   }

   public NodePage<ICoverageNode> getPage() {
      return this.page;
   }

   protected void handleBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
      BundlePage bundlepage = new BundlePage(bundle, this.page, locator, this.folder.subFolder(bundle.getName()), this.context);
      bundlepage.render();
      this.page.addItem(bundlepage);
   }

   protected AbstractGroupVisitor handleGroup(String name) throws IOException {
      HTMLGroupVisitor handler = new HTMLGroupVisitor(this.page, this.folder.subFolder(name), this.context, name);
      this.page.addItem(handler.getPage());
      return handler;
   }

   protected void handleEnd() throws IOException {
      this.page.render();
   }
}
