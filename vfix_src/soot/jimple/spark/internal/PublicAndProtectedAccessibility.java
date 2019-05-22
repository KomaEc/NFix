package soot.jimple.spark.internal;

import soot.G;
import soot.Singletons;
import soot.SootField;
import soot.SootMethod;

public class PublicAndProtectedAccessibility implements ClientAccessibilityOracle {
   public PublicAndProtectedAccessibility(Singletons.Global g) {
   }

   public static PublicAndProtectedAccessibility v() {
      return G.v().soot_jimple_spark_internal_PublicAndProtectedAccessibility();
   }

   public boolean isAccessible(SootMethod method) {
      return method.isPublic() || method.isProtected();
   }

   public boolean isAccessible(SootField field) {
      return field.isPublic() || field.isProtected();
   }
}
