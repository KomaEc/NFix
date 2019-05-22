package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MutationStatusMap {
   private final Map<MutationDetails, MutationStatusTestPair> mutationMap = new HashMap();

   public void setStatusForMutation(MutationDetails mutation, DetectionStatus status) {
      this.setStatusForMutations(Collections.singleton(mutation), status);
   }

   public void setStatusForMutation(MutationDetails mutation, MutationStatusTestPair status) {
      this.mutationMap.put(mutation, status);
   }

   public void setStatusForMutations(Collection<MutationDetails> mutations, DetectionStatus status) {
      FCollection.forEach(mutations, Prelude.putToMap(this.mutationMap, new MutationStatusTestPair(0, status)));
   }

   public List<MutationResult> createMutationResults() {
      return FCollection.map(this.mutationMap.entrySet(), detailsToMutationResults());
   }

   public boolean hasUnrunMutations() {
      return !this.getUnrunMutations().isEmpty();
   }

   public Collection<MutationDetails> getUnrunMutations() {
      return FCollection.filter(this.mutationMap.entrySet(), hasStatus(DetectionStatus.NOT_STARTED)).map(toMutationDetails());
   }

   public Collection<MutationDetails> getUnfinishedRuns() {
      return FCollection.filter(this.mutationMap.entrySet(), hasStatus(DetectionStatus.STARTED)).map(toMutationDetails());
   }

   public Set<MutationDetails> allMutations() {
      return this.mutationMap.keySet();
   }

   private static F<Entry<MutationDetails, MutationStatusTestPair>, MutationResult> detailsToMutationResults() {
      return new F<Entry<MutationDetails, MutationStatusTestPair>, MutationResult>() {
         public MutationResult apply(Entry<MutationDetails, MutationStatusTestPair> a) {
            return new MutationResult((MutationDetails)a.getKey(), (MutationStatusTestPair)a.getValue());
         }
      };
   }

   private static F<Entry<MutationDetails, MutationStatusTestPair>, MutationDetails> toMutationDetails() {
      return new F<Entry<MutationDetails, MutationStatusTestPair>, MutationDetails>() {
         public MutationDetails apply(Entry<MutationDetails, MutationStatusTestPair> a) {
            return (MutationDetails)a.getKey();
         }
      };
   }

   private static Predicate<Entry<MutationDetails, MutationStatusTestPair>> hasStatus(final DetectionStatus status) {
      return new Predicate<Entry<MutationDetails, MutationStatusTestPair>>() {
         public Boolean apply(Entry<MutationDetails, MutationStatusTestPair> a) {
            return ((MutationStatusTestPair)a.getValue()).getStatus().equals(status);
         }
      };
   }

   public void markUncoveredMutations() {
      this.setStatusForMutations(FCollection.filter(this.mutationMap.keySet(), hasNoCoverage()), DetectionStatus.NO_COVERAGE);
   }

   private static F<MutationDetails, Boolean> hasNoCoverage() {
      return new F<MutationDetails, Boolean>() {
         public Boolean apply(MutationDetails a) {
            return a.getTestsInOrder().isEmpty();
         }
      };
   }
}
