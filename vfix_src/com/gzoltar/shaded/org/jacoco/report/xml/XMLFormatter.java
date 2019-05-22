package com.gzoltar.shaded.org.jacoco.report.xml;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import com.gzoltar.shaded.org.jacoco.report.IReportVisitor;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import com.gzoltar.shaded.org.jacoco.report.internal.AbstractGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.internal.xml.XMLCoverageWriter;
import com.gzoltar.shaded.org.jacoco.report.internal.xml.XMLDocument;
import com.gzoltar.shaded.org.jacoco.report.internal.xml.XMLElement;
import com.gzoltar.shaded.org.jacoco.report.internal.xml.XMLGroupVisitor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class XMLFormatter {
   private static final String PUBID = "-//JACOCO//DTD Report 1.0//EN";
   private static final String SYSTEM = "report.dtd";
   private String outputEncoding = "UTF-8";

   public void setOutputEncoding(String outputEncoding) {
      this.outputEncoding = outputEncoding;
   }

   public IReportVisitor createVisitor(OutputStream output) throws IOException {
      final XMLElement root = new XMLDocument("report", "-//JACOCO//DTD Report 1.0//EN", "report.dtd", this.outputEncoding, true, output);

      class RootVisitor extends XMLGroupVisitor implements IReportVisitor {
         private List<SessionInfo> sessionInfos;

         RootVisitor(XMLElement element) throws IOException {
            super(element, (String)null);
         }

         public void visitInfo(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData) throws IOException {
            this.sessionInfos = sessionInfos;
         }

         protected void handleBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
            this.writeHeader(bundle.getName());
            XMLCoverageWriter.writeBundle(bundle, this.element);
         }

         protected AbstractGroupVisitor handleGroup(String name) throws IOException {
            this.writeHeader(name);
            return new XMLGroupVisitor(this.element, name);
         }

         private void writeHeader(String name) throws IOException {
            this.element.attr("name", name);
            Iterator i$ = this.sessionInfos.iterator();

            while(i$.hasNext()) {
               SessionInfo i = (SessionInfo)i$.next();
               XMLElement sessioninfo = root.element("sessioninfo");
               sessioninfo.attr("id", i.getId());
               sessioninfo.attr("start", i.getStartTimeStamp());
               sessioninfo.attr("dump", i.getDumpTimeStamp());
            }

         }

         protected void handleEnd() throws IOException {
            this.element.close();
         }
      }

      return new RootVisitor(root);
   }
}
