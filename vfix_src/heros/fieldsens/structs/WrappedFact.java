package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import heros.fieldsens.Resolver;

public class WrappedFact<Field, Fact, Stmt, Method> {
   private final Fact fact;
   private final AccessPath<Field> accessPath;
   private final Resolver<Field, Fact, Stmt, Method> resolver;

   public WrappedFact(Fact fact, AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver) {
      assert fact != null;

      assert accessPath != null;

      assert resolver != null;

      this.fact = fact;
      this.accessPath = accessPath;
      this.resolver = resolver;
   }

   public Fact getFact() {
      return this.fact;
   }

   public WrappedFact<Field, Fact, Stmt, Method> applyDelta(AccessPath.Delta<Field> delta) {
      return new WrappedFact(this.fact, delta.applyTo(this.accessPath), this.resolver);
   }

   public AccessPath<Field> getAccessPath() {
      return this.accessPath;
   }

   public WrappedFact<Field, Fact, Stmt, Method> applyConstraint(FlowFunction.Constraint<Field> constraint, Fact zeroValue) {
      return this.fact.equals(zeroValue) ? this : new WrappedFact(this.fact, constraint.applyToAccessPath(this.accessPath), this.resolver);
   }

   public String toString() {
      String result = this.fact.toString() + this.accessPath;
      if (this.resolver != null) {
         result = result + this.resolver.toString();
      }

      return result;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.accessPath == null ? 0 : this.accessPath.hashCode());
      result = 31 * result + (this.fact == null ? 0 : this.fact.hashCode());
      result = 31 * result + (this.resolver == null ? 0 : this.resolver.hashCode());
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
         WrappedFact other = (WrappedFact)obj;
         if (this.accessPath == null) {
            if (other.accessPath != null) {
               return false;
            }
         } else if (!this.accessPath.equals(other.accessPath)) {
            return false;
         }

         if (this.fact == null) {
            if (other.fact != null) {
               return false;
            }
         } else if (!this.fact.equals(other.fact)) {
            return false;
         }

         if (this.resolver == null) {
            if (other.resolver != null) {
               return false;
            }
         } else if (!this.resolver.equals(other.resolver)) {
            return false;
         }

         return true;
      }
   }

   public Resolver<Field, Fact, Stmt, Method> getResolver() {
      return this.resolver;
   }
}
