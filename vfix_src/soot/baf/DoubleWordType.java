package soot.baf;

import soot.G;
import soot.Singletons;
import soot.Type;
import soot.util.Switch;

public class DoubleWordType extends Type {
   public DoubleWordType(Singletons.Global g) {
   }

   public static DoubleWordType v() {
      return G.v().soot_baf_DoubleWordType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return -1572371553;
   }

   public String toString() {
      return "dword";
   }

   public void apply(Switch sw) {
      throw new RuntimeException("invalid switch case");
   }
}
