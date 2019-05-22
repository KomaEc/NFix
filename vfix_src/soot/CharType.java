package soot;

import soot.util.Switch;

public class CharType extends PrimType implements IntegerType {
   public CharType(Singletons.Global g) {
   }

   public static CharType v() {
      return G.v().soot_CharType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "char";
   }

   public int hashCode() {
      return 1939776628;
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseCharType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Character");
   }
}
