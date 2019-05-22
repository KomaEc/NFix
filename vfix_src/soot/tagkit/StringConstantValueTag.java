package soot.tagkit;

import soot.coffi.CONSTANT_Utf8_info;
import soot.jimple.StringConstant;

public class StringConstantValueTag extends ConstantValueTag {
   private final String value;

   public StringConstantValueTag(String value) {
      this.value = value;
      this.bytes = CONSTANT_Utf8_info.toUtf8(value);
   }

   public String getStringValue() {
      return this.value;
   }

   public String toString() {
      return "ConstantValue: " + this.value;
   }

   public StringConstant getConstant() {
      return StringConstant.v(this.value);
   }

   public int hashCode() {
      int prime = true;
      int result = super.hashCode();
      result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
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
         StringConstantValueTag other = (StringConstantValueTag)obj;
         if (this.value == null) {
            if (other.value != null) {
               return false;
            }
         } else if (!this.value.equals(other.value)) {
            return false;
         }

         return true;
      }
   }
}
