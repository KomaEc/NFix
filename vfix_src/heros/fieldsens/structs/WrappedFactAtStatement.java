package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.Resolver;

public class WrappedFactAtStatement<Field, Fact, Stmt, Method> {
   private WrappedFact<Field, Fact, Stmt, Method> fact;
   private Stmt stmt;

   public WrappedFactAtStatement(Stmt stmt, WrappedFact<Field, Fact, Stmt, Method> fact) {
      this.stmt = stmt;
      this.fact = fact;
   }

   public WrappedFact<Field, Fact, Stmt, Method> getWrappedFact() {
      return this.fact;
   }

   public Fact getFact() {
      return this.fact.getFact();
   }

   public AccessPath<Field> getAccessPath() {
      return this.fact.getAccessPath();
   }

   public Resolver<Field, Fact, Stmt, Method> getResolver() {
      return this.fact.getResolver();
   }

   public Stmt getStatement() {
      return this.stmt;
   }

   public FactAtStatement<Fact, Stmt> getAsFactAtStatement() {
      return new FactAtStatement(this.fact.getFact(), this.stmt);
   }

   public boolean canDeltaBeApplied(AccessPath.Delta<Field> delta) {
      return delta.canBeAppliedTo(this.fact.getAccessPath());
   }

   public String toString() {
      return this.fact + " @ " + this.stmt;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.fact == null ? 0 : this.fact.hashCode());
      result = 31 * result + (this.stmt == null ? 0 : this.stmt.hashCode());
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
         WrappedFactAtStatement other = (WrappedFactAtStatement)obj;
         if (this.fact == null) {
            if (other.fact != null) {
               return false;
            }
         } else if (!this.fact.equals(other.fact)) {
            return false;
         }

         if (this.stmt == null) {
            if (other.stmt != null) {
               return false;
            }
         } else if (!this.stmt.equals(other.stmt)) {
            return false;
         }

         return true;
      }
   }
}
