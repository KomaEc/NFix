package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class DefaultTestPrioritiser implements TestPrioritiser {
   private static final Logger LOG = Log.getLogger();
   private static final int TIME_WEIGHTING_FOR_DIRECT_UNIT_TESTS = 1000;
   private final CoverageDatabase coverage;

   public DefaultTestPrioritiser(CoverageDatabase coverage) {
      this.coverage = coverage;
   }

   public List<TestInfo> assignTests(MutationDetails mutation) {
      return this.prioritizeTests(mutation.getClassName(), this.pickTests(mutation));
   }

   private Collection<TestInfo> pickTests(MutationDetails mutation) {
      if (!mutation.isInStaticInitializer()) {
         return this.coverage.getTestsForClassLine(mutation.getClassLine());
      } else {
         LOG.warning("Using untargetted tests");
         return this.coverage.getTestsForClass(mutation.getClassName());
      }
   }

   private List<TestInfo> prioritizeTests(ClassName clazz, Collection<TestInfo> testsForMutant) {
      List<TestInfo> sortedTis = FCollection.map(testsForMutant, Prelude.id(TestInfo.class));
      Collections.sort(sortedTis, new TestInfoPriorisationComparator(clazz, 1000));
      return sortedTis;
   }
}
