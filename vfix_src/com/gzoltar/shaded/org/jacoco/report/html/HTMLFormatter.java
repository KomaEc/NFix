package com.gzoltar.shaded.org.jacoco.report.html;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import com.gzoltar.shaded.org.jacoco.report.IMultiReportOutput;
import com.gzoltar.shaded.org.jacoco.report.IReportGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.IReportVisitor;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.JavaNames;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.HTMLGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import com.gzoltar.shaded.org.jacoco.report.internal.html.index.ElementIndex;
import com.gzoltar.shaded.org.jacoco.report.internal.html.index.IIndexUpdate;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.BundlePage;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.ReportPage;
import com.gzoltar.shaded.org.jacoco.report.internal.html.page.SessionsPage;
import com.gzoltar.shaded.org.jacoco.report.internal.html.resources.Resources;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.BarColumn;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.CounterColumn;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.LabelColumn;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.PercentageColumn;
import com.gzoltar.shaded.org.jacoco.report.internal.html.table.Table;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class HTMLFormatter implements IHTMLReportContext {
   private ILanguageNames languageNames = new JavaNames();
   private Locale locale = Locale.getDefault();
   private String footerText = "";
   private String outputEncoding = "UTF-8";
   private Resources resources;
   private ElementIndex index;
   private SessionsPage sessionsPage;
   private Table table;

   public void setLanguageNames(ILanguageNames languageNames) {
      this.languageNames = languageNames;
   }

   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   public void setFooterText(String footerText) {
      this.footerText = footerText;
   }

   public void setOutputEncoding(String outputEncoding) {
      this.outputEncoding = outputEncoding;
   }

   public ILanguageNames getLanguageNames() {
      return this.languageNames;
   }

   public Resources getResources() {
      return this.resources;
   }

   public Table getTable() {
      if (this.table == null) {
         this.table = this.createTable();
      }

      return this.table;
   }

   private Table createTable() {
      Table t = new Table();
      t.add("Element", (String)null, new LabelColumn(), false);
      t.add("Missed Instructions", "bar", new BarColumn(ICoverageNode.CounterEntity.INSTRUCTION, this.locale), true);
      t.add("Cov.", "ctr2", new PercentageColumn(ICoverageNode.CounterEntity.INSTRUCTION, this.locale), false);
      t.add("Missed Branches", "bar", new BarColumn(ICoverageNode.CounterEntity.BRANCH, this.locale), false);
      t.add("Cov.", "ctr2", new PercentageColumn(ICoverageNode.CounterEntity.BRANCH, this.locale), false);
      this.addMissedTotalColumns(t, "Cxty", ICoverageNode.CounterEntity.COMPLEXITY);
      this.addMissedTotalColumns(t, "Lines", ICoverageNode.CounterEntity.LINE);
      this.addMissedTotalColumns(t, "Methods", ICoverageNode.CounterEntity.METHOD);
      this.addMissedTotalColumns(t, "Classes", ICoverageNode.CounterEntity.CLASS);
      return t;
   }

   private void addMissedTotalColumns(Table table, String label, ICoverageNode.CounterEntity entity) {
      table.add("Missed", "ctr1", CounterColumn.newMissed(entity, this.locale), false);
      table.add(label, "ctr2", CounterColumn.newTotal(entity, this.locale), false);
   }

   public String getFooterText() {
      return this.footerText;
   }

   public ILinkable getSessionsPage() {
      return this.sessionsPage;
   }

   public String getOutputEncoding() {
      return this.outputEncoding;
   }

   public IIndexUpdate getIndexUpdate() {
      return this.index;
   }

   public Locale getLocale() {
      return this.locale;
   }

   public IReportVisitor createVisitor(final IMultiReportOutput output) throws IOException {
      final ReportOutputFolder root = new ReportOutputFolder(output);
      this.resources = new Resources(root);
      this.resources.copyResources();
      this.index = new ElementIndex(root);
      return new IReportVisitor() {
         private List<SessionInfo> sessionInfos;
         private Collection<ExecutionData> executionData;
         private HTMLGroupVisitor groupHandler;

         public void visitInfo(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData) throws IOException {
            this.sessionInfos = sessionInfos;
            this.executionData = executionData;
         }

         public void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
            BundlePage page = new BundlePage(bundle, (ReportPage)null, locator, root, HTMLFormatter.this);
            this.createSessionsPage(page);
            page.render();
         }

         public IReportGroupVisitor visitGroup(String name) throws IOException {
            this.groupHandler = new HTMLGroupVisitor((ReportPage)null, root, HTMLFormatter.this, name);
            this.createSessionsPage(this.groupHandler.getPage());
            return this.groupHandler;
         }

         private void createSessionsPage(ReportPage rootpage) {
            HTMLFormatter.this.sessionsPage = new SessionsPage(this.sessionInfos, this.executionData, HTMLFormatter.this.index, rootpage, root, HTMLFormatter.this);
         }

         public void visitEnd() throws IOException {
            if (this.groupHandler != null) {
               this.groupHandler.visitEnd();
            }

            HTMLFormatter.this.sessionsPage.render();
            output.close();
         }
      };
   }
}
