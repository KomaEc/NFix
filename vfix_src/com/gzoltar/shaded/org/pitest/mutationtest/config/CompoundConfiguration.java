package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.extension.common.CompoundTestSuiteFinder;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.junit.CompoundTestUnitFinder;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestClassIdentifier;
import com.gzoltar.shaded.org.pitest.testapi.TestSuiteFinder;
import com.gzoltar.shaded.org.pitest.testapi.TestUnitFinder;
import java.util.List;

public class CompoundConfiguration implements Configuration {
   private final Iterable<Configuration> configs;
   private final CompoundTestUnitFinder testUnitFinder;
   private final CompoundTestSuiteFinder suiteFinder;
   private final CompoundTestClassIdentifier testIdentifier;

   public CompoundConfiguration(Iterable<Configuration> configs) {
      this.configs = configs;
      this.testUnitFinder = new CompoundTestUnitFinder(FCollection.map(configs, asTestUnitFinders()));
      this.suiteFinder = new CompoundTestSuiteFinder(FCollection.map(configs, asSuiteFinders()));
      this.testIdentifier = new CompoundTestClassIdentifier(FCollection.map(configs, asTestIdentifier()));
   }

   private static F<Configuration, TestClassIdentifier> asTestIdentifier() {
      return new F<Configuration, TestClassIdentifier>() {
         public TestClassIdentifier apply(Configuration a) {
            return a.testClassIdentifier();
         }
      };
   }

   private static F<Configuration, TestSuiteFinder> asSuiteFinders() {
      return new F<Configuration, TestSuiteFinder>() {
         public TestSuiteFinder apply(Configuration a) {
            return a.testSuiteFinder();
         }
      };
   }

   private static F<Configuration, TestUnitFinder> asTestUnitFinders() {
      return new F<Configuration, TestUnitFinder>() {
         public TestUnitFinder apply(Configuration a) {
            return a.testUnitFinder();
         }
      };
   }

   public TestUnitFinder testUnitFinder() {
      return this.testUnitFinder;
   }

   public TestSuiteFinder testSuiteFinder() {
      return this.suiteFinder;
   }

   public TestClassIdentifier testClassIdentifier() {
      return this.testIdentifier;
   }

   public Option<PitHelpError> verifyEnvironment() {
      List<PitHelpError> verificationResults = FCollection.flatMap(this.configs, verify());
      return (Option)(verificationResults.isEmpty() ? Option.none() : Option.some(verificationResults.iterator().next()));
   }

   private static F<Configuration, Iterable<PitHelpError>> verify() {
      return new F<Configuration, Iterable<PitHelpError>>() {
         public Iterable<PitHelpError> apply(Configuration a) {
            return a.verifyEnvironment();
         }
      };
   }
}
