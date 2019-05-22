package com.gzoltar.shaded.org.jacoco.report.internal.xml;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.internal.AbstractGroupVisitor;
import java.io.IOException;

public class XMLGroupVisitor extends AbstractGroupVisitor {
   protected final XMLElement element;

   public XMLGroupVisitor(XMLElement element, String name) throws IOException {
      super(name);
      this.element = element;
   }

   protected void handleBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
      XMLElement child = this.createChild(bundle.getName());
      XMLCoverageWriter.writeBundle(bundle, child);
   }

   protected AbstractGroupVisitor handleGroup(String name) throws IOException {
      XMLElement child = this.createChild(name);
      return new XMLGroupVisitor(child, name);
   }

   protected void handleEnd() throws IOException {
      XMLCoverageWriter.writeCounters(this.total, this.element);
   }

   private XMLElement createChild(String name) throws IOException {
      return XMLCoverageWriter.createChild(this.element, "group", name);
   }
}
