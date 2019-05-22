package soot.coffi;

import soot.Value;
import soot.jimple.StringConstant;

class CONSTANT_String_info extends cp_info {
   public int string_index;

   public int size() {
      return 3;
   }

   public String toString(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.string_index]);
      return "\"" + ci.convert() + "\"";
   }

   public String typeName() {
      return "string";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_String_info cu = (CONSTANT_String_info)cp;
         return ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.string_index])).compareTo(cp_constant_pool[cu.string_index]);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.string_index]);
      return StringConstant.v(ci.convert());
   }
}
