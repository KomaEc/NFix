package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;

public class SourceFileCoverageImpl extends SourceNodeImpl implements ISourceFileCoverage {
   private final String packagename;

   public SourceFileCoverageImpl(String name, String packagename) {
      super(ICoverageNode.ElementType.SOURCEFILE, name);
      this.packagename = packagename;
   }

   public String getPackageName() {
      return this.packagename;
   }
}
