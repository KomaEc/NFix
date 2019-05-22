package org.testng.internal.junit;

import org.testng.AssertJUnit;

public class InexactComparisonCriteria extends ComparisonCriteria {
   public double fDelta;

   public InexactComparisonCriteria(double delta) {
      this.fDelta = delta;
   }

   protected void assertElementsEqual(Object expected, Object actual) {
      if (expected instanceof Double) {
         AssertJUnit.assertEquals((Double)expected, (Double)actual, this.fDelta);
      } else {
         AssertJUnit.assertEquals((double)(Float)expected, (double)(Float)actual, this.fDelta);
      }

   }
}
