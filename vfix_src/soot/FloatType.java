package soot;

import soot.util.Switch;

public class FloatType extends PrimType {
   public FloatType(Singletons.Global g) {
   }

   public static FloatType v() {
      return G.v().soot_FloatType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return -1471974406;
   }

   public String toString() {
      return "float";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseFloatType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Float");
   }
}
