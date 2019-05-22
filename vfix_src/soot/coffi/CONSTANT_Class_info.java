package soot.coffi;

import soot.Value;
import soot.jimple.ClassConstant;

public class CONSTANT_Class_info extends cp_info {
   public int name_index;

   public int size() {
      return 3;
   }

   public String toString(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index]);
      return ci.convert();
   }

   public String typeName() {
      return "class";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_Class_info cu = (CONSTANT_Class_info)cp;
         return ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index])).compareTo(cp_constant_pool[cu.name_index]);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index]);
      String name = ci.convert();
      return ClassConstant.v(name);
   }
}
