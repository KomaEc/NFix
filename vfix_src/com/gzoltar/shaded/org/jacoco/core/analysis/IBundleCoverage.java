package com.gzoltar.shaded.org.jacoco.core.analysis;

import java.util.Collection;

public interface IBundleCoverage extends ICoverageNode {
   Collection<IPackageCoverage> getPackages();
}
