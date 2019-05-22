package soot.toolkits.scalar;

public class BinaryIdentitySet<T> {
   protected final T o1;
   protected final T o2;
   protected final int hashCode;

   public BinaryIdentitySet(T o1, T o2) {
      this.o1 = o1;
      this.o2 = o2;
      this.hashCode = this.computeHashCode();
   }

   public int hashCode() {
      return this.hashCode;
   }

   private int computeHashCode() {
      int result = 1;
      int result = result + System.identityHashCode(this.o1);
      result += System.identityHashCode(this.o2);
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
         BinaryIdentitySet other = (BinaryIdentitySet)obj;
         if (this.o1 != other.o1 && this.o1 != other.o2) {
            return false;
         } else {
            return this.o2 == other.o2 || this.o2 == other.o1;
         }
      }
   }

   public T getO1() {
      return this.o1;
   }

   public T getO2() {
      return this.o2;
   }

   public String toString() {
      return "IdentityPair " + this.o1 + "," + this.o2;
   }
}
