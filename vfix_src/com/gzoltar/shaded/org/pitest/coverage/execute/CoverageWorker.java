package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.coverage.CoverageReceiver;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.execute.Container;
import com.gzoltar.shaded.org.pitest.testapi.execute.Pitest;
import com.gzoltar.shaded.org.pitest.testapi.execute.containers.UnContainer;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CoverageWorker {
   private final CoveragePipe pipe;
   private final List<TestUnit> tests;

   public CoverageWorker(CoveragePipe pipe, List<TestUnit> tests) {
      this.pipe = pipe;
      this.tests = tests;
   }

   public void run() {
      try {
         List<TestUnit> decoratedTests = this.decorateForCoverage(this.tests, this.pipe);
         Collections.sort(decoratedTests, this.testComparator());
         Container c = new UnContainer();
         Pitest pit = new Pitest(Collections.singletonList(new ErrorListener()));
         pit.run(c, decoratedTests);
      } catch (Exception var4) {
         throw Unchecked.translateCheckedException(var4);
      }
   }

   private Comparator<TestUnit> testComparator() {
      return new Comparator<TestUnit>() {
         public int compare(TestUnit o1, TestUnit o2) {
            return o1.getDescription().getQualifiedName().compareTo(o2.getDescription().getQualifiedName());
         }
      };
   }

   private List<TestUnit> decorateForCoverage(List<TestUnit> plainTests, CoverageReceiver queue) {
      List<TestUnit> decorated = new ArrayList(plainTests.size());
      Iterator i$ = plainTests.iterator();

      while(i$.hasNext()) {
         TestUnit each = (TestUnit)i$.next();
         decorated.add(new CoverageDecorator(queue, each));
      }

      return decorated;
   }
}
