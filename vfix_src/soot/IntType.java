package soot;

import soot.util.Switch;

public class IntType extends PrimType implements IntegerType {
   public IntType(Singletons.Global g) {
   }

   public static IntType v() {
      return G.v().soot_IntType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return -1220074593;
   }

   public String toString() {
      return "int";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseIntType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Integer");
   }
}
