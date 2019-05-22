package soot.jimple.toolkits.pointer;

import java.util.Set;
import soot.PointsToSet;
import soot.jimple.ClassConstant;

public abstract class Union implements PointsToSet {
   public abstract boolean addAll(PointsToSet var1);

   public static boolean hasNonEmptyIntersection(PointsToSet s1, PointsToSet s2) {
      if (s1 == null) {
         return false;
      } else if (s1 instanceof Union) {
         return s1.hasNonEmptyIntersection(s2);
      } else {
         return s2 == null ? false : s2.hasNonEmptyIntersection(s1);
      }
   }

   public Set<String> possibleStringConstants() {
      return null;
   }

   public Set<ClassConstant> possibleClassConstants() {
      return null;
   }
}
