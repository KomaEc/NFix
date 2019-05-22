package soot.tagkit;

import soot.jimple.IntConstant;

public class IntegerConstantValueTag extends ConstantValueTag {
   private final int value;

   public IntegerConstantValueTag(int value) {
      this.value = value;
      this.bytes = new byte[]{(byte)(value >> 24 & 255), (byte)(value >> 16 & 255), (byte)(value >> 8 & 255), (byte)(value & 255)};
   }

   public int getIntValue() {
      return this.value;
   }

   public String toString() {
      return "ConstantValue: " + Integer.toString(this.value);
   }

   public IntConstant getConstant() {
      return IntConstant.v(this.value);
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + this.value;
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
         IntegerConstantValueTag other = (IntegerConstantValueTag)obj;
         return this.value == other.value;
      }
   }
}
