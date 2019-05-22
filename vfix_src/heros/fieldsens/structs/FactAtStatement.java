package heros.fieldsens.structs;

public class FactAtStatement<Fact, Stmt> {
   public final Fact fact;
   public final Stmt stmt;

   public FactAtStatement(Fact fact, Stmt stmt) {
      this.fact = fact;
      this.stmt = stmt;
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
         FactAtStatement other = (FactAtStatement)obj;
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
