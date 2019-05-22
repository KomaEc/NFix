package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.TimeoutLengthStrategy;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TimeOutDecoratedTestSource {
   private final Map<String, TestUnit> allTests = new HashMap();
   private final TimeoutLengthStrategy timeoutStrategy;
   private final Reporter r;

   public TimeOutDecoratedTestSource(TimeoutLengthStrategy timeoutStrategy, List<TestUnit> allTests, Reporter r) {
      this.timeoutStrategy = timeoutStrategy;
      this.mapTests(allTests);
      this.r = r;
   }

   private void mapTests(List<TestUnit> tests) {
      Iterator i$ = tests.iterator();

      while(i$.hasNext()) {
         TestUnit each = (TestUnit)i$.next();
         this.allTests.put(each.getDescription().getQualifiedName(), each);
      }

   }

   public List<TestUnit> translateTests(List<TestInfo> testsInOrder) {
      return FCollection.flatMap(testsInOrder, this.testToTestUnit());
   }

   private F<TestInfo, Option<TestUnit>> testToTestUnit() {
      return new F<TestInfo, Option<TestUnit>>() {
         public Option<TestUnit> apply(TestInfo a) {
            TestUnit tu = (TestUnit)TimeOutDecoratedTestSource.this.allTests.get(a.getName());
            return (Option)(tu != null ? Option.some(new MutationTimeoutDecorator(tu, new TimeOutSystemExitSideEffect(TimeOutDecoratedTestSource.this.r), TimeOutDecoratedTestSource.this.timeoutStrategy, (long)a.getTime())) : Option.none());
         }
      };
   }
}
