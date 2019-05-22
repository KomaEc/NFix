package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PackageSourcePage extends TablePage<IPackageCoverage> {
   private final ISourceFileLocator locator;
   private final Map<String, ILinkable> sourceFilePages;
   private final ILinkable packagePage;

   public PackageSourcePage(IPackageCoverage node, ReportPage parent, ISourceFileLocator locator, ReportOutputFolder folder, IHTMLReportContext context, ILinkable packagePage) {
      super(node, parent, folder, context);
      this.locator = locator;
      this.packagePage = packagePage;
      this.sourceFilePages = new HashMap();
   }

   public void render() throws IOException {
      this.renderSourceFilePages();
      super.render();
   }

   ILinkable getSourceFilePage(String name) {
      return (ILinkable)this.sourceFilePages.get(name);
   }

   private final void renderSourceFilePages() throws IOException {
      String packagename = ((IPackageCoverage)this.getNode()).getName();
      Iterator i$ = ((IPackageCoverage)this.getNode()).getSourceFiles().iterator();

      while(i$.hasNext()) {
         ISourceFileCoverage s = (ISourceFileCoverage)i$.next();
         String sourcename = s.getName();
         Reader reader = this.locator.getSourceFile(packagename, sourcename);
         if (reader == null) {
            this.addItem(new SourceFileItem(s));
         } else {
            SourceFilePage sourcePage = new SourceFilePage(s, reader, this.locator.getTabWidth(), this, this.folder, this.context);
            sourcePage.render();
            this.sourceFilePages.put(sourcename, sourcePage);
            this.addItem(sourcePage);
         }
      }

   }

   protected String getOnload() {
      return "initialSort(['breadcrumb', 'coveragetable'])";
   }

   protected String getFileName() {
      return "index.source.html";
   }

   public String getLinkLabel() {
      return this.context.getLanguageNames().getPackageName(((IPackageCoverage)this.getNode()).getName());
   }

   protected void infoLinks(HTMLElement span) throws IOException {
      String link = this.packagePage.getLink(this.folder);
      span.a(link, "el_class").text("Classes");
      super.infoLinks(span);
   }
}
