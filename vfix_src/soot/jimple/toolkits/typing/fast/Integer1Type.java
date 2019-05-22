package soot.jimple.toolkits.typing.fast;

import soot.G;
import soot.IntegerType;
import soot.PrimType;
import soot.RefType;
import soot.Singletons;

public class Integer1Type extends PrimType implements IntegerType {
   public static Integer1Type v() {
      return G.v().soot_jimple_toolkits_typing_fast_Integer1Type();
   }

   public Integer1Type(Singletons.Global g) {
   }

   public String toString() {
      return "[0..1]";
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Integer");
   }

   public boolean isAllowedInFinalCode() {
      return false;
   }
}
