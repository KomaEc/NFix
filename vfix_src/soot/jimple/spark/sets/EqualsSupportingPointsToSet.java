package soot.jimple.spark.sets;

import soot.PointsToSet;

public interface EqualsSupportingPointsToSet extends PointsToSet {
   int pointsToSetHashCode();

   boolean pointsToSetEquals(Object var1);
}
