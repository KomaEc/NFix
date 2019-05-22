package soot.tagkit;

import soot.jimple.FloatConstant;

public class FloatConstantValueTag extends ConstantValueTag {
   private final float value;

   public FloatConstantValueTag(float value) {
      this.value = value;
   }

   public float getFloatValue() {
      return this.value;
   }

   public String toString() {
      return "ConstantValue: " + Float.toString(this.value);
   }

   public FloatConstant getConstant() {
      return FloatConstant.v(this.value);
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + Float.floatToIntBits(this.value);
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
         FloatConstantValueTag other = (FloatConstantValueTag)obj;
         return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
      }
   }
}
