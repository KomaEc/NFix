package soot;

import soot.util.Switch;

public class BooleanType extends PrimType implements IntegerType {
   public BooleanType(Singletons.Global g) {
   }

   public static BooleanType v() {
      return G.v().soot_BooleanType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return 474318298;
   }

   public String toString() {
      return "boolean";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseBooleanType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Boolean");
   }
}
