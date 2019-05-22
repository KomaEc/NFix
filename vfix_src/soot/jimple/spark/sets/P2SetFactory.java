package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.pag.PAG;

public abstract class P2SetFactory {
   public abstract PointsToSetInternal newSet(Type var1, PAG var2);
}
