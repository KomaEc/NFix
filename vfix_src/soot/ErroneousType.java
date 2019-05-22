package soot;

import soot.util.Switch;

public class ErroneousType extends Type {
   public ErroneousType(Singletons.Global g) {
   }

   public static ErroneousType v() {
      return G.v().soot_ErroneousType();
   }

   public int hashCode() {
      return -1840824321;
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "<error>";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseErroneousType(this);
   }
}
