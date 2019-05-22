package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JExitMonitorStmt extends AbstractOpStmt implements ExitMonitorStmt {
   public JExitMonitorStmt(Value op) {
      this(Jimple.v().newImmediateBox(op));
   }

   protected JExitMonitorStmt(ValueBox opBox) {
      super(opBox);
   }

   public Object clone() {
      return new JExitMonitorStmt(Jimple.cloneIfNecessary(this.getOp()));
   }

   public String toString() {
      return "exitmonitor " + this.opBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("exitmonitor");
      up.literal(" ");
      this.opBox.toString(up);
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseExitMonitorStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)((ConvertToBaf)this.getOp())).convertToBaf(context, out);
      Unit u = Baf.v().newExitMonitorInst();
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
