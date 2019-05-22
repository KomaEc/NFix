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
import soot.jimple.ReturnStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JReturnStmt extends AbstractOpStmt implements ReturnStmt {
   public JReturnStmt(Value returnValue) {
      this(Jimple.v().newImmediateBox(returnValue));
   }

   protected JReturnStmt(ValueBox returnValueBox) {
      super(returnValueBox);
   }

   public Object clone() {
      return new JReturnStmt(Jimple.cloneIfNecessary(this.getOp()));
   }

   public String toString() {
      return "return " + this.opBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("return");
      up.literal(" ");
      this.opBox.toString(up);
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseReturnStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)((ConvertToBaf)this.getOp())).convertToBaf(context, out);
      Unit u = Baf.v().newReturnInst(this.getOp().getType());
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
