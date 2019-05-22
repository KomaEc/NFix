package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.CoverageNodeImpl;
import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IPackageCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ISourceFileCoverage;
import java.util.Collection;
import java.util.Iterator;

public class PackageCoverageImpl extends CoverageNodeImpl implements IPackageCoverage {
   private final Collection<IClassCoverage> classes;
   private final Collection<ISourceFileCoverage> sourceFiles;

   public PackageCoverageImpl(String name, Collection<IClassCoverage> classes, Collection<ISourceFileCoverage> sourceFiles) {
      super(ICoverageNode.ElementType.PACKAGE, name);
      this.classes = classes;
      this.sourceFiles = sourceFiles;
      this.increment(sourceFiles);
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         IClassCoverage c = (IClassCoverage)i$.next();
         if (c.getSourceFileName() == null) {
            this.increment(c);
         }
      }

   }

   public Collection<IClassCoverage> getClasses() {
      return this.classes;
   }

   public Collection<ISourceFileCoverage> getSourceFiles() {
      return this.sourceFiles;
   }
}
