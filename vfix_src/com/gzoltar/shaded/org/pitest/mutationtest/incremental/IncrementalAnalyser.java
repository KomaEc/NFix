package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationAnalyser;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class IncrementalAnalyser implements MutationAnalyser {
   private static final Logger LOG = Log.getLogger();
   private final CodeHistory history;
   private final CoverageDatabase coverage;
   private final Map<DetectionStatus, Long> preAnalysed = createStatusMap();

   public IncrementalAnalyser(CodeHistory history, CoverageDatabase coverage) {
      this.history = history;
      this.coverage = coverage;
   }

   private static Map<DetectionStatus, Long> createStatusMap() {
      HashMap<DetectionStatus, Long> map = new HashMap();
      DetectionStatus[] arr$ = DetectionStatus.values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DetectionStatus each = arr$[i$];
         map.put(each, 0L);
      }

      return map;
   }

   public Collection<MutationResult> analyse(Collection<MutationDetails> mutation) {
      List<MutationResult> mrs = new ArrayList(mutation.size());
      Iterator i$ = mutation.iterator();

      while(i$.hasNext()) {
         MutationDetails each = (MutationDetails)i$.next();
         Option<MutationStatusTestPair> maybeResult = this.history.getPreviousResult(each.getId());
         if (maybeResult.hasNone()) {
            mrs.add(this.analyseFromScratch(each));
         } else {
            mrs.add(this.analyseFromHistory(each, (MutationStatusTestPair)maybeResult.value()));
         }
      }

      this.logTotals();
      return mrs;
   }

   private void logTotals() {
      Iterator i$ = this.preAnalysed.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<DetectionStatus, Long> each = (Entry)i$.next();
         if ((Long)each.getValue() != 0L) {
            LOG.fine("Incremental analysis set " + each.getValue() + " mutations to a status of " + each.getKey());
         }
      }

   }

   private MutationResult analyseFromHistory(MutationDetails each, MutationStatusTestPair mutationStatusTestPair) {
      ClassName clazz = each.getClassName();
      if (this.history.hasClassChanged(clazz)) {
         return this.analyseFromScratch(each);
      } else if (mutationStatusTestPair.getStatus() == DetectionStatus.TIMED_OUT) {
         return this.makeResult(each, DetectionStatus.TIMED_OUT);
      } else if (mutationStatusTestPair.getStatus() == DetectionStatus.KILLED && this.killingTestHasNotChanged(each, mutationStatusTestPair)) {
         return this.makeResult(each, DetectionStatus.KILLED, (String)mutationStatusTestPair.getKillingTest().value());
      } else {
         return mutationStatusTestPair.getStatus() == DetectionStatus.SURVIVED && !this.history.hasCoverageChanged(clazz, this.coverage.getCoverageIdForClass(clazz)) ? this.makeResult(each, DetectionStatus.SURVIVED) : this.analyseFromScratch(each);
      }
   }

   private boolean killingTestHasNotChanged(MutationDetails each, MutationStatusTestPair mutationStatusTestPair) {
      Collection<TestInfo> allTests = this.coverage.getTestsForClass(each.getClassName());
      List<ClassName> testClasses = FCollection.filter(allTests, testIsCalled((String)mutationStatusTestPair.getKillingTest().value())).map(TestInfo.toDefiningClassName());
      if (testClasses.isEmpty()) {
         return false;
      } else {
         return !this.history.hasClassChanged((ClassName)testClasses.get(0));
      }
   }

   private static F<TestInfo, Boolean> testIsCalled(final String testName) {
      return new F<TestInfo, Boolean>() {
         public Boolean apply(TestInfo a) {
            return a.getName().equals(testName);
         }
      };
   }

   private MutationResult analyseFromScratch(MutationDetails mutation) {
      return this.makeResult(mutation, DetectionStatus.NOT_STARTED);
   }

   private MutationResult makeResult(MutationDetails each, DetectionStatus status) {
      return this.makeResult(each, status, (String)null);
   }

   private MutationResult makeResult(MutationDetails each, DetectionStatus status, String killingTest) {
      this.updatePreanalysedTotal(status);
      return new MutationResult(each, new MutationStatusTestPair(0, status, killingTest));
   }

   private void updatePreanalysedTotal(DetectionStatus status) {
      if (status != DetectionStatus.NOT_STARTED) {
         long count = (Long)this.preAnalysed.get(status);
         this.preAnalysed.put(status, count + 1L);
      }

   }
}
