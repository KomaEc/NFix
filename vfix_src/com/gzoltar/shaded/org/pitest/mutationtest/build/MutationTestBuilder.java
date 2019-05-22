package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationAnalyser;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MutationTestBuilder {
   private final MutationSource mutationSource;
   private final MutationAnalyser analyser;
   private final WorkerFactory workerFactory;
   private final MutationGrouper grouper;

   public MutationTestBuilder(WorkerFactory workerFactory, MutationAnalyser analyser, MutationSource mutationSource, MutationGrouper grouper) {
      this.mutationSource = mutationSource;
      this.analyser = analyser;
      this.workerFactory = workerFactory;
      this.grouper = grouper;
   }

   public List<MutationAnalysisUnit> createMutationTestUnits(Collection<ClassName> codeClasses) {
      List<MutationAnalysisUnit> tus = new ArrayList();
      List<MutationDetails> mutations = FCollection.flatMap(codeClasses, this.classToMutations());
      Collections.sort(mutations, this.comparator());
      Collection<MutationResult> analysedMutations = this.analyser.analyse(mutations);
      Collection<MutationDetails> needAnalysis = FCollection.filter(analysedMutations, statusNotKnown()).map(resultToDetails());
      List<MutationResult> analysed = FCollection.filter(analysedMutations, Prelude.not(statusNotKnown()));
      if (!analysed.isEmpty()) {
         tus.add(this.makePreAnalysedUnit(analysed));
      }

      if (!needAnalysis.isEmpty()) {
         Iterator i$ = this.grouper.groupMutations(codeClasses, needAnalysis).iterator();

         while(i$.hasNext()) {
            Collection<MutationDetails> ms = (Collection)i$.next();
            tus.add(this.makeUnanalysedUnit(ms));
         }
      }

      Collections.sort(tus, new AnalysisPriorityComparator());
      return tus;
   }

   private Comparator<MutationDetails> comparator() {
      return new Comparator<MutationDetails>() {
         public int compare(MutationDetails arg0, MutationDetails arg1) {
            return arg0.getId().compareTo(arg1.getId());
         }
      };
   }

   private F<ClassName, Iterable<MutationDetails>> classToMutations() {
      return new F<ClassName, Iterable<MutationDetails>>() {
         public Iterable<MutationDetails> apply(ClassName a) {
            return MutationTestBuilder.this.mutationSource.createMutations(a);
         }
      };
   }

   private MutationAnalysisUnit makePreAnalysedUnit(List<MutationResult> analysed) {
      return new KnownStatusMutationTestUnit(analysed);
   }

   private MutationAnalysisUnit makeUnanalysedUnit(Collection<MutationDetails> needAnalysis) {
      Set<ClassName> uniqueTestClasses = new HashSet();
      FCollection.flatMapTo(needAnalysis, mutationDetailsToTestClass(), uniqueTestClasses);
      return new MutationTestUnit(needAnalysis, uniqueTestClasses, this.workerFactory);
   }

   private static F<MutationResult, MutationDetails> resultToDetails() {
      return new F<MutationResult, MutationDetails>() {
         public MutationDetails apply(MutationResult a) {
            return a.getDetails();
         }
      };
   }

   private static F<MutationResult, Boolean> statusNotKnown() {
      return new F<MutationResult, Boolean>() {
         public Boolean apply(MutationResult a) {
            return a.getStatus() == DetectionStatus.NOT_STARTED;
         }
      };
   }

   private static F<MutationDetails, Iterable<ClassName>> mutationDetailsToTestClass() {
      return new F<MutationDetails, Iterable<ClassName>>() {
         public Iterable<ClassName> apply(MutationDetails a) {
            return FCollection.map(a.getTestsInOrder(), TestInfo.toDefiningClassName());
         }
      };
   }
}
