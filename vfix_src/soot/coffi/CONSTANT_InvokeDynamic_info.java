package soot.coffi;

import soot.Value;

class CONSTANT_InvokeDynamic_info extends cp_info {
   public int bootstrap_method_index;
   public int name_and_type_index;

   public int size() {
      return 5;
   }

   public String toString(cp_info[] constant_pool) {
      cp_info bsm = constant_pool[this.bootstrap_method_index];
      cp_info nat = constant_pool[this.name_and_type_index];
      return nat.toString(constant_pool) + " - " + bsm.toString(constant_pool);
   }

   public String typeName() {
      return "invokedynamic";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_InvokeDynamic_info cu = (CONSTANT_InvokeDynamic_info)cp;
         int i = constant_pool[this.bootstrap_method_index].compareTo(constant_pool, cp_constant_pool[cu.bootstrap_method_index], cp_constant_pool);
         if (i != 0) {
            return i;
         } else {
            i = constant_pool[this.name_and_type_index].compareTo(constant_pool, cp_constant_pool[cu.name_and_type_index], cp_constant_pool);
            return i;
         }
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      throw new UnsupportedOperationException("cannot convert to Jimple: " + this.typeName());
   }
}
