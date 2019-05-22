package soot.jimple.spark.internal;

import soot.G;
import soot.Singletons;
import soot.SootField;
import soot.SootMethod;

public class CompleteAccessibility implements ClientAccessibilityOracle {
   public CompleteAccessibility(Singletons.Global g) {
   }

   public static CompleteAccessibility v() {
      return G.v().soot_jimple_spark_internal_CompleteAccessibility();
   }

   public boolean isAccessible(SootMethod method) {
      return true;
   }

   public boolean isAccessible(SootField field) {
      return true;
   }
}
