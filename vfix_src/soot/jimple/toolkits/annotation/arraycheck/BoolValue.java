package soot.jimple.toolkits.annotation.arraycheck;

class BoolValue {
   private boolean isRectangular;
   private static final BoolValue trueValue = new BoolValue(true);
   private static final BoolValue falseValue = new BoolValue(false);

   public BoolValue(boolean v) {
      this.isRectangular = v;
   }

   public static BoolValue v(boolean v) {
      return v ? trueValue : falseValue;
   }

   public boolean getValue() {
      return this.isRectangular;
   }

   public boolean or(BoolValue other) {
      if (other.getValue()) {
         this.isRectangular = true;
      }

      return this.isRectangular;
   }

   public boolean or(boolean other) {
      if (other) {
         this.isRectangular = true;
      }

      return this.isRectangular;
   }

   public boolean and(BoolValue other) {
      if (!other.getValue()) {
         this.isRectangular = false;
      }

      return this.isRectangular;
   }

   public boolean and(boolean other) {
      if (!other) {
         this.isRectangular = false;
      }

      return this.isRectangular;
   }

   public int hashCode() {
      return this.isRectangular ? 1 : 0;
   }

   public boolean equals(Object other) {
      if (other instanceof BoolValue) {
         return this.isRectangular == ((BoolValue)other).getValue();
      } else {
         return false;
      }
   }

   public String toString() {
      return "[" + this.isRectangular + "]";
   }
}
