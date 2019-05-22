package soot.tagkit;

import soot.jimple.DoubleConstant;

public class DoubleConstantValueTag extends ConstantValueTag {
   private final double value;

   public double getDoubleValue() {
      return this.value;
   }

   public DoubleConstantValueTag(double val) {
      this.value = val;
   }

   public String toString() {
      return "ConstantValue: " + Double.toString(this.value);
   }

   public DoubleConstant getConstant() {
      return DoubleConstant.v(this.value);
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      long temp = Double.doubleToLongBits(this.value);
      result = 31 * result + (int)(temp ^ temp >>> 32);
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!super.equals(obj)) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         DoubleConstantValueTag other = (DoubleConstantValueTag)obj;
         return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
      }
   }
}
