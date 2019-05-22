package soot.grimp.internal;

import soot.Value;
import soot.ValueBox;
import soot.dava.internal.javaRep.DCmpExpr;
import soot.dava.internal.javaRep.DCmpgExpr;
import soot.dava.internal.javaRep.DCmplExpr;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.DivExpr;
import soot.jimple.SubExpr;
import soot.jimple.internal.AbstractIntBinopExpr;

public abstract class AbstractGrimpIntBinopExpr extends AbstractIntBinopExpr implements Precedence {
   public AbstractGrimpIntBinopExpr(Value op1, Value op2) {
      this(Grimp.v().newArgBox(op1), Grimp.v().newArgBox(op2));
   }

   protected AbstractGrimpIntBinopExpr(ValueBox op1Box, ValueBox op2Box) {
      this.op1Box = op1Box;
      this.op2Box = op2Box;
   }

   public abstract int getPrecedence();

   private String toString(Value op1, Value op2, String leftOp, String rightOp) {
      if (op1 instanceof Precedence && ((Precedence)op1).getPrecedence() < this.getPrecedence()) {
         leftOp = "(" + leftOp + ")";
      }

      if (op2 instanceof Precedence) {
         int opPrec = ((Precedence)op2).getPrecedence();
         int myPrec = this.getPrecedence();
         if (opPrec < myPrec || opPrec == myPrec && (this instanceof SubExpr || this instanceof DivExpr || this instanceof DCmpExpr || this instanceof DCmpgExpr || this instanceof DCmplExpr)) {
            rightOp = "(" + rightOp + ")";
         }
      }

      return leftOp + this.getSymbol() + rightOp;
   }

   public String toString() {
      Value op1 = this.op1Box.getValue();
      Value op2 = this.op2Box.getValue();
      String leftOp = op1.toString();
      String rightOp = op2.toString();
      return this.toString(op1, op2, leftOp, rightOp);
   }
}
