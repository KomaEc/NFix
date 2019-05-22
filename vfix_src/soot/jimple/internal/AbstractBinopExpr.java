package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.PrecedenceTest;
import soot.jimple.Expr;

public abstract class AbstractBinopExpr implements Expr {
   protected ValueBox op1Box;
   protected ValueBox op2Box;

   public Value getOp1() {
      return this.op1Box.getValue();
   }

   public Value getOp2() {
      return this.op2Box.getValue();
   }

   public ValueBox getOp1Box() {
      return this.op1Box;
   }

   public ValueBox getOp2Box() {
      return this.op2Box;
   }

   public void setOp1(Value op1) {
      this.op1Box.setValue(op1);
   }

   public void setOp2(Value op2) {
      this.op2Box.setValue(op2);
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      list.addAll(this.op1Box.getValue().getUseBoxes());
      list.add(this.op1Box);
      list.addAll(this.op2Box.getValue().getUseBoxes());
      list.add(this.op2Box);
      return list;
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractBinopExpr)) {
         return false;
      } else {
         AbstractBinopExpr abe = (AbstractBinopExpr)o;
         return this.op1Box.getValue().equivTo(abe.op1Box.getValue()) && this.op2Box.getValue().equivTo(abe.op2Box.getValue()) && this.getSymbol().equals(abe.getSymbol());
      }
   }

   public int equivHashCode() {
      return this.op1Box.getValue().equivHashCode() * 101 + this.op2Box.getValue().equivHashCode() + 17 ^ this.getSymbol().hashCode();
   }

   protected abstract String getSymbol();

   public abstract Object clone();

   public String toString() {
      Value op1 = this.op1Box.getValue();
      Value op2 = this.op2Box.getValue();
      String leftOp = op1.toString();
      String rightOp = op2.toString();
      return leftOp + this.getSymbol() + rightOp;
   }

   public void toString(UnitPrinter up) {
      if (PrecedenceTest.needsBrackets(this.op1Box, this)) {
         up.literal("(");
      }

      this.op1Box.toString(up);
      if (PrecedenceTest.needsBrackets(this.op1Box, this)) {
         up.literal(")");
      }

      up.literal(this.getSymbol());
      if (PrecedenceTest.needsBracketsRight(this.op2Box, this)) {
         up.literal("(");
      }

      this.op2Box.toString(up);
      if (PrecedenceTest.needsBracketsRight(this.op2Box, this)) {
         up.literal(")");
      }

   }
}
