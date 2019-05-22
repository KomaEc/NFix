package soot.jimple.internal;

import soot.UnitPrinter;
import soot.jimple.BreakpointStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JBreakpointStmt extends AbstractStmt implements BreakpointStmt {
   public String toString() {
      return "breakpoint";
   }

   public void toString(UnitPrinter up) {
      up.literal("breakpoint");
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseBreakpointStmt(this);
   }

   public Object clone() {
      return new JBreakpointStmt();
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
