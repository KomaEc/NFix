package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import java.io.IOException;
import java.util.Iterator;

public class PackagePage extends TablePage<IPackageCoverage> {
   private final PackageSourcePage packageSourcePage;
   private final boolean sourceCoverageExists;

   public PackagePage(IPackageCoverage node, ReportPage parent, ISourceFileLocator locator, ReportOutputFolder folder, IHTMLReportContext context) {
      super(node, parent, folder, context);
      this.packageSourcePage = new PackageSourcePage(node, parent, locator, folder, context, this);
      this.sourceCoverageExists = !node.getSourceFiles().isEmpty();
   }

   public void render() throws IOException {
      if (this.sourceCoverageExists) {
         this.packageSourcePage.render();
      }

      this.renderClasses();
      super.render();
   }

   private void renderClasses() throws IOException {
      Iterator i$ = ((IPackageCoverage)this.getNode()).getClasses().iterator();

      while(i$.hasNext()) {
         IClassCoverage c = (IClassCoverage)i$.next();
         ILinkable sourceFilePage = this.packageSourcePage.getSourceFilePage(c.getSourceFileName());
         ClassPage page = new ClassPage(c, this, sourceFilePage, this.folder, this.context);
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

   public String getLinkLabel() {
      return this.context.getLanguageNames().getPackageName(((IPackageCoverage)this.getNode()).getName());
   }

   protected void infoLinks(HTMLElement span) throws IOException {
      if (this.sourceCoverageExists) {
         String link = this.packageSourcePage.getLink(this.folder);
         span.a(link, "el_source").text("Source Files");
      }

      super.infoLinks(span);
   }
}
