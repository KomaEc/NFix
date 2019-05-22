package soot.jimple.spark.ondemand;

import java.util.Set;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;
import soot.jimple.spark.sets.PointsToSetInternal;

public class WrappedPointsToSet implements EqualsSupportingPointsToSet {
   final PointsToSetInternal wrapped;

   public PointsToSetInternal getWrapped() {
      return this.wrapped;
   }

   public WrappedPointsToSet(PointsToSetInternal wrapped) {
      this.wrapped = wrapped;
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      if (other instanceof AllocAndContextSet) {
         return other.hasNonEmptyIntersection(this);
      } else {
         return other instanceof WrappedPointsToSet ? this.hasNonEmptyIntersection(((WrappedPointsToSet)other).getWrapped()) : this.wrapped.hasNonEmptyIntersection(other);
      }
   }

   public boolean isEmpty() {
      return this.wrapped.isEmpty();
   }

   public Set<ClassConstant> possibleClassConstants() {
      return this.wrapped.possibleClassConstants();
   }

   public Set<String> possibleStringConstants() {
      return this.wrapped.possibleStringConstants();
   }

   public Set<Type> possibleTypes() {
      return this.wrapped.possibleTypes();
   }

   public String toString() {
      return this.wrapped.toString();
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (this == obj) {
         return true;
      } else if (obj instanceof WrappedPointsToSet) {
         WrappedPointsToSet wrapper = (WrappedPointsToSet)obj;
         return this.wrapped.equals(wrapper.wrapped);
      } else {
         return obj.equals(this.wrapped);
      }
   }

   public int hashCode() {
      return this.wrapped.hashCode();
   }

   public boolean pointsToSetEquals(Object other) {
      if (!(other instanceof EqualsSupportingPointsToSet)) {
         return false;
      } else {
         EqualsSupportingPointsToSet otherPts = (EqualsSupportingPointsToSet)this.unwrapIfNecessary(other);
         return this.wrapped.pointsToSetEquals(otherPts);
      }
   }

   public int pointsToSetHashCode() {
      return this.wrapped.pointsToSetHashCode();
   }

   protected Object unwrapIfNecessary(Object obj) {
      if (obj instanceof WrappedPointsToSet) {
         WrappedPointsToSet wrapper = (WrappedPointsToSet)obj;
         obj = wrapper.wrapped;
      }

      return obj;
   }
}
