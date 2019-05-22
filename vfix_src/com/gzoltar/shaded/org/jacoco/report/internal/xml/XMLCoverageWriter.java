package com.gzoltar.shaded.org.jacoco.report.internal.xml;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.ILine;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceNode;
import java.io.IOException;
import java.util.Iterator;

public final class XMLCoverageWriter {
   public static XMLElement createChild(XMLElement parent, String tagname, String name) throws IOException {
      XMLElement child = parent.element(tagname);
      child.attr("name", name);
      return child;
   }

   public static void writeBundle(IBundleCoverage bundle, XMLElement element) throws IOException {
      Iterator i$ = bundle.getPackages().iterator();

      while(i$.hasNext()) {
         IPackageCoverage p = (IPackageCoverage)i$.next();
         writePackage(p, element);
      }

      writeCounters(bundle, element);
   }

   private static void writePackage(IPackageCoverage p, XMLElement parent) throws IOException {
      XMLElement element = createChild(parent, "package", p.getName());
      Iterator i$ = p.getClasses().iterator();

      while(i$.hasNext()) {
         IClassCoverage c = (IClassCoverage)i$.next();
         writeClass(c, element);
      }

      i$ = p.getSourceFiles().iterator();

      while(i$.hasNext()) {
         ISourceFileCoverage s = (ISourceFileCoverage)i$.next();
         writeSourceFile(s, element);
      }

      writeCounters(p, element);
   }

   private static void writeClass(IClassCoverage c, XMLElement parent) throws IOException {
      XMLElement element = createChild(parent, "class", c.getName());
      Iterator i$ = c.getMethods().iterator();

      while(i$.hasNext()) {
         IMethodCoverage m = (IMethodCoverage)i$.next();
         writeMethod(m, element);
      }

      writeCounters(c, element);
   }

   private static void writeMethod(IMethodCoverage m, XMLElement parent) throws IOException {
      XMLElement element = createChild(parent, "method", m.getName());
      element.attr("desc", m.getDesc());
      int line = m.getFirstLine();
      if (line != -1) {
         element.attr("line", line);
      }

      writeCounters(m, element);
   }

   private static void writeSourceFile(ISourceFileCoverage s, XMLElement parent) throws IOException {
      XMLElement element = createChild(parent, "sourcefile", s.getName());
      writeLines(s, element);
      writeCounters(s, element);
   }

   public static void writeCounters(ICoverageNode node, XMLElement parent) throws IOException {
      ICoverageNode.CounterEntity[] arr$ = ICoverageNode.CounterEntity.values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ICoverageNode.CounterEntity counterEntity = arr$[i$];
         ICounter counter = node.getCounter(counterEntity);
         if (counter.getTotalCount() > 0) {
            XMLElement counterNode = parent.element("counter");
            counterNode.attr("type", counterEntity.name());
            writeCounter(counterNode, "missed", "covered", counter);
            counterNode.close();
         }
      }

   }

   private static void writeLines(ISourceNode source, XMLElement parent) throws IOException {
      int last = source.getLastLine();

      for(int nr = source.getFirstLine(); nr <= last; ++nr) {
         ILine line = source.getLine(nr);
         if (line.getStatus() != 0) {
            XMLElement element = parent.element("line");
            element.attr("nr", nr);
            writeCounter(element, "mi", "ci", line.getInstructionCounter());
            writeCounter(element, "mb", "cb", line.getBranchCounter());
         }
      }

   }

   private static void writeCounter(XMLElement element, String missedattr, String coveredattr, ICounter counter) throws IOException {
      element.attr(missedattr, counter.getMissedCount());
      element.attr(coveredattr, counter.getCoveredCount());
   }

   private XMLCoverageWriter() {
   }
}
