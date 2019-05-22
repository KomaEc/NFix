package soot;

import soot.util.Switch;

public class ByteType extends PrimType implements IntegerType {
   public ByteType(Singletons.Global g) {
   }

   public static ByteType v() {
      return G.v().soot_ByteType();
   }

   public int hashCode() {
      return -2126703831;
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public String toString() {
      return "byte";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseByteType(this);
   }

   public RefType boxedType() {
      return RefType.v("java.lang.Byte");
   }
}
