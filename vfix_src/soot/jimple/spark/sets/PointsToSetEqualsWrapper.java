package soot.jimple.spark.sets;

import java.util.Set;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;

public class PointsToSetEqualsWrapper implements PointsToSet {
   protected EqualsSupportingPointsToSet pts;

   public PointsToSetEqualsWrapper(EqualsSupportingPointsToSet pts) {
      this.pts = pts;
   }

   public int hashCode() {
      return this.pts.pointsToSetHashCode();
   }

   public boolean equals(Object obj) {
      if (this != obj && this.pts != obj) {
         obj = this.unwrapIfNecessary(obj);
         return this.pts.pointsToSetEquals(obj);
      } else {
         return true;
      }
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      other = (PointsToSet)this.unwrapIfNecessary(other);
      return this.pts.hasNonEmptyIntersection(other);
   }

   public boolean isEmpty() {
      return this.pts.isEmpty();
   }

   public Set<ClassConstant> possibleClassConstants() {
      return this.pts.possibleClassConstants();
   }

   public Set<String> possibleStringConstants() {
      return this.pts.possibleStringConstants();
   }

   public Set<Type> possibleTypes() {
      return this.pts.possibleTypes();
   }

   protected Object unwrapIfNecessary(Object obj) {
      if (obj instanceof PointsToSetEqualsWrapper) {
         PointsToSetEqualsWrapper wrapper = (PointsToSetEqualsWrapper)obj;
         obj = wrapper.pts;
      }

      return obj;
   }

   public String toString() {
      return this.pts.toString();
   }
}
