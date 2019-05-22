package soot.tagkit;

import soot.jimple.LongConstant;

public class LongConstantValueTag extends ConstantValueTag {
   private final long value;

   public LongConstantValueTag(long value) {
      this.value = value;
      this.bytes = new byte[]{(byte)((int)(value >> 56 & 255L)), (byte)((int)(value >> 48 & 255L)), (byte)((int)(value >> 40 & 255L)), (byte)((int)(value >> 32 & 255L)), (byte)((int)(value >> 24 & 255L)), (byte)((int)(value >> 16 & 255L)), (byte)((int)(value >> 8 & 255L)), (byte)((int)(value & 255L))};
   }

   public long getLongValue() {
      return this.value;
   }

   public String toString() {
      return "ConstantValue: " + Long.toString(this.value);
   }

   public LongConstant getConstant() {
      return LongConstant.v(this.value);
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + (int)(this.value ^ this.value >>> 32);
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
         LongConstantValueTag other = (LongConstantValueTag)obj;
         return this.value == other.value;
      }
   }
}
