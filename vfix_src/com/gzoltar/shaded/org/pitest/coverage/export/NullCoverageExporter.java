package com.gzoltar.shaded.org.pitest.coverage.export;

import com.gzoltar.shaded.org.pitest.coverage.BlockCoverage;
import com.gzoltar.shaded.org.pitest.coverage.CoverageExporter;
import java.util.Collection;

public class NullCoverageExporter implements CoverageExporter {
   public void recordCoverage(Collection<BlockCoverage> coverage) {
   }
}
