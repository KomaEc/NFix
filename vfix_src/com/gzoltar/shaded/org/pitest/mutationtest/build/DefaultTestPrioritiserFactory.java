package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import java.util.Properties;

public class DefaultTestPrioritiserFactory implements TestPrioritiserFactory {
   public String description() {
      return "Default test prioritiser";
   }

   public TestPrioritiser makeTestPrioritiser(Properties props, CodeSource code, CoverageDatabase coverage) {
      return new DefaultTestPrioritiser(coverage);
   }
}
