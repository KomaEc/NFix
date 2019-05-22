package com.gzoltar.shaded.org.jacoco.report.internal.html.page;

import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import com.gzoltar.shaded.org.jacoco.report.internal.html.IHTMLReportContext;
import com.gzoltar.shaded.org.jacoco.report.internal.html.ILinkable;
import java.io.IOException;
import java.util.Iterator;

public class ClassPage extends TablePage<IClassCoverage> {
   private final ILinkable sourcePage;

   public ClassPage(IClassCoverage classNode, ReportPage parent, ILinkable sourcePage, ReportOutputFolder folder, IHTMLReportContext context) {
      super(classNode, parent, folder, context);
      this.sourcePage = sourcePage;
      context.getIndexUpdate().addClass(this, classNode.getId());
   }

   protected String getOnload() {
      return "initialSort(['breadcrumb'])";
   }

   public void render() throws IOException {
      Iterator i$ = ((IClassCoverage)this.getNode()).getMethods().iterator();

      while(i$.hasNext()) {
         IMethodCoverage m = (IMethodCoverage)i$.next();
         String label = this.context.getLanguageNames().getMethodName(((IClassCoverage)this.getNode()).getName(), m.getName(), m.getDesc(), m.getSignature());
         this.addItem(new MethodItem(m, label, this.sourcePage));
      }

      super.render();
   }

   protected String getFileName() {
      String vmname = ((IClassCoverage)this.getNode()).getName();
      int pos = vmname.lastIndexOf(47);
      String shortname = pos == -1 ? vmname : vmname.substring(pos + 1);
      return shortname + ".html";
   }

   public String getLinkLabel() {
      return this.context.getLanguageNames().getClassName(((IClassCoverage)this.getNode()).getName(), ((IClassCoverage)this.getNode()).getSignature(), ((IClassCoverage)this.getNode()).getSuperName(), ((IClassCoverage)this.getNode()).getInterfaceNames());
   }
}
