package soot;

import soot.util.Switch;

public class VoidType extends Type {
   public VoidType(Singletons.Global g) {
   }

   public static VoidType v() {
      return G.v().soot_VoidType();
   }

   public int hashCode() {
      return 982257717;
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "void";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseVoidType(this);
   }

   public boolean isAllowedInFinalCode() {
      return true;
   }
}
