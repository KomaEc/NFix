package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode;

public class LineMutatorPair {
   private final int lineNumber;
   private final String mutator;

   public LineMutatorPair(int lineNumber, String mutator) {
      this.lineNumber = lineNumber;
      this.mutator = mutator;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.lineNumber;
      result = 31 * result + (this.mutator == null ? 0 : this.mutator.hashCode());
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
         LineMutatorPair other = (LineMutatorPair)obj;
         if (this.lineNumber != other.lineNumber) {
            return false;
         } else {
            if (this.mutator == null) {
               if (other.mutator != null) {
                  return false;
               }
            } else if (!this.mutator.equals(other.mutator)) {
               return false;
            }

            return true;
         }
      }
   }
}
