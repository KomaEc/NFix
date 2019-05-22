package com.gzoltar.shaded.org.jacoco.core.analysis;

import java.util.Collection;

public interface IPackageCoverage extends ICoverageNode {
   Collection<IClassCoverage> getClasses();

   Collection<ISourceFileCoverage> getSourceFiles();
}
