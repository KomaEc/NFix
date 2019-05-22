package soot.coffi;

import soot.UnitPrinter;
import soot.jimple.StmtSwitch;
import soot.jimple.internal.AbstractStmt;
import soot.util.Switch;

class FutureStmt extends AbstractStmt {
   public Object object;

   public FutureStmt(Object object) {
      this.object = object;
   }

   public FutureStmt() {
   }

   public String toString() {
      return "<futurestmt>";
   }

   public void toString(UnitPrinter up) {
      up.literal("<futurestmt>");
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).defaultCase(this);
   }

   public boolean fallsThrough() {
      throw new RuntimeException();
   }

   public boolean branches() {
      throw new RuntimeException();
   }

   public Object clone() {
      throw new RuntimeException();
   }
}
