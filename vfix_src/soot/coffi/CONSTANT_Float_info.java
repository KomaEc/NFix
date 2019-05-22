package soot.coffi;

import soot.Value;
import soot.jimple.FloatConstant;

class CONSTANT_Float_info extends cp_info {
   public long bytes;

   public int size() {
      return 5;
   }

   public float convert() {
      return Float.intBitsToFloat((int)this.bytes);
   }

   public String toString(cp_info[] constant_pool) {
      return Float.toString((float)this.bytes);
   }

   public String typeName() {
      return "float";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_Float_info cu = (CONSTANT_Float_info)cp;
         float d = this.convert() - cu.convert();
         return (double)d > 0.0D ? 1 : ((double)d < 0.0D ? -1 : 0);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      return FloatConstant.v(this.convert());
   }
}
