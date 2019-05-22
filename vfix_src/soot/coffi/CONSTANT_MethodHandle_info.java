package soot.coffi;

import soot.Value;

class CONSTANT_MethodHandle_info extends cp_info {
   public int kind;
   public int target_index;

   public int size() {
      return 4;
   }

   public String toString(cp_info[] constant_pool) {
      cp_info target = constant_pool[this.target_index];
      return target.toString(constant_pool);
   }

   public String typeName() {
      return "methodhandle";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_MethodHandle_info cu = (CONSTANT_MethodHandle_info)cp;
         int i = constant_pool[this.target_index].compareTo(constant_pool, cp_constant_pool[cu.target_index], cp_constant_pool);
         return i != 0 ? i : this.kind - cu.kind;
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      return constant_pool[this.target_index].createJimpleConstantValue(constant_pool);
   }
}
