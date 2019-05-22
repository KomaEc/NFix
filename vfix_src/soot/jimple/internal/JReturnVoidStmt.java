package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.JimpleToBafContext;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JReturnVoidStmt extends AbstractStmt implements ReturnVoidStmt {
   public Object clone() {
      return new JReturnVoidStmt();
   }

   public String toString() {
      return "return";
   }

   public void toString(UnitPrinter up) {
      up.literal("return");
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseReturnVoidStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newReturnVoidInst();
      u.addAllTagsOf(this);
      out.add(u);
   }

   public boolean fallsThrough() {
      return false;
   }

   public boolean branches() {
      return false;
   }
}
