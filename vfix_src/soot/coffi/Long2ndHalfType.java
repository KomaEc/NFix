package soot.coffi;

import soot.G;
import soot.Singletons;
import soot.Type;

public class Long2ndHalfType extends Type {
   public Long2ndHalfType(Singletons.Global g) {
   }

   public static Long2ndHalfType v() {
      return G.v().soot_coffi_Long2ndHalfType();
   }

   public boolean equals(Type otherType) {
      return otherType instanceof Long2ndHalfType;
   }

   public String toString() {
      return "long2ndhalf";
   }
}
