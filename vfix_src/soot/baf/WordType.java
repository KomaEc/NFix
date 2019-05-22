package soot.baf;

import soot.G;
import soot.Singletons;
import soot.Type;
import soot.util.Switch;

public class WordType extends Type {
   public WordType(Singletons.Global g) {
   }

   public static WordType v() {
      return G.v().soot_baf_WordType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return -1220074593;
   }

   public String toString() {
      return "word";
   }

   public void apply(Switch sw) {
      throw new RuntimeException("invalid switch case");
   }
}
