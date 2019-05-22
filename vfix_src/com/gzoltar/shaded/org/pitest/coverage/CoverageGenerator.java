package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.process.LaunchOptions;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;

public interface CoverageGenerator {
   CoverageDatabase calculateCoverage();

   Configuration getConfiguration();

   LaunchOptions getLaunchOptions();
}
