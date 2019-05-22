package soot;

import soot.util.Switch;

public class StmtAddressType extends Type {
   public StmtAddressType(Singletons.Global g) {
   }

   public static StmtAddressType v() {
      return G.v().soot_StmtAddressType();
   }

   public boolean equals(Object t) {
      return this == t;
   }

   public int hashCode() {
      return 1962109137;
   }

   public String toString() {
      return "address";
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseStmtAddressType(this);
   }
}
