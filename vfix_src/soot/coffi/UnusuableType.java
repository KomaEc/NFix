package soot.coffi;

import soot.G;
import soot.Singletons;
import soot.Type;

public class UnusuableType extends Type {
   public UnusuableType(Singletons.Global g) {
   }

   public static UnusuableType v() {
      return G.v().soot_coffi_UnusuableType();
   }

   public boolean equals(Type otherType) {
      return otherType instanceof UnusuableType;
   }

   public String toString() {
      return "unusuable";
   }
}
