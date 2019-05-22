package soot;

import soot.util.Switch;

public class UnknownType extends Type {
   public UnknownType(Singletons.Global g) {
   }

   public static UnknownType v() {
      return G.v().soot_UnknownType();
   }

   public int hashCode() {
      return 1554928471;
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "unknown";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseUnknownType(this);
   }

   public Type merge(Type other, Scene cm) {
      if (other instanceof RefType) {
         return other;
      } else {
         throw new RuntimeException("illegal type merge: " + this + " and " + other);
      }
   }
}
