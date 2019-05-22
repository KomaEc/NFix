package soot;

import soot.util.Switch;

public class ShortType extends PrimType implements IntegerType {
   public ShortType(Singletons.Global g) {
   }

   public static ShortType v() {
      return G.v().soot_ShortType();
   }

   public int hashCode() {
      return -1954447917;
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "short";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseShortType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Short");
   }
}
