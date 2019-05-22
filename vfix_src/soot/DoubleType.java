package soot;

import soot.util.Switch;

public class DoubleType extends PrimType {
   public DoubleType(Singletons.Global g) {
   }

   public static DoubleType v() {
      return G.v().soot_DoubleType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return 1268609602;
   }

   public String toString() {
      return "double";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseDoubleType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Double");
   }
}
