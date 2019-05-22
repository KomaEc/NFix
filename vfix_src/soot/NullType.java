package soot;

import soot.util.Switch;

public class NullType extends RefLikeType {
   public NullType(Singletons.Global g) {
   }

   public static NullType v() {
      return G.v().soot_NullType();
   }

   public int hashCode() {
      return -1735270431;
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "null_type";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseNullType(this);
   }

   public Type getArrayElementType() {
      throw new RuntimeException("Attempt to get array base type of a non-array");
   }
}
