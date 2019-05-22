package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.jimple.internal.AbstractVirtualInvokeExpr;

public class GVirtualInvokeExpr extends AbstractVirtualInvokeExpr implements Precedence {
   public GVirtualInvokeExpr(Value base, SootMethodRef methodRef, List args) {
      super(Grimp.v().newObjExprBox(base), methodRef, new ValueBox[args.size()]);

      for(int i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Grimp.v().newExprBox((Value)args.get(i));
      }

   }

   public int getPrecedence() {
      return 950;
   }

   private String toString(Value op, String opString, String rightString) {
      String leftOp = opString;
      if (this.getBase() instanceof Precedence && ((Precedence)this.getBase()).getPrecedence() < this.getPrecedence()) {
         leftOp = "(" + opString + ")";
      }

      return leftOp + rightString;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("." + this.methodRef.getSignature() + "(");
      if (this.argBoxes != null) {
         for(int i = 0; i < this.argBoxes.length; ++i) {
            if (i != 0) {
               buffer.append(", ");
            }

            buffer.append(this.argBoxes[i].getValue().toString());
         }
      }

      buffer.append(")");
      return this.toString(this.getBase(), this.getBase().toString(), buffer.toString());
   }

   public void toString(UnitPrinter up) {
      if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
         up.literal("(");
      }

      this.baseBox.toString(up);
      if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
         up.literal(")");
      }

      up.literal(".");
      up.methodRef(this.methodRef);
      up.literal("(");
      if (this.argBoxes != null) {
         for(int i = 0; i < this.argBoxes.length; ++i) {
            if (i != 0) {
               up.literal(", ");
            }

            this.argBoxes[i].toString(up);
         }
      }

      up.literal(")");
   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      return new GVirtualInvokeExpr(Grimp.cloneIfNecessary(this.getBase()), this.methodRef, clonedArgs);
   }
}
