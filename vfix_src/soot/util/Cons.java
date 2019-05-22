package soot.util;

public final class Cons<U, V> {
   private final U car;
   private final V cdr;

   public Cons(U car, V cdr) {
      this.car = car;
      this.cdr = cdr;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.car == null ? 0 : this.car.hashCode());
      result = 31 * result + (this.cdr == null ? 0 : this.cdr.hashCode());
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
         Cons<U, V> other = (Cons)obj;
         if (this.car == null) {
            if (other.car != null) {
               return false;
            }
         } else if (!this.car.equals(other.car)) {
            return false;
         }

         if (this.cdr == null) {
            if (other.cdr != null) {
               return false;
            }
         } else if (!this.cdr.equals(other.cdr)) {
            return false;
         }

         return true;
      }
   }

   public U car() {
      return this.car;
   }

   public V cdr() {
      return this.cdr;
   }

   public String toString() {
      return this.car.toString() + "," + this.cdr.toString();
   }
}
