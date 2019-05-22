package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;
import java.util.Set;

public interface FlowFunction<FieldRef, D, Stmt, Method> {
   Set<FlowFunction.ConstrainedFact<FieldRef, D, Stmt, Method>> computeTargets(D var1, AccessPathHandler<FieldRef, D, Stmt, Method> var2);

   public static class ReadFieldConstraint<FieldRef> implements FlowFunction.Constraint<FieldRef> {
      private FieldRef fieldRef;

      public ReadFieldConstraint(FieldRef fieldRef) {
         this.fieldRef = fieldRef;
      }

      public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
         return accPath.append(this.fieldRef);
      }

      public String toString() {
         return this.fieldRef.toString();
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.fieldRef == null ? 0 : this.fieldRef.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (!(obj instanceof FlowFunction.ReadFieldConstraint)) {
            return false;
         } else {
            FlowFunction.ReadFieldConstraint other = (FlowFunction.ReadFieldConstraint)obj;
            if (this.fieldRef == null) {
               if (other.fieldRef != null) {
                  return false;
               }
            } else if (!this.fieldRef.equals(other.fieldRef)) {
               return false;
            }

            return true;
         }
      }

      public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
         return !accPath.isAccessInExclusions(this.fieldRef);
      }
   }

   public static class WriteFieldConstraint<FieldRef> implements FlowFunction.Constraint<FieldRef> {
      private FieldRef fieldRef;

      public WriteFieldConstraint(FieldRef fieldRef) {
         this.fieldRef = fieldRef;
      }

      public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
         return accPath.appendExcludedFieldReference(this.fieldRef);
      }

      public String toString() {
         return "^" + this.fieldRef.toString();
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.fieldRef == null ? 0 : this.fieldRef.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (!(obj instanceof FlowFunction.WriteFieldConstraint)) {
            return false;
         } else {
            FlowFunction.WriteFieldConstraint other = (FlowFunction.WriteFieldConstraint)obj;
            if (this.fieldRef == null) {
               if (other.fieldRef != null) {
                  return false;
               }
            } else if (!this.fieldRef.equals(other.fieldRef)) {
               return false;
            }

            return true;
         }
      }

      public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
         return true;
      }
   }

   public interface Constraint<FieldRef> {
      AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> var1);

      boolean canBeAppliedTo(AccessPath<FieldRef> var1);
   }

   public static class ConstrainedFact<FieldRef, D, Stmt, Method> {
      private WrappedFact<FieldRef, D, Stmt, Method> fact;
      private FlowFunction.Constraint<FieldRef> constraint;

      ConstrainedFact(WrappedFact<FieldRef, D, Stmt, Method> fact) {
         this.fact = fact;
         this.constraint = null;
      }

      ConstrainedFact(WrappedFact<FieldRef, D, Stmt, Method> fact, FlowFunction.Constraint<FieldRef> constraint) {
         this.fact = fact;
         this.constraint = constraint;
      }

      public WrappedFact<FieldRef, D, Stmt, Method> getFact() {
         return this.fact;
      }

      public FlowFunction.Constraint<FieldRef> getConstraint() {
         return this.constraint;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.constraint == null ? 0 : this.constraint.hashCode());
         result = 31 * result + (this.fact == null ? 0 : this.fact.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (!(obj instanceof FlowFunction.ConstrainedFact)) {
            return false;
         } else {
            FlowFunction.ConstrainedFact other = (FlowFunction.ConstrainedFact)obj;
            if (this.constraint == null) {
               if (other.constraint != null) {
                  return false;
               }
            } else if (!this.constraint.equals(other.constraint)) {
               return false;
            }

            if (this.fact == null) {
               if (other.fact != null) {
                  return false;
               }
            } else if (!this.fact.equals(other.fact)) {
               return false;
            }

            return true;
         }
      }

      public String toString() {
         return this.fact.toString() + "<" + this.constraint + ">";
      }
   }
}
