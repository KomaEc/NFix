package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JInvokeStmt extends AbstractStmt implements InvokeStmt {
   final ValueBox invokeExprBox;

   public JInvokeStmt(Value c) {
      this(Jimple.v().newInvokeExprBox(c));
   }

   protected JInvokeStmt(ValueBox invokeExprBox) {
      this.invokeExprBox = invokeExprBox;
   }

   public Object clone() {
      return new JInvokeStmt(Jimple.cloneIfNecessary(this.getInvokeExpr()));
   }

   public boolean containsInvokeExpr() {
      return true;
   }

   public String toString() {
      return this.invokeExprBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      this.invokeExprBox.toString(up);
   }

   public void setInvokeExpr(Value invokeExpr) {
      this.invokeExprBox.setValue(invokeExpr);
   }

   public InvokeExpr getInvokeExpr() {
      return (InvokeExpr)this.invokeExprBox.getValue();
   }

   public ValueBox getInvokeExprBox() {
      return this.invokeExprBox;
   }

   public List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      list.addAll(this.invokeExprBox.getValue().getUseBoxes());
      list.add(this.invokeExprBox);
      return list;
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseInvokeStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      InvokeExpr ie = this.getInvokeExpr();
      context.setCurrentUnit(this);
      ((ConvertToBaf)ie).convertToBaf(context, out);
      if (!ie.getMethodRef().returnType().equals(VoidType.v())) {
         Unit u = Baf.v().newPopInst(ie.getMethodRef().returnType());
         u.addAllTagsOf(this);
         out.add(u);
      }

   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
