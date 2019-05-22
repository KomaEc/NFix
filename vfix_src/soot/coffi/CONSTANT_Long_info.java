package soot.coffi;

import soot.Value;
import soot.jimple.LongConstant;

class CONSTANT_Long_info extends cp_info {
   public long high;
   public long low;

   public int size() {
      return 9;
   }

   public long convert() {
      return ints2long(this.high, this.low);
   }

   public String toString(cp_info[] constant_pool) {
      return "(" + this.high + "," + this.low + ") = " + Long.toString(this.convert());
   }

   public String typeName() {
      return "long";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_Long_info cu = (CONSTANT_Long_info)cp;
         long d = this.convert() - cu.convert();
         return d > 0L ? 1 : (d < 0L ? -1 : 0);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      return LongConstant.v(this.convert());
   }
}
