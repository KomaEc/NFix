package com.gzoltar.shaded.org.pitest.junit;

import com.gzoltar.shaded.org.pitest.extension.common.CompoundTestSuiteFinder;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestClassIdentifier;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.runner.Version;

public class JUnitCompatibleConfiguration implements Configuration {
   private final TestGroupConfig config;
   private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+)\\.(\\d+).*");

   public JUnitCompatibleConfiguration(TestGroupConfig config) {
      this.config = config;
   }

   public TestUnitFinder testUnitFinder() {
      return new CompoundTestUnitFinder(Arrays.asList(new JUnitCustomRunnerTestUnitFinder(), new ParameterisedJUnitTestFinder()));
   }

   public TestSuiteFinder testSuiteFinder() {
      return new CompoundTestSuiteFinder(Arrays.asList(new JUnit4SuiteFinder(), new RunnerSuiteFinder()));
   }

   public TestClassIdentifier testClassIdentifier() {
      return new JUnitTestClassIdentifier(this.config);
   }

   public Option<PitHelpError> verifyEnvironment() {
      try {
         String version = Version.id();
         if (this.isInvalidVersion(version)) {
            return Option.some(new PitHelpError(Help.WRONG_JUNIT_VERSION, new Object[]{version}));
         }
      } catch (NoClassDefFoundError var2) {
         return Option.some(new PitHelpError(Help.NO_JUNIT, new Object[0]));
      }

      return Option.none();
   }

   boolean isInvalidVersion(String version) {
      Matcher matcher = VERSION_PATTERN.matcher(version);
      if (!matcher.matches()) {
         return true;
      } else {
         int major = Integer.parseInt(matcher.group(1));
         int minor = Integer.parseInt(matcher.group(2));
         return major < 4 || major == 4 && minor < 6;
      }
   }
}
