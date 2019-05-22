package soot.jimple.spark.ondemand;

import java.util.Set;
import soot.Local;
import soot.PointsToSet;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.sets.EqualsSupportingPointsToSet;

public class LazyContextSensitivePointsToSet implements EqualsSupportingPointsToSet {
   private EqualsSupportingPointsToSet delegate;
   private final DemandCSPointsTo demandCSPointsTo;
   private final Local local;
   private boolean isContextSensitive;

   public boolean isContextSensitive() {
      return this.isContextSensitive;
   }

   public LazyContextSensitivePointsToSet(Local l, EqualsSupportingPointsToSet contextInsensitiveSet, DemandCSPointsTo demandCSPointsTo) {
      this.local = l;
      this.delegate = contextInsensitiveSet;
      this.demandCSPointsTo = demandCSPointsTo;
      this.isContextSensitive = false;
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      Object otherInner;
      if (other instanceof LazyContextSensitivePointsToSet) {
         otherInner = ((LazyContextSensitivePointsToSet)other).delegate;
      } else {
         otherInner = other;
      }

      if (this.delegate.hasNonEmptyIntersection((PointsToSet)otherInner)) {
         if (other instanceof LazyContextSensitivePointsToSet) {
            ((LazyContextSensitivePointsToSet)other).computeContextSensitiveInfo();
            otherInner = ((LazyContextSensitivePointsToSet)other).delegate;
         }

         this.computeContextSensitiveInfo();
         return this.delegate.hasNonEmptyIntersection((PointsToSet)otherInner);
      } else {
         return false;
      }
   }

   public void computeContextSensitiveInfo() {
      if (!this.isContextSensitive) {
         this.delegate = (EqualsSupportingPointsToSet)this.demandCSPointsTo.doReachingObjects(this.local);
         this.isContextSensitive = true;
      }

   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Set<ClassConstant> possibleClassConstants() {
      return this.delegate.possibleClassConstants();
   }

   public Set<String> possibleStringConstants() {
      return this.delegate.possibleStringConstants();
   }

   public Set<Type> possibleTypes() {
      return this.delegate.possibleTypes();
   }

   public boolean pointsToSetEquals(Object other) {
      return !(other instanceof LazyContextSensitivePointsToSet) ? false : ((LazyContextSensitivePointsToSet)other).delegate.equals(this.delegate);
   }

   public int pointsToSetHashCode() {
      return this.delegate.pointsToSetHashCode();
   }

   public EqualsSupportingPointsToSet getDelegate() {
      return this.delegate;
   }
}
