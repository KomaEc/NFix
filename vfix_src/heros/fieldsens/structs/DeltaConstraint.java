package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;

public class DeltaConstraint<FieldRef> implements FlowFunction.Constraint<FieldRef> {
   private AccessPath.Delta<FieldRef> delta;

   public DeltaConstraint(AccessPath<FieldRef> accPathAtCaller, AccessPath<FieldRef> accPathAtCallee) {
      this.delta = accPathAtCaller.getDeltaTo(accPathAtCallee);
   }

   public DeltaConstraint(AccessPath.Delta<FieldRef> delta) {
      this.delta = delta;
   }

   public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
      return this.delta.applyTo(accPath);
   }

   public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
      return this.delta.canBeAppliedTo(accPath);
   }

   public String toString() {
      return this.delta.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.delta == null ? 0 : this.delta.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         DeltaConstraint other = (DeltaConstraint)obj;
         if (this.delta == null) {
            if (other.delta != null) {
               return false;
            }
         } else if (!this.delta.equals(other.delta)) {
            return false;
         }

         return true;
      }
   }
}
