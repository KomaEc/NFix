package com.gzoltar.shaded.org.jacoco.report;

import com.gzoltar.shaded.org.jacoco.core.analysis.IBundleCoverage;
import java.io.IOException;

public interface IReportGroupVisitor {
   void visitBundle(IBundleCoverage var1, ISourceFileLocator var2) throws IOException;

   IReportGroupVisitor visitGroup(String var1) throws IOException;
}
