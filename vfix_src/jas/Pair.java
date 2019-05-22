package jas;

public class Pair<T, U> {
   protected T o1;
   protected U o2;

   public Pair() {
      this.o1 = null;
      this.o2 = null;
   }

   public Pair(T o1, U o2) {
      this.o1 = o1;
      this.o2 = o2;
   }

   public int hashCode() {
      return this.o1.hashCode() + this.o2.hashCode();
   }

   public boolean equals(Object other) {
      if (!(other instanceof Pair)) {
         return false;
      } else {
         Pair p = (Pair)other;
         return this.o1.equals(p.o1) && this.o2.equals(p.o2);
      }
   }

   public String toString() {
      return "<" + this.o1 + "," + this.o2 + ">";
   }

   public T getO1() {
      return this.o1;
   }

   public U getO2() {
      return this.o2;
   }
}
