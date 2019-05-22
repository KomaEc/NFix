package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.JaCoCo;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLDocument;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import java.io.IOException;

public abstract class ReportPage implements ILinkable {
   private final ReportPage parent;
   protected final ReportOutputFolder folder;
   protected final IHTMLReportContext context;

   protected ReportPage(ReportPage parent, ReportOutputFolder folder, IHTMLReportContext context) {
      this.parent = parent;
      this.context = context;
      this.folder = folder;
   }

   protected final boolean isRootPage() {
      return this.parent == null;
   }

   public void render() throws IOException {
      HTMLDocument doc = new HTMLDocument(this.folder.createFile(this.getFileName()), this.context.getOutputEncoding());
      doc.attr("lang", this.context.getLocale().getLanguage());
      this.head(doc.head());
      this.body(doc.body());
      doc.close();
   }

   protected void head(HTMLElement head) throws IOException {
      head.meta("Content-Type", "text/html;charset=UTF-8");
      head.link("stylesheet", this.context.getResources().getLink(this.folder, "report.css"), "text/css");
      head.link("shortcut icon", this.context.getResources().getLink(this.folder, "report.gif"), "image/gif");
      head.title().text(this.getLinkLabel());
   }

   private void body(HTMLElement body) throws IOException {
      body.attr("onload", this.getOnload());
      HTMLElement navigation = body.div("breadcrumb");
      navigation.attr("id", "breadcrumb");
      this.infoLinks(navigation.span("info"));
      this.breadcrumb(navigation, this.folder);
      body.h1().text(this.getLinkLabel());
      this.content(body);
      this.footer(body);
   }

   protected String getOnload() {
      return null;
   }

   protected void infoLinks(HTMLElement span) throws IOException {
      span.a(this.context.getSessionsPage(), this.folder);
   }

   private void breadcrumb(HTMLElement div, ReportOutputFolder base) throws IOException {
      breadcrumbParent(this.parent, div, base);
      div.span(this.getLinkStyle()).text(this.getLinkLabel());
   }

   private static void breadcrumbParent(ReportPage page, HTMLElement div, ReportOutputFolder base) throws IOException {
      if (page != null) {
         breadcrumbParent(page.parent, div, base);
         div.a((ILinkable)page, (ReportOutputFolder)base);
         div.text(" > ");
      }

   }

   private void footer(HTMLElement body) throws IOException {
      HTMLElement footer = body.div("footer");
      HTMLElement versioninfo = footer.span("right");
      versioninfo.text("Created with ");
      versioninfo.a(JaCoCo.HOMEURL).text("JaCoCo");
      versioninfo.text(" ").text(JaCoCo.VERSION);
      footer.text(this.context.getFooterText());
   }

   protected abstract String getFileName();

   protected abstract void content(HTMLElement var1) throws IOException;

   public final String getLink(ReportOutputFolder base) {
      return this.folder.getLink(base, this.getFileName());
   }
}
