package soot.jimple.toolkits.annotation.arraycheck;

class IntContainer {
   static IntContainer[] pool = new IntContainer[100];
   int value;

   public IntContainer(int v) {
      this.value = v;
   }

   public static IntContainer v(int v) {
      return v >= -50 && v <= 49 ? pool[v + 50] : new IntContainer(v);
   }

   public IntContainer dup() {
      return new IntContainer(this.value);
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(Object other) {
      if (other instanceof IntContainer) {
         return ((IntContainer)other).value == this.value;
      } else {
         return false;
      }
   }

   public String toString() {
      return "" + this.value;
   }

   static {
      for(int i = 0; i < 100; ++i) {
         pool[i] = new IntContainer(i - 50);
      }

   }
}
