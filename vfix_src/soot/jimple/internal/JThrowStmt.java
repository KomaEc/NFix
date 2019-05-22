package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.jimple.ThrowStmt;
import soot.util.Switch;

public class JThrowStmt extends AbstractOpStmt implements ThrowStmt {
   public JThrowStmt(Value op) {
      this(Jimple.v().newImmediateBox(op));
   }

   protected JThrowStmt(ValueBox opBox) {
      super(opBox);
   }

   public Object clone() {
      return new JThrowStmt(Jimple.cloneIfNecessary(this.getOp()));
   }

   public String toString() {
      return "throw " + this.opBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("throw");
      up.literal(" ");
      this.opBox.toString(up);
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseThrowStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)this.getOp()).convertToBaf(context, out);
      Unit u = Baf.v().newThrowInst();
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
