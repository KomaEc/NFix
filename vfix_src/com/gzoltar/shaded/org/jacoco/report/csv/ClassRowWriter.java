package com.gzoltar.shaded.org.jacoco.report.csv;

import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import java.io.IOException;

class ClassRowWriter {
   private static final ICoverageNode.CounterEntity[] COUNTERS;
   private final DelimitedWriter writer;
   private final ILanguageNames languageNames;

   public ClassRowWriter(DelimitedWriter writer, ILanguageNames languageNames) throws IOException {
      this.writer = writer;
      this.languageNames = languageNames;
      this.writeHeader();
   }

   private void writeHeader() throws IOException {
      this.writer.write("GROUP", "PACKAGE", "CLASS");
      ICoverageNode.CounterEntity[] arr$ = COUNTERS;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ICoverageNode.CounterEntity entity = arr$[i$];
         this.writer.write(entity.name() + "_MISSED");
         this.writer.write(entity.name() + "_COVERED");
      }

      this.writer.nextLine();
   }

   public void writeRow(String groupName, String packageName, IClassCoverage node) throws IOException {
      this.writer.write(groupName);
      this.writer.write(this.languageNames.getPackageName(packageName));
      String className = this.languageNames.getClassName(node.getName(), node.getSignature(), node.getSuperName(), node.getInterfaceNames());
      this.writer.write(className);
      ICoverageNode.CounterEntity[] arr$ = COUNTERS;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ICoverageNode.CounterEntity entity = arr$[i$];
         ICounter counter = node.getCounter(entity);
         this.writer.write(counter.getMissedCount());
         this.writer.write(counter.getCoveredCount());
      }

      this.writer.nextLine();
   }

   static {
      COUNTERS = new ICoverageNode.CounterEntity[]{ICoverageNode.CounterEntity.INSTRUCTION, ICoverageNode.CounterEntity.BRANCH, ICoverageNode.CounterEntity.LINE, ICoverageNode.CounterEntity.COMPLEXITY, ICoverageNode.CounterEntity.METHOD};
   }
}
