package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MutationTestSummaryData {
   private final String fileName;
   private final Set<String> mutators = new HashSet();
   private final Collection<MutationResult> mutations = new ArrayList();
   private final Set<ClassInfo> classes = new HashSet();
   private long numberOfCoveredLines;

   public MutationTestSummaryData(String fileName, Collection<MutationResult> results, Collection<String> mutators, Collection<ClassInfo> classes, long numberOfCoveredLines) {
      this.fileName = fileName;
      this.mutations.addAll(results);
      this.mutators.addAll(mutators);
      this.classes.addAll(classes);
      this.numberOfCoveredLines = numberOfCoveredLines;
   }

   public MutationTotals getTotals() {
      MutationTotals mt = new MutationTotals();
      mt.addFiles(1L);
      mt.addMutations(this.getNumberOfMutations());
      mt.addMutationsDetetcted(this.getNumberOfMutationsDetected());
      mt.addLines((long)this.getNumberOfLines());
      mt.addLinesCovered(this.numberOfCoveredLines);
      return mt;
   }

   public String getPackageName() {
      String packageName = ((ClassInfo)this.getMutatedClasses().iterator().next()).getName().asJavaName();
      int lastDot = packageName.lastIndexOf(46);
      return lastDot > 0 ? packageName.substring(0, lastDot) : "default";
   }

   public void add(MutationTestSummaryData data) {
      this.mutations.addAll(data.mutations);
      this.mutators.addAll(data.getMutators());
      int classesBefore = this.classes.size();
      this.classes.addAll(data.classes);
      if (classesBefore < this.classes.size()) {
         this.numberOfCoveredLines += data.numberOfCoveredLines;
      }

   }

   public Collection<TestInfo> getTests() {
      Set<TestInfo> uniqueTests = new HashSet();
      FCollection.flatMapTo(this.mutations, this.mutationToTargettedTests(), uniqueTests);
      return uniqueTests;
   }

   public String getFileName() {
      return this.fileName;
   }

   public Collection<ClassInfo> getMutatedClasses() {
      return this.classes;
   }

   public Set<String> getMutators() {
      return this.mutators;
   }

   public MutationResultList getResults() {
      return new MutationResultList(this.mutations);
   }

   public Collection<ClassInfo> getClasses() {
      return this.classes;
   }

   private int getNumberOfLines() {
      return (Integer)FCollection.fold(this.accumulateCodeLines(), 0, this.classes);
   }

   private F2<Integer, ClassInfo, Integer> accumulateCodeLines() {
      return new F2<Integer, ClassInfo, Integer>() {
         public Integer apply(Integer a, ClassInfo b) {
            return a + b.getNumberOfCodeLines();
         }
      };
   }

   private long getNumberOfMutations() {
      return (long)this.mutations.size();
   }

   private long getNumberOfMutationsDetected() {
      int count = 0;
      Iterator i$ = this.mutations.iterator();

      while(i$.hasNext()) {
         MutationResult each = (MutationResult)i$.next();
         if (each.getStatus().isDetected()) {
            ++count;
         }
      }

      return (long)count;
   }

   private F<MutationResult, Iterable<TestInfo>> mutationToTargettedTests() {
      return new F<MutationResult, Iterable<TestInfo>>() {
         public Iterable<TestInfo> apply(MutationResult a) {
            return a.getDetails().getTestsInOrder();
         }
      };
   }
}
