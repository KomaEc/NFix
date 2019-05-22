package soot.coffi;

import soot.Value;

class CONSTANT_NameAndType_info extends cp_info {
   public int name_index;
   public int descriptor_index;

   public int size() {
      return 5;
   }

   public String toString(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index]);
      return ci.convert();
   }

   public String typeName() {
      return "nameandtype";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_NameAndType_info cu = (CONSTANT_NameAndType_info)cp;
         int i = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index])).compareTo(cp_constant_pool[cu.name_index]);
         return i != 0 ? i : ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.descriptor_index])).compareTo(cp_constant_pool[cu.descriptor_index]);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      throw new UnsupportedOperationException("cannot convert to Jimple: " + this.typeName());
   }
}
