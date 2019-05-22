package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.JimpleToBafContext;
import soot.jimple.NopStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JNopStmt extends AbstractStmt implements NopStmt {
   public Object clone() {
      return new JNopStmt();
   }

   public String toString() {
      return "nop";
   }

   public void toString(UnitPrinter up) {
      up.literal("nop");
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseNopStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newNopInst();
      u.addAllTagsOf(this);
      out.add(u);
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
