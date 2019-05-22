package soot.jimple.toolkits.annotation.arraycheck;

class IntValueContainer {
   private static final int BOT = 0;
   private static final int TOP = 1;
   private static final int INT = 2;
   private int type;
   private int value;

   public IntValueContainer() {
      this.type = 0;
   }

   public IntValueContainer(int v) {
      this.type = 2;
      this.value = v;
   }

   public boolean isBottom() {
      return this.type == 0;
   }

   public boolean isTop() {
      return this.type == 1;
   }

   public boolean isInteger() {
      return this.type == 2;
   }

   public int getValue() {
      if (this.type != 2) {
         throw new RuntimeException("IntValueContainer: not integer type");
      } else {
         return this.value;
      }
   }

   public void setTop() {
      this.type = 1;
   }

   public void setValue(int v) {
      this.type = 2;
      this.value = v;
   }

   public void setBottom() {
      this.type = 0;
   }

   public String toString() {
      if (this.type == 0) {
         return "[B]";
      } else {
         return this.type == 1 ? "[T]" : "[" + this.value + "]";
      }
   }

   public boolean equals(Object other) {
      if (!(other instanceof IntValueContainer)) {
         return false;
      } else {
         IntValueContainer otherv = (IntValueContainer)other;
         if (this.type == 2 && otherv.type == 2) {
            return this.value == otherv.value;
         } else {
            return this.type == otherv.type;
         }
      }
   }

   public IntValueContainer dup() {
      IntValueContainer other = new IntValueContainer();
      other.type = this.type;
      other.value = this.value;
      return other;
   }
}
