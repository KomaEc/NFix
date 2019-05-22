package com.gzoltar.shaded.org.pitest.testng;

import com.gzoltar.shaded.org.pitest.extension.common.NoTestSuiteFinder;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestClassIdentifier;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;

public class TestNGConfiguration implements Configuration {
   private final TestGroupConfig config;

   public TestNGConfiguration(TestGroupConfig config) {
      this.config = config;
   }

   public TestUnitFinder testUnitFinder() {
      return new TestNGTestUnitFinder(this.config);
   }

   public TestSuiteFinder testSuiteFinder() {
      return new NoTestSuiteFinder();
   }

   public TestClassIdentifier testClassIdentifier() {
      return new TestNGTestClassIdentifier();
   }

   public Option<PitHelpError> verifyEnvironment() {
      return Option.none();
   }
}
