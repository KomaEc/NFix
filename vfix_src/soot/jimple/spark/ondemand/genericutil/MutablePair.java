package soot.jimple.spark.ondemand.genericutil;

public class MutablePair<T, U> {
   private T o1;
   private U o2;

   public MutablePair(T o1, U o2) {
      this.o1 = o1;
      this.o2 = o2;
   }

   public int hashCode() {
      return this.o1.hashCode() + this.o2.hashCode();
   }

   public boolean equals(Object other) {
      if (!(other instanceof MutablePair)) {
         return false;
      } else {
         MutablePair p = (MutablePair)other;
         return this.o1.equals(p.o1) && this.o2.equals(p.o2);
      }
   }

   public String toString() {
      return "Pair " + this.o1 + "," + this.o2;
   }

   public T getO1() {
      return this.o1;
   }

   public U getO2() {
      return this.o2;
   }

   public void setO1(T o1) {
      this.o1 = o1;
   }

   public void setO2(U o2) {
      this.o2 = o2;
   }
}
