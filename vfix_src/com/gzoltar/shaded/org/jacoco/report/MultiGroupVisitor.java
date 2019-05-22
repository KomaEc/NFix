package com.gzoltar.shaded.org.jacoco.report;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class MultiGroupVisitor implements IReportGroupVisitor {
   private final List<? extends IReportGroupVisitor> visitors;

   MultiGroupVisitor(List<? extends IReportGroupVisitor> visitors) {
      this.visitors = visitors;
   }

   public void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
      Iterator i$ = this.visitors.iterator();

      while(i$.hasNext()) {
         IReportGroupVisitor v = (IReportGroupVisitor)i$.next();
         v.visitBundle(bundle, locator);
      }

   }

   public IReportGroupVisitor visitGroup(String name) throws IOException {
      List<IReportGroupVisitor> children = new ArrayList();
      Iterator i$ = this.visitors.iterator();

      while(i$.hasNext()) {
         IReportGroupVisitor v = (IReportGroupVisitor)i$.next();
         children.add(v.visitGroup(name));
      }

      return new MultiGroupVisitor(children);
   }
}
