package soot.coffi;

import soot.Value;
import soot.jimple.IntConstant;

class CONSTANT_Integer_info extends cp_info {
   public long bytes;

   public int size() {
      return 5;
   }

   public String toString(cp_info[] constant_pool) {
      return Integer.toString((int)this.bytes);
   }

   public String typeName() {
      return "int";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_Integer_info cu = (CONSTANT_Integer_info)cp;
         return (int)this.bytes - (int)cu.bytes;
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      return IntConstant.v((int)this.bytes);
   }
}
