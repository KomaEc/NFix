package com.gzoltar.shaded.org.pitest.testapi;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;

public interface Configuration {
   TestUnitFinder testUnitFinder();

   TestSuiteFinder testSuiteFinder();

   TestClassIdentifier testClassIdentifier();

   Option<PitHelpError> verifyEnvironment();
}
