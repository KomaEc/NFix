package com.gzoltar.shaded.org.jacoco.report.csv;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.report.IReportGroupVisitor;
import com.gzoltar.shaded.org.jacoco.report.ISourceFileLocator;
import java.io.IOException;
import java.util.Iterator;

class CSVGroupHandler implements IReportGroupVisitor {
   private final ClassRowWriter writer;
   private final String groupName;

   public CSVGroupHandler(ClassRowWriter writer) {
      this(writer, (String)null);
   }

   private CSVGroupHandler(ClassRowWriter writer, String groupName) {
      this.writer = writer;
      this.groupName = groupName;
   }

   public void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
      String name = this.appendName(bundle.getName());
      Iterator i$ = bundle.getPackages().iterator();

      while(i$.hasNext()) {
         IPackageCoverage p = (IPackageCoverage)i$.next();
         String packageName = p.getName();
         Iterator i$ = p.getClasses().iterator();

         while(i$.hasNext()) {
            IClassCoverage c = (IClassCoverage)i$.next();
            this.writer.writeRow(name, packageName, c);
         }
      }

   }

   public IReportGroupVisitor visitGroup(String name) throws IOException {
      return new CSVGroupHandler(this.writer, this.appendName(name));
   }

   private String appendName(String name) {
      return this.groupName == null ? name : this.groupName + "/" + name;
   }
}
