package soot.jimple.spark.ondemand;

import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.pag.AllocNode;

public class AllocAndContext {
   public final AllocNode alloc;
   public final ImmutableStack<Integer> context;

   public AllocAndContext(AllocNode alloc, ImmutableStack<Integer> context) {
      this.alloc = alloc;
      this.context = context;
   }

   public String toString() {
      return this.alloc + ", context " + this.context;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = 31 * result + this.alloc.hashCode();
      result = 31 * result + this.context.hashCode();
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
         AllocAndContext other = (AllocAndContext)obj;
         if (!this.alloc.equals(other.alloc)) {
            return false;
         } else {
            return this.context.equals(other.context);
         }
      }
   }
}
