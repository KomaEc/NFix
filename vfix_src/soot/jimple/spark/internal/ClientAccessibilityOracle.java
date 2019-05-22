package soot.jimple.spark.internal;

import soot.SootField;
import soot.SootMethod;

public interface ClientAccessibilityOracle {
   boolean isAccessible(SootMethod var1);

   boolean isAccessible(SootField var1);
}
