package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import java.io.Serializable;
import java.util.Comparator;

public class TestInfoPriorisationComparator implements Comparator<TestInfo>, Serializable {
   private static final long serialVersionUID = 1L;
   private final int distanceTimeWeighting;
   private final ClassName targetClass;

   public TestInfoPriorisationComparator(ClassName targetClass, int distanceTimeWeighting) {
      this.targetClass = targetClass;
      this.distanceTimeWeighting = distanceTimeWeighting;
   }

   public int compare(TestInfo arg0, TestInfo arg1) {
      int t0 = arg0.getTime();
      int t1 = arg1.getTime();
      return t0 - t1 - this.distanceWeighting(arg0, arg1);
   }

   private int distanceWeighting(TestInfo arg0, TestInfo arg1) {
      return this.weightFor(arg0) - this.weightFor(arg1);
   }

   private int weightFor(TestInfo ti) {
      return this.weightForDirectHit(ti) - ti.getNumberOfBlocksCovered() / 10;
   }

   private int weightForDirectHit(TestInfo arg0) {
      return arg0.directlyHits(this.targetClass) ? this.distanceTimeWeighting : 0;
   }
}
