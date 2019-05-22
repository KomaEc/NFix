package soot.coffi;

import soot.Value;
import soot.jimple.DoubleConstant;

class CONSTANT_Double_info extends cp_info {
   public long high;
   public long low;

   public int size() {
      return 9;
   }

   public double convert() {
      return Double.longBitsToDouble(ints2long(this.high, this.low));
   }

   public String toString(cp_info[] constant_pool) {
      return Double.toString(this.convert());
   }

   public String typeName() {
      return "double";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_Double_info cu = (CONSTANT_Double_info)cp;
         double d = this.convert() - cu.convert();
         return d > 0.0D ? 1 : (d < 0.0D ? -1 : 0);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      return DoubleConstant.v(this.convert());
   }
}
