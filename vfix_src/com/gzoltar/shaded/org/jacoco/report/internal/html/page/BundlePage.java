package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import java.io.IOException;
import java.util.Iterator;

public class BundlePage extends TablePage<ICoverageNode> {
   private final ISourceFileLocator locator;
   private IBundleCoverage bundle;

   public BundlePage(IBundleCoverage bundle, ReportPage parent, ISourceFileLocator locator, ReportOutputFolder folder, IHTMLReportContext context) {
      super(bundle.getPlainCopy(), parent, folder, context);
      this.bundle = bundle;
      this.locator = locator;
   }

   public void render() throws IOException {
      this.renderPackages();
      super.render();
      this.bundle = null;
   }

   private void renderPackages() throws IOException {
      Iterator i$ = this.bundle.getPackages().iterator();

      while(i$.hasNext()) {
         IPackageCoverage p = (IPackageCoverage)i$.next();
         String packagename = p.getName();
         String foldername = packagename.length() == 0 ? "default" : packagename.replace('/', '.');
         PackagePage page = new PackagePage(p, this, this.locator, this.folder.subFolder(foldername), this.context);
         page.render();
         this.addItem(page);
      }

   }

   protected String getOnload() {
      return "initialSort(['breadcrumb', 'coveragetable'])";
   }

   protected String getFileName() {
      return "index.html";
   }
}
