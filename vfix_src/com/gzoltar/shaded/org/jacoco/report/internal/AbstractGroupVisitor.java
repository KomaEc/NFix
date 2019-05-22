package com.gzoltar.shaded.org.jacoco.report.internal;

import com.gzoltar.shaded.org.jacoco.core.analysis.CoverageNodeImpl;
import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.IReportGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import java.io.IOException;

public abstract class AbstractGroupVisitor implements IReportGroupVisitor {
   protected final CoverageNodeImpl total;
   private AbstractGroupVisitor lastChild;

   protected AbstractGroupVisitor(String name) {
      this.total = new CoverageNodeImpl(ICoverageNode.ElementType.GROUP, name);
   }

   public final void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
      this.finalizeLastChild();
      this.total.increment((ICoverageNode)bundle);
      this.handleBundle(bundle, locator);
   }

   protected abstract void handleBundle(IBundleCoverage var1, ISourceFileLocator var2) throws IOException;

   public final IReportGroupVisitor visitGroup(String name) throws IOException {
      this.finalizeLastChild();
      this.lastChild = this.handleGroup(name);
      return this.lastChild;
   }

   protected abstract AbstractGroupVisitor handleGroup(String var1) throws IOException;

   public final void visitEnd() throws IOException {
      this.finalizeLastChild();
      this.handleEnd();
   }

   protected abstract void handleEnd() throws IOException;

   private void finalizeLastChild() throws IOException {
      if (this.lastChild != null) {
         this.lastChild.visitEnd();
         this.total.increment((ICoverageNode)this.lastChild.total);
         this.lastChild = null;
      }

   }
}
