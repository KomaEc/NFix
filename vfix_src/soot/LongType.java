package soot;

import soot.util.Switch;

public class LongType extends PrimType {
   public LongType(Singletons.Global g) {
   }

   public static LongType v() {
      return G.v().soot_LongType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return 37593207;
   }

   public String toString() {
      return "long";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseLongType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Long");
   }
}
