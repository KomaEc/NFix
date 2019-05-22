package corg.vfix.sa.analysis;

import soot.Local;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SootField;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.spark.sets.PointsToSetInternal;

public class AliasQuery {
   public static boolean isAlias(Value value1, Value value2) {
      if (value1.equals(value2)) {
         return true;
      } else if (value1.toString().equals("this") && value2.toString().equals("this")) {
         return true;
      } else {
         PointsToSetInternal pts1 = getPointsToSet(value1);
         PointsToSetInternal pts2 = getPointsToSet(value2);
         if (pts1 != null && pts2 != null) {
            if (!pts1.isEmpty() && !pts2.isEmpty()) {
               return pts1.hasNonEmptyIntersection(pts2);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   private static PointsToSetInternal getPointsToSet(Value value) {
      PointsToSetInternal pts = null;
      PointsToAnalysis ptsProvider = Scene.v().getPointsToAnalysis();
      if (value instanceof InstanceFieldRef) {
         InstanceFieldRef ifr = (InstanceFieldRef)value;
         pts = (PointsToSetInternal)ptsProvider.reachingObjects((Local)ifr.getBase(), ifr.getField());
      } else if (value instanceof ArrayRef) {
         ArrayRef arrayRef = (ArrayRef)value;
         pts = (PointsToSetInternal)ptsProvider.reachingObjectsOfArrayElement(ptsProvider.reachingObjects((Local)arrayRef.getBase()));
      } else if (value instanceof Local) {
         pts = (PointsToSetInternal)ptsProvider.reachingObjects((Local)value);
      } else if (value instanceof StaticFieldRef) {
         SootField field = ((StaticFieldRef)value).getField();
         pts = (PointsToSetInternal)ptsProvider.reachingObjects(field);
      }

      return pts;
   }
}
